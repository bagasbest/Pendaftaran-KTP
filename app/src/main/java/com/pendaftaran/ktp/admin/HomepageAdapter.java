package com.pendaftaran.ktp.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pendaftaran.ktp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.ViewHolder> {

    private final ArrayList<HomepageModel> listRegistration = new ArrayList<>();
    public void setData(ArrayList<HomepageModel> items) {
        listRegistration.clear();
        listRegistration.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homepage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listRegistration.get(position));
    }

    @Override
    public int getItemCount() {
        return listRegistration.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cv;
        ImageView dp;
        View view;
        TextView name, nik, noKK, age, status;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            dp = itemView.findViewById(R.id.dp);
            view = itemView.findViewById(R.id.view);
            name = itemView.findViewById(R.id.name);
            nik = itemView.findViewById(R.id.nik);
            noKK = itemView.findViewById(R.id.noKK);
            age = itemView.findViewById(R.id.age);
            status = itemView.findViewById(R.id.status);
        }

        @SuppressLint("SetTextI18n")
        public void bind(HomepageModel model) {

            Glide.with(itemView.getContext())
                    .load(model.getDp())
                    .into(dp);

            name.setText(model.getName());
            nik.setText("NIK: " + model.getNik());
            noKK.setText("No.KK: " + model.getNoKK());
            age.setText("Usia: " + model.getAge() + " Tahun");
            status.setText(model.getStatus());

            switch (model.getStatus()) {
                case "Menunggu":
                    view.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_waiting));
                    break;
                case "Diterima":
                    view.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_accepted));
                    break;
                case "Ditolak":
                    view.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_ignore));
                    break;
            }

            cv.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), HomepageDetail.class);
                intent.putExtra(HomepageDetail.EXTRA_DETAIL, model);
                itemView.getContext().startActivity(intent);
            });

        }
    }
}
