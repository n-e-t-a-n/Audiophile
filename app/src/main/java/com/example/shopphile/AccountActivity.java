package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Edit Profile Picture Button
        Button editProfilePicButton = findViewById(R.id.edit_profile_pic_button);
        editProfilePicButton.setOnClickListener(v ->
                Toast.makeText(AccountActivity.this, "Edit Profile Picture Clicked", Toast.LENGTH_SHORT).show()
        );

        // Log Out Button
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

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