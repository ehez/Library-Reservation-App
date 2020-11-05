package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout, btnReserveRoom, btnRoomRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.logout);
        btnReserveRoom = findViewById(R.id.reserveRoom);
        btnRoomRate = findViewById(R.id.btnRoomRate);

        btnLogout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                finish();
            }
        }));

        btnReserveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToReserveRoom = new Intent(HomeActivity.this, ReserveRoomActivity.class);
                startActivity(intToReserveRoom);
            }
        });
        btnRoomRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToRoomRate = new Intent(HomeActivity.this, RoomsRating.class);
                startActivity(intToRoomRate);
            }
        });
    }
}