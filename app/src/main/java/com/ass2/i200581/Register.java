package com.ass2.i200581;

import android.content.Intent;
import java.util.Random;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

public class Register extends AppCompatActivity {
    private Spinner spinnerCountry;
    private Spinner spinnerCity;
    private String selectedCountry;
    private boolean isCountrySelected = false;

    private EditText editTextName, editTextEmailRegister, editTextContact, editTextpassRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Find the Spinners by their IDs
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerCity = findViewById(R.id.spinnerCity);

        // Find the EditText fields by their IDs
        editTextName = findViewById(R.id.editTextName);
        editTextEmailRegister = findViewById(R.id.editTextEmailRegister);
        editTextContact = findViewById(R.id.editTextContact);
        editTextpassRegister = findViewById(R.id.editTextpassRegister);

        // Create an ArrayAdapter for the country Spinner
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                this, R.array.countries_array, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        // Set a listener for the country Spinner to enable/disable the city Spinner
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCountry = parentView.getItemAtPosition(position).toString();
                if (selectedCountry.equals("Select a country")) {
                    isCountrySelected = false;
                    spinnerCity.setEnabled(false);
                } else {
                    isCountrySelected = true;
                    spinnerCity.setEnabled(true);
                    updateCitySpinner(selectedCountry);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Create an ArrayAdapter for the city Spinner (initially empty)
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        // Find the "Login" button by its ID
        View buttonLoginRegister = findViewById(R.id.buttonLoginRegister);

        // Set an OnClickListener for the "Login" button
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate back to the MainActivity
                Intent intent = new Intent(Register.this, MainActivity.class);

                // Start the MainActivity
                startActivity(intent);
            }
        });

        View buttonSignUpRegister = findViewById(R.id.buttonSignUpRegister);
        // Set an OnClickListener for the "Sign Up" button
        buttonSignUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the registration method
                registerUser();
            }
        });
    }

    // Method to update the city Spinner based on the selected country
    private void updateCitySpinner(String country) {
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Populate the cityAdapter based on the selected country
        if (country.equals("Pakistan")) {
            cityAdapter.addAll("Karachi", "Lahore", "Islamabad");
        } else if (country.equals("India")) {
            cityAdapter.addAll("Delhi", "Mumbai", "Bangalore");
        } else if (country.equals("UAE")) {
            cityAdapter.addAll("Dubai", "Abu Dhabi", "Sharjah");
        }

        spinnerCity.setAdapter(cityAdapter);
    }

    // Method to handle user registration
    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmailRegister.getText().toString().trim();
        final String contact = editTextContact.getText().toString().trim();
        final String Pass = editTextpassRegister.getText().toString().trim();
        final String city = spinnerCity.getSelectedItem().toString();

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || Pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userid = generateRandomUserId();


        // Pass the user information to the EmailVerificationActivity
        Intent intent = new Intent(Register.this, VerificationActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("contactno", contact);
        intent.putExtra("password", Pass);
        intent.putExtra("city", city);
        intent.putExtra("country", selectedCountry);
        intent.putExtra("userId", userid); // Add the user ID to the intent
        startActivity(intent);
    }

    private String generateRandomUserId() {
        Random random = new Random();
        int userid = 10000 + random.nextInt(90000); // Random number between 10000 and 99999
        return String.valueOf(userid);
    }
}
