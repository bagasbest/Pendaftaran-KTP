package com.pendaftaran.ktp.utils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pendaftaran.ktp.admin.HomepageActivity;
import com.pendaftaran.ktp.R;
import com.pendaftaran.ktp.databinding.ActivityRegisterBinding;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formValidation();
            }
        });
    }

    private void formValidation() {
        String email = binding.emailEt.getText().toString().trim();
        String pass = binding.passwordEt.getText().toString().trim();
        String name = binding.nameEt.getText().toString().trim();

        if(email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT ).show();
            return;
        } else if (pass.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Kata sandi tidak boleh kosong!", Toast.LENGTH_SHORT ).show();
            return;
        } else if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT ).show();
            return;
        }

        /// registrasi admin
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // simpan data ke dalam database
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("email", email);
                        user.put("password", pass);
                        user.put("uid", uid);

                        FirebaseFirestore
                                .getInstance()
                                .collection("admin")
                                .document(uid)
                                .set(user)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        showSuccessDialog();
                                    } else {
                                        mProgressDialog.dismiss();
                                        showFailureDialog();
                                    }
                                });

                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sukses Registrasi")
                .setMessage("Sukses registrasi sebagai admin")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, HomepageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Registrasi")
                .setMessage("Terdapat kesalahan ketika registrasi, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}