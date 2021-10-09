package com.pendaftaran.ktp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pendaftaran.ktp.R;
import com.pendaftaran.ktp.auth.RegistrationDetailActivity;
import com.pendaftaran.ktp.databinding.ActivityHomepageDetailBinding;

import org.jetbrains.annotations.NotNull;

public class HomepageDetail extends AppCompatActivity {

    public static final String EXTRA_DETAIL = "detail";
    private ActivityHomepageDetailBinding binding;
    private HomepageModel model;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_DETAIL);

        if(model.getStatus().equals("Menunggu")) {
            binding.acc.setVisibility(View.VISIBLE);
            binding.ignore.setVisibility(View.VISIBLE);
        }

        Glide.with(this)
                .load(model.getSign())
                .into(binding.dp);

        Glide.with(this)
                .load(model.getSign())
                .into(binding.sign);


        binding.textView6.setText(model.getName());
        binding.nik.setText("NIK: " + model.getNik());
        binding.noKK.setText("Nomor KK" + model.getNoKK());
        binding.name.setText(model.getName());
        binding.age.setText(model.getAge());
        binding.email.setText(model.getEmail());
        binding.phone.setText(model.getPhone());
        binding.pos.setText(model.getPos());
        binding.address.setText(model.getAddress());
        binding.status.setText("Status: " + model.getStatus());


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus("Diterima", "Menerima Pendaftaran KTP");
            }
        });

        binding.ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus("Ditolak", "Menolak Pendaftaran KTP");
            }
        });

        binding.seeOtherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageDetail.this, RegistrationDetailActivity.class);
                intent.putExtra(RegistrationDetailActivity.EXTRA_DOCUMENT, model.getDocument());
                startActivity(intent);
            }
        });

    }

    private void updateStatus(String result, String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("Apakah anda yakin ingin ''" + title + "'' ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    /// TERIMA PENDAFTARAN
                    updateDocument(result, title);
                    dialogInterface.dismiss();
                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();

    }

    private void updateDocument(String result, String title) {

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection("registration")
                .document(model.getUid())
                .update("status", result)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            showSuccessDialog(title);
                        } else {
                            mProgressDialog.dismiss();
                            showFailureDialog(title);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Log.e("TAG", e.getMessage());
                    showFailureDialog(title);
                });

    }

    @SuppressLint("SetTextI18n")
    private void showSuccessDialog(String title) {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil")
                .setMessage("Sukses " + title.toLowerCase())
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                   dialogInterface.dismiss();
                   if(title.equals("Menerima Pendaftaran KTP")) {
                       binding.status.setText("Status: Diterima" );
                   } else {
                       binding.status.setText("Status: Ditolak" );
                   }
                   onBackPressed();
                })
                .show();
    }

    private void showFailureDialog(String title) {
        new AlertDialog.Builder(this)
                .setTitle("Gagal")
                .setMessage("Gagal " + title.toLowerCase())
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}