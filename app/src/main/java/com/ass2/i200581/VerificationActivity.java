package com.ass2.i200581;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    private EditText[] inputSpaces;
    private Button[] numpadButtons;
    private Button submitButton;
    private Button requestCodeButton; // Added request code button
    private FirebaseAuth firebaseAuth;

    private String otp;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Retrieve the OTP from the intent
        String userid = getIntent().getStringExtra("userid");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String contact = getIntent().getStringExtra("contactno");
        String city = getIntent().getStringExtra("city");
        String country = getIntent().getStringExtra("country");

        inputSpaces = new EditText[5];
        inputSpaces[0] = findViewById(R.id.editTextCode1);
        inputSpaces[1] = findViewById(R.id.editTextCode2);
        inputSpaces[2] = findViewById(R.id.editTextCode3);
        inputSpaces[3] = findViewById(R.id.editTextCode4);
        inputSpaces[4] = findViewById(R.id.editTextCode5);

        numpadButtons = new Button[11];
        numpadButtons[0] = findViewById(R.id.button0);
        numpadButtons[1] = findViewById(R.id.button1);
        numpadButtons[2] = findViewById(R.id.button2);
        numpadButtons[3] = findViewById(R.id.button3);
        numpadButtons[4] = findViewById(R.id.button4);
        numpadButtons[5] = findViewById(R.id.button5);
        numpadButtons[6] = findViewById(R.id.button6);
        numpadButtons[7] = findViewById(R.id.button7);
        numpadButtons[8] = findViewById(R.id.button8);
        numpadButtons[9] = findViewById(R.id.button9);
        numpadButtons[10] = findViewById(R.id.buttonremove);

        submitButton = findViewById(R.id.buttonsubmit);
        requestCodeButton = findViewById(R.id.buttonrequest);

        requestCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a random 5-digit OTP and send it via email
                otp = generateRandomOTP();
                sendEmailVerification(email, Integer.parseInt(otp)); // Use the email passed from the previous screen

                // Show a message indicating that the OTP has been sent
                Toast.makeText(VerificationActivity.this, "OTP has been sent to your email.", Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 0; i < 11; i++) {
            final int finalI = i;
            numpadButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (EditText inputSpace : inputSpaces) {
                        if (inputSpace.getText().toString().isEmpty()) {
                            if (finalI == 10) {
                                String text = inputSpace.getText().toString();
                                if (!text.isEmpty()) {
                                    inputSpace.setText("");
                                }
                            } else {
                                inputSpace.append(String.valueOf(finalI));
                            }
                            break;
                        }
                    }
                }
            });
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the entered code matches the OTP
                StringBuilder enteredCode = new StringBuilder();
                for (EditText inputSpace : inputSpaces) {
                    enteredCode.append(inputSpace.getText().toString());
                }

                if (enteredCode.toString().equals(otp)) {
                    // Code is correct, proceed with user registration
                    // Retrieve the user's email and password from the previous screen
                    String email = getIntent().getStringExtra("email");
                    String password = getIntent().getStringExtra("password");

                    // Create a new user with the provided email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(VerificationActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // User registration was successful
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    // You can add additional user data to Firebase Realtime Database or Firestore here if needed.

                                    Toast.makeText(VerificationActivity.this, "Verification and registration successful", Toast.LENGTH_SHORT).show();

                                    // After successful user registration in VerificationActivity
                                    Intent intent = new Intent(VerificationActivity.this, editprofile.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("contactno", contact);
                                    intent.putExtra("city", city);
                                    intent.putExtra("country", country);
                                    intent.putExtra("userid", userid);
                                    startActivity(intent);

                                } else {
                                    // User registration failed
                                    Toast.makeText(VerificationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Code is incorrect
                    Toast.makeText(VerificationActivity.this, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Generate a random 5-digit OTP
    private String generateRandomOTP() {
        // Generate a random number between 10000 and 99999
        int random = (int) (Math.random() * 90000) + 10000;
        return String.valueOf(random);
    }

    // Send email verification using Firebase Authentication
    // Send email verification using Firebase Authentication
    private void sendEmailVerification(String email, int otp) {
        String subject = "Your OTP for Registration";
        String message = "Your OTP is: " + otp;

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
//This code generates a random 5-digit OTP and sends an email verification request using Firebase Authentication. The email verification functionality is provided by Firebase, and this code utilizes it.






