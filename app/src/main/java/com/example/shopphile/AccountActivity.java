package com.example.shopphile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Profile Image View
        profileImage = findViewById(R.id.profile_image);

        // Edit Profile Picture Button
        Button editProfilePicButton = findViewById(R.id.edit_profile_pic_button);
        editProfilePicButton.setOnClickListener(v -> openFilePicker());

        // Log Out Button
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        profileImage.setImageURI(selectedImageUri);
                        // Persist the permission to read the selected file
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Log out the user from Firebase
                    mAuth.signOut();
                    Toast.makeText(AccountActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to LoginActivity
                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
