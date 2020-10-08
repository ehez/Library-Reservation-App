package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {
    Button btnLogout;
    Button btnAddRoom;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnLogout = findViewById(R.id.logout);
        btnAddRoom = findViewById(R.id.btnGoToAddRoom);

        btnLogout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                //Erick
            }
        }));

        // Add Room On Click Listener
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starts AddRoomActivity if the button is clicked
                Intent intToAddRoom = new Intent(AdminHomeActivity.this, AddRoomActivity.class);
                startActivity(intToAddRoom);
            }
        });
    }
}