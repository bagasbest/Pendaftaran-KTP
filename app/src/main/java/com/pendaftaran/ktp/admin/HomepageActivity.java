package com.pendaftaran.ktp.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pendaftaran.ktp.DashboardActivity;
import com.pendaftaran.ktp.R;
import com.pendaftaran.ktp.databinding.ActivityHomepageBinding;

public class HomepageActivity extends AppCompatActivity {

    private ActivityHomepageBinding binding;
    private HomepageAdapter adapter;


    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        initViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// ambil nama admin
        getName();

        binding.exit.setOnClickListener(view -> showConformationLogout());
    }

    @SuppressLint("SetTextI18n")
    private void getName() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore
                .getInstance()
                .collection("admin")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> binding.greeting.setText("Selamat Datang, " + documentSnapshot.get("name")));
    }

    private void initRecyclerView() {
        binding.rvRegistration.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomepageAdapter();
        binding.rvRegistration.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan daftar registrasi peserta
        HomepageViewModel viewModel = new ViewModelProvider(this).get(HomepageViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setListHomepage();
        viewModel.getHomepage().observe(this, registration -> {
            if (registration.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(registration);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void showConformationLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah anda yakin ingin logout ?")
                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // sign out dari firebase autentikasi
                    FirebaseAuth.getInstance().signOut();

                    // go to login activity
                    Intent intent = new Intent(HomepageActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogInterface.dismiss();
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}