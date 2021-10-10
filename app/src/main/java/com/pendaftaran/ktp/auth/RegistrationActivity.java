package com.pendaftaran.ktp.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pendaftaran.ktp.R;
import com.pendaftaran.ktp.databinding.ActivityRegistrationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private String dp, sign;
    private ArrayList<String> document = new ArrayList<>();
    private static final int REQUEST_FROM_GALLERY_SELF_PHOTO = 1001;
    private static final int REQUEST_FROM_GALLERY_SIGN = 1002;
    private static final int REQUEST_FROM_GALLERY_DOCUMENT = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// kembali
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /// tambahkan foto formal
        binding.hint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegistrationActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .start(REQUEST_FROM_GALLERY_SELF_PHOTO);
            }
        });


        /// tambahkan foto tanda tangan
        binding.hint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegistrationActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .start(REQUEST_FROM_GALLERY_SIGN);
            }
        });


        /// tambahkan foto dokumen lain
        binding.otherDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegistrationActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .start(REQUEST_FROM_GALLERY_DOCUMENT);
            }
        });


        /// lihat dokumen dokumen lain yang sudah di upload
        binding.seeOtherTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, RegistrationDetailActivity.class);
                intent.putExtra(RegistrationDetailActivity.EXTRA_DOCUMENT, document);
                startActivity(intent);
            }
        });


        /// klik registrasi
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formValidation();
            }
        });
    }

    private void formValidation() {
        String nik = binding.nik.getText().toString().trim();
        String noKK = binding.noKK.getText().toString().trim();
        String name = binding.name.getText().toString().trim();
        String age = binding.usia.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String phone = binding.phone.getText().toString().trim();
        String pos = binding.kodePos.getText().toString().trim();
        String address = binding.address.getText().toString().trim();

        if(nik.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(noKK.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Nomor Kartu Keluarga tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(name.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Nama Lengkap tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(age.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Usia tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(email.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(phone.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Nomor Handphone tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(pos.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Kode POS tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if(address.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Alamat tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (dp == null) {
            Toast.makeText(RegistrationActivity.this, "Foto Formal tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (sign == null) {
            Toast.makeText(RegistrationActivity.this, "Foto Tanda Tangan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (document.size() == 0) {
            Toast.makeText(RegistrationActivity.this, "Dokumen lainnya tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }


        /// simpan data registrasi ke firebase
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String uid = String.valueOf(System.currentTimeMillis());

        Map<String, Object> registration = new HashMap<>();
        registration.put("nik", nik);
        registration.put("noKK", noKK);
        registration.put("name", name);
        registration.put("age", age);
        registration.put("email", email);
        registration.put("phone", phone);
        registration.put("pos", pos);
        registration.put("address",address);
        registration.put("dp", dp);
        registration.put("sign", sign);
        registration.put("document", document);
        registration.put("uid", uid);
        registration.put("status", "Menunggu");

        FirebaseFirestore
                .getInstance()
                .collection("registration")
                .document(uid)
                .set(registration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            showSuccessDialog();
                        } else {
                            mProgressDialog.dismiss();
                            showFailureDialog();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                        Log.e("ERR REGISTRATION", e.getMessage());
                    }
                });

    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Registrasi KTP")
                .setMessage("Terdapat kesalahan ketika mengunggah data KTP, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Registrasi KTP")
                .setMessage("Pendaftaran anda akan segera si proses oleh admin, silahkan tunggu dan bersabar\n\nTerima Kasih.")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_SELF_PHOTO) {
                uploadArticleDp(data.getData(), "self");
            } else if (requestCode == REQUEST_FROM_GALLERY_SIGN) {
                uploadArticleDp(data.getData(), "sign");
            } else if (requestCode == REQUEST_FROM_GALLERY_DOCUMENT) {
                uploadArticleDp(data.getData(), "document");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void uploadArticleDp(Uri data, String option) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = option + "/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    if(option.equals("self")) {
                                        dp = uri.toString();
                                        binding.hint1.setVisibility(View.GONE);
                                        Glide
                                                .with(this)
                                                .load(dp)
                                                .into(binding.formal);
                                    } else if(option.equals("sign")) {
                                        sign = uri.toString();
                                        binding.hint2.setVisibility(View.GONE);
                                        Glide
                                                .with(this)
                                                .load(sign)
                                                .into(binding.tandaTangan);
                                    } else {
                                        document.add(uri.toString());
                                        binding.otherDocument.setText("Dokumen sudah ditambahkan (" + document.size() + ")");
                                    }
                                    Toast.makeText(RegistrationActivity.this, "Berhasil mengunggah gambar", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}