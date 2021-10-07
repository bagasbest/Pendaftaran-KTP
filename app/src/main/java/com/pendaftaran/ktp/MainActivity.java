package com.pendaftaran.ktp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.pendaftaran.ktp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this).load(R.drawable.logo).into(binding.imageView);

        // delay 4 detik sebelum masuk ke dashboard
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }, 4000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}