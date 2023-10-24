package com.ass2.i200581;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

public class editprofile extends AppCompatActivity {

    private Spinner spinnerCountry;
    private Spinner spinnerCity;
    private String selectedCountry;
    private boolean isCountrySelected = false;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextContact;
    private Button saveButton;
    private ImageView profileImageView;
    private ImageView coverImageView;
    private Uri selectedProfileImageUri;
    private Uri selectedCoverImageUri;
    private static final int PROFILE_IMAGE_REQUEST_CODE = 1;
    private static final int COVER_IMAGE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://smda2-51583.appspot.com");
        StorageReference storageRef = storage.getReference();

        // Get a reference to your Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        profileImageView = findViewById(R.id.profileImageView);
        coverImageView = findViewById(R.id.coverImageView);

        // Set an OnClickListener for the "Upload Profile Image" button
        findViewById(R.id.profileimageuploadphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(PROFILE_IMAGE_REQUEST_CODE);
            }
        });

        // Set an OnClickListener for the "Upload Cover Image" button
        findViewById(R.id.coverimageuploadphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(COVER_IMAGE_REQUEST_CODE);
            }
        });

        // Find the Spinners by their IDs
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerCity = findViewById(R.id.spinnerCity);

        // Find EditText fields for name, email, and contact
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextContact = findViewById(R.id.editTextContact);

        // Find the "Save Changes" button by its ID
        saveButton = findViewById(R.id.buttonsavechanges);

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

        // Set an OnClickListener for the "Save Changes" button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the UI fields
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String contact = editTextContact.getText().toString();
                String country = spinnerCountry.getSelectedItem().toString();
                String city = spinnerCity.getSelectedItem().toString();

                // Check if all fields are filled (you can add more validation if needed)
                if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || country.isEmpty() || city.isEmpty()) {
                    // Show an error message if any field is empty
                    // You can implement a custom error message or dialog here
                    // For simplicity, let's just toast a message for now
                    Toast.makeText(editprofile.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if both profile and cover images are selected
                if (selectedProfileImageUri != null && selectedCoverImageUri != null) {
                    // Create references for profile and cover images
                    StorageReference profileImageRef = storageRef.child("profile_images/" + contact + ".jpg");
                    StorageReference coverImageRef = storageRef.child("cover_images/" + contact + ".jpg");

                    // Upload profile image
                    profileImageRef.putFile(selectedProfileImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get the download URL of the uploaded profile image
                                    profileImageRef.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri profileImageUri) {
                                                    // Store the profile image URL in the database
                                                    String profileImageURL = profileImageUri.toString();

                                                    // Upload cover image
                                                    coverImageRef.putFile(selectedCoverImageUri)
                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    // Get the download URL of the uploaded cover image
                                                                    coverImageRef.getDownloadUrl()
                                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri coverImageUri) {
                                                                                    // Store the cover image URL in the database
                                                                                    String coverImageURL = coverImageUri.toString();

                                                                                    // Create a User object with the data
                                                                                    User user = new User(name, email, contact, country, city, profileImageURL, coverImageURL);

                                                                                    // Save the user data to the database using the contact number as the key
                                                                                    usersRef.child(contact).setValue(user);

                                                                                    // Show a success message using a toast
                                                                                    Toast.makeText(editprofile.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();

                                                                                    // Navigate to MainActivity
                                                                                    Intent intent = new Intent(editprofile.this, MainActivity.class);
                                                                                    startActivity(intent);
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                } else {
                    // Show an error message if images are not selected
                    Toast.makeText(editprofile.this, "Please select both profile and cover images.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get data from the verification screen using Intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String contact = intent.getStringExtra("contactno");
            String country = intent.getStringExtra("country");

            // Set the retrieved data in the EditText fields and the country Spinner
            if (name != null) {
                editTextName.setText(name);
            }
            if (email != null) {
                editTextEmail.setText(email);
            }
            if (contact != null) {
                editTextContact.setText(contact);
            }
            if (country != null) {
                int countryPosition = countryAdapter.getPosition(country);
                if (countryPosition >= 0) {
                    spinnerCountry.setSelection(countryPosition);
                }
            }
        }
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PROFILE_IMAGE_REQUEST_CODE) {
                selectedProfileImageUri = data.getData();
                profileImageView.setImageURI(selectedProfileImageUri);
            } else if (requestCode == COVER_IMAGE_REQUEST_CODE) {
                selectedCoverImageUri = data.getData();
                coverImageView.setImageURI(selectedCoverImageUri);
            }
        }
    }

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
}
