package com.example.finalejava.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalejava.R;
import com.example.finalejava.ViewPhoto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<Root> list;

    public imageAdapter(ArrayList<Root> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Root root = list.get(position);

        Glide.with(context).load(list.get(position).urls.regular)
                .into(holder.imageView);
        holder.heartIcon.setOnClickListener(v -> {
            checkAndAddToFavorites(root);

        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPhoto.class);
                intent.putExtra("image_url",root.urls.regular);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView heartIcon;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            heartIcon = itemView.findViewById(R.id.heart_icon);
        }
    }

    private void checkAndAddToFavorites(Root root) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("favorites")
                    .whereEqualTo("id", root.id)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // Photo is not in favorites, add it with user ID
                                root.setUserId(userId); // Ensure Root class has a setUserId method
                                db.collection("favorites").add(root)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(context, "Success added", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(context, "Photo is already in favorites", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }





}
