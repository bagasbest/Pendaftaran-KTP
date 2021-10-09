package com.pendaftaran.ktp.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pendaftaran.ktp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.ViewHolder> {

    private final ArrayList<String> document = new ArrayList<>();
    public void setData(ArrayList<String> items) {
        document.clear();
        document.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(document.get(position));
    }

    @Override
    public int getItemCount() {
        return document.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }

        public void bind(String model) {
            Log.e("TAG", model);
            Glide.with(itemView.getContext())
                    .load(model)
                    .into(image);
        }
    }
}
