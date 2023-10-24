package com.ass2.i200581;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class postitem extends AppCompatActivity {

    private Spinner citySpinner;
    private TextInputEditText itemNameEditText;
    private TextInputEditText hourlyRateEditText;
    private TextInputEditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postitem); // Replace with your XML layout file name

        // Initialize the Spinner
        citySpinner = findViewById(R.id.spinnerCity);

        // Initialize the TextInputEditText fields
        itemNameEditText = findViewById(R.id.editTextItemName);
        hourlyRateEditText = findViewById(R.id.editTextHourlyRate);
        descriptionEditText = findViewById(R.id.editTextDescription);

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.city_array, // This should be a string array resource containing your cities
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter on the Spinner
        citySpinner.setAdapter(adapter);

        // Set a listener to handle item selection in the Spinner
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected city here
                String selectedCity = parent.getItemAtPosition(position).toString();
                // You can perform actions based on the selected city, e.g., save it to a variable
                Toast.makeText(postitem.this, "Selected City: " + selectedCity, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected (optional)
            }
        });
    }
}
