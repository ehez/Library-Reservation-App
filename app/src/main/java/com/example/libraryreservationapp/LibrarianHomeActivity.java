package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LibrarianHomeActivity extends AppCompatActivity
{
    private Button btnLogout;
    private Button btnAddBook;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_home);

        btnLogout = findViewById(R.id.logout);
        btnAddBook = findViewById(R.id.btnGoToAddBook);

        btnLogout.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(LibrarianHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);

            }
        }));

        // Add Book On Click Listener
        btnAddBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Starts AddBookActivity if the button is clicked
                Intent intToAddBook = new Intent(LibrarianHomeActivity.this, AddBookActivity.class);
                startActivity(intToAddBook);
            }
        });
    }
}