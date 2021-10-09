package com.pendaftaran.ktp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.pendaftaran.ktp.databinding.ActivityRegistrationDetailBinding;
import com.pendaftaran.ktp.utils.RegistrationAdapter;
import java.util.ArrayList;

public class RegistrationDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DOCUMENT = "document";
    private ActivityRegistrationDetailBinding binding;
    private final ArrayList<String> document = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        document.addAll(getIntent().getStringArrayListExtra(EXTRA_DOCUMENT));

        if(document.size() > 0) {
            // set up the RecyclerView
            binding.rvRegistration.setLayoutManager(new LinearLayoutManager(this));
            RegistrationAdapter adapter = new RegistrationAdapter();
            adapter.setData(document);
            binding.rvRegistration.setAdapter(adapter);
        } else {
            binding.noData.setVisibility(View.VISIBLE);
        }


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}