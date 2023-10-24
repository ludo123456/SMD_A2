package com.ass2.i200581;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class chathome extends AppCompatActivity {

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chathome);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        // Find the TextViews for user names and contact numbers
        TextView user1Name = findViewById(R.id.user1name);
        final TextView user2Name = findViewById(R.id.user2name);
        final TextView user3Name = findViewById(R.id.user3name);

        final TextView user2ContactNo = findViewById(R.id.user2ContactNo);
        final TextView user3ContactNo = findViewById(R.id.user3ContactNo);

        // Set click listeners for the user names
        user1Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open chat for user 1
                openChatWithUser2(user1Name.getText().toString());
            }
        });

        user2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open chat for user 1
                openChatWithUser2(user2Name.getText().toString());
            }
        });

        user3Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open chat for user 1
                openChatWithUser2(user3Name.getText().toString());
            }
        });

        // Randomly select two contact numbers
        getRandomContactNumbers(user2Name, user2ContactNo, user3Name, user3ContactNo);
    }

    // Function to retrieve and populate user data for the second and third users with random contact numbers
    private void getRandomContactNumbers(final TextView user2Name, final TextView user2ContactNo, final TextView user3Name, final TextView user3ContactNo) {
        // Retrieve a list of contact numbers from the database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> contactNumbers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming contact numbers are stored under child nodes
                    String contactNumber = snapshot.child("contact").getValue(String.class);
                    if (contactNumber != null) {
                        contactNumbers.add(contactNumber);
                    }
                }

                // Randomly select two contact numbers
                if (contactNumbers.size() >= 2) {
                    Collections.shuffle(contactNumbers);
                    String randomContactNumber1 = contactNumbers.get(0);
                    String randomContactNumber2 = contactNumbers.get(1);

                    // Now, retrieve the names associated with the contact numbers
                    retrieveNameForContactNumber(user2Name, randomContactNumber1);
                    retrieveNameForContactNumber(user3Name, randomContactNumber2);

                    // Set the randomly selected contact numbers
                    user2ContactNo.setText(randomContactNumber1);
                    user3ContactNo.setText(randomContactNumber2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    // Function to retrieve the name associated with a contact number
    private void retrieveNameForContactNumber(final TextView nameTextView, final String contactNumber) {
        usersRef.orderByChild("contact").equalTo(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        String name = user.getName();
                        nameTextView.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void openChatWithUser(String userName, String Contact) {
        // Create an Intent to open the chat with the selected user
        Intent chatIntent = new Intent(chathome.this, ChatActivity.class);

        // Pass the user's name to the chat activity
        chatIntent.putExtra("userName", userName);
        chatIntent.putExtra("contact", Contact);

        // Start the chat activity
        startActivity(chatIntent);
    }

    private void openChatWithUser2(String userName) {
        // Create an Intent to open the chat with the selected user
        Intent chatIntent = new Intent(chathome.this, ChatActivity.class);

        // Pass the user's name to the chat activity
        chatIntent.putExtra("userName", userName);

        // Start the chat activity
        startActivity(chatIntent);
    }
}
