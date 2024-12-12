package com.example.shopphile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView profileImage;

    private static final String SHARED_PREFS = "ShopphilePrefs";
    private static final String PROFILE_IMAGE_URI_KEY = "ProfileImageUri";

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        saveProfileImageUri(selectedImageUri.toString());
                        profileImage.setImageURI(selectedImageUri);
                        Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Profile Image View
        profileImage = findViewById(R.id.profile_image);

        // Load the saved profile image
        loadProfileImage();

        // Initialize the Logout button
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        
        // Edit Profile Picture Button
        Button editProfilePicButton = findViewById(R.id.edit_profile_pic_button);
        editProfilePicButton.setOnClickListener(v -> openFilePicker());

        // Set up navigation bar click listeners
        setupNavigationBar();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Log out the user from Firebase
                    mAuth.signOut();
                    Toast.makeText(AccountActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveProfileImageUri(String uri) {
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .edit()
                .putString(PROFILE_IMAGE_URI_KEY, uri)
                .apply();
    }

    private void loadProfileImage() {
        String uri = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                .getString(PROFILE_IMAGE_URI_KEY, null);
        if (uri != null) {
            profileImage.setImageURI(Uri.parse(uri));
        }
    }

    private void setupNavigationBar() {
        // Home navigation
        LinearLayout homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close AccountActivity
        });

        // Orders navigation
        LinearLayout ordersButton = findViewById(R.id.orders_button);
        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, OrdersActivity.class);
            startActivity(intent);
            finish(); // Close AccountActivity
        });

        // Account navigation (current activity)
        LinearLayout accountButton = findViewById(R.id.account);
        accountButton.setOnClickListener(v -> {
            Toast.makeText(AccountActivity.this, "You're already on the Account page", Toast.LENGTH_SHORT).show();
        });
    }
}
