package com.example.finalejava;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ViewPhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.zoomview);

        // Get the URL from the intent
        String imageUrl = getIntent().getStringExtra("image_url");
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageView);
        } else {
            // Handle the case where imageUrl is null
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }

        ImageView backButton = findViewById(R.id.imageViewback);
        backButton.setOnClickListener(view -> finish());
    }
}