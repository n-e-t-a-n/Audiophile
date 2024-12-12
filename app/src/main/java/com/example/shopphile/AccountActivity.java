package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopphile.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //Push test
        // Edit Profile Picture Button
        Button editProfilePicButton = findViewById(R.id.edit_profile_pic_button);
        editProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to allow the user to select and update profile picture
                Toast.makeText(AccountActivity.this, "Edit Profile Picture Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Log Out Button
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to log out the user and redirect to the login page
                Toast.makeText(AccountActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                // Redirect to LoginActivity or MainActivity
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
