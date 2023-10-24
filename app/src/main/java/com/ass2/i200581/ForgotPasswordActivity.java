package com.ass2.i200581;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);

        View buttonResetPassword = findViewById(R.id.buttonResetPassword);
        View buttonGoBackForgot = findViewById(R.id.buttonGoBackForgot);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                final String newPassword = editTextNewPassword.getText().toString();

                // Check if email and new password are not empty
                int toastDuration = Toast.LENGTH_SHORT;
                if (!email.isEmpty() && !newPassword.isEmpty()) {
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password reset email sent successfully
                                        // Now, update the user's password in Firebase
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user != null) {
                                            user.updatePassword(newPassword)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ForgotPasswordActivity.this, "Password reset and updated successfully.", toastDuration).show();
                                                            } else {
                                                                Toast.makeText(ForgotPasswordActivity.this, "Password update failed.", toastDuration).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Invalid email or other error
                                        Toast.makeText(ForgotPasswordActivity.this, "Invalid email or an error occurred.", toastDuration).show();
                                    }
                                }
                            });
                } else {
                    // Email or password fields are empty
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter both email and new password.", toastDuration).show();
                }
            }
        });

        buttonGoBackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
