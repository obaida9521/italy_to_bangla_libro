package com.developerobaida.italytobanglalibro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerobaida.italytobanglalibro.R;
import com.developerobaida.italytobanglalibro.activities.MainActivity2;
import com.developerobaida.italytobanglalibro.databinding.ItemCategoryBinding;
import com.developerobaida.italytobanglalibro.models.ModelCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {

    Context context;
    List<ModelCategory> arrayList;

    public AdapterCategory(Context context, List<ModelCategory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            ModelCategory model = arrayList.get(position);

            Picasso.get().load("https://eyasinit.xyz/api/category_image/" + model.getImage())
                    .placeholder(R.drawable.translate_24).into(holder.binding.image);

            holder.binding.title.setText(model.getTitle());

            holder.binding.getRoot().setOnClickListener(view -> {

                Intent intent = new Intent(context, MainActivity2.class);
                intent.putExtra("title", model.getTitle());
                context.startActivity(intent);

            });
        } catch (Exception e) {
            Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;

        public MyViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}