package com.example.shopphile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView userEmail;
    private Button logoutButton;
    private FirebaseAuth auth;

    private SharedPreferences sharedPreferences;

    // File picker launcher
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Display and save the image URI locally
                        Glide.with(this).load(selectedImageUri).into(profileImage);
                        saveImageUri(selectedImageUri.toString());
                        Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Image selection canceled.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize views
        profileImage = findViewById(R.id.profile_picture);
        userEmail = findViewById(R.id.user_email);
        logoutButton = findViewById(R.id.logout_button);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);

        loadUserProfile();  // Load user data and saved image

        // Logout button
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            onBackPressed(); // Navigate back to the previous activity
        });

        // Image picker
        profileImage.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        filePickerLauncher.launch(intent);
    }

    private void saveImageUri(String uri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_image_uri", uri);
        editor.apply();
    }

    private void loadUserProfile() {
        // Load email
        if (auth.getCurrentUser() != null) {
            userEmail.setText(auth.getCurrentUser().getEmail());
        }

        // Load profile image
        String imageUri = sharedPreferences.getString("profile_image_uri", null);
        if (imageUri != null) {
            Glide.with(this).load(Uri.parse(imageUri)).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.account_circle_24px);  // Default image
        }
    }
}
