package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText_Register);
        passwordEditText = findViewById(R.id.passwordEditText_Register);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText_Register);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        loginLink.setOnClickListener(v -> navigateToLogin());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                        String name = email.replace(".", " ").split("@")[0];
                        StringBuilder capitalized = new StringBuilder();
                        for (String word : name.split(" ")) {
                            if (!word.isEmpty()) {
                                capitalized.append(word.substring(0, 1).toUpperCase())
                                        .append(word.substring(1).toLowerCase())
                                        .append(" ");
                            }
                        }

                        name = capitalized.toString().trim();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("name", name);
                        userData.put("profile_picture_url", "");
                        userData.put("created_at", FieldValue.serverTimestamp());

                        userData.put("cart", new ArrayList<>());
                        userData.put("orders", new ArrayList<>());

                        db.collection("users")
                                .document(email)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "User document created with email: " + email);

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error creating user document: " + e.getMessage());
                                    Toast.makeText(RegisterActivity.this, "Error creating user document", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
