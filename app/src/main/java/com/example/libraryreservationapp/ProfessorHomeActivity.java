package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfessorHomeActivity extends AppCompatActivity {
    // Declared Variables
     EditText booksName, className, isbn, booksQuantity;
     Button btnLogout, btnRequest, btnClear;

    //Firebase db
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home);

        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //------------------------------------------------------
        //
        // Assigning the xml Variables
        btnLogout = findViewById(R.id.logout);
        btnRequest =  findViewById(R.id.btnRequest);
        btnClear = findViewById(R.id.btnClear);
        booksName = findViewById(R.id.booksName);
        className = findViewById(R.id.className);
        isbn =  findViewById(R.id.isbn);
        booksQuantity =  findViewById(R.id.booksQuantity);
        // END of Assigning the xml Variables
        //-------------------------------------------------------
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Request the Books
        //
        //////////////////////////////////////////////////////////////
        btnRequest.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                int flags = 0;

                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                //
                // If/Else statements to Check for Errors
                //
                // Checking Book's Name - - - - - - - - - - - - - - - - - -
                 String books_Name = booksName.getText().toString().trim();
                if(books_Name.equals("")) {
                    flags++;
                    booksName.setError("Please enter a book title");
                } else { books_Name  = booksName.getText().toString().trim(); }


                // Checking Class Name - - - - - - - - - - - - - - - - - -
                String class_Name = className.getText().toString().trim();
                if(class_Name.equals("")){
                    flags++;
                    className.setError("Please enter a Class Name");
                } else { class_Name = className.getText().toString().trim(); }


                // Checking isbn - - - - - - - - - - - - - - - - - -
                String isbnX = isbn.getText().toString().trim();
                if(isbnX.equals("")){
                    if (isbn.length() < 0 || isbn.length() >13){
                        flags++;
                        isbn.setError("Please enter the 13 isbn number"); }
                } else{ isbnX = isbn.getText().toString().trim(); }


                // Checking quantity - - - - - - - - - - - - - - - - - -
                String books_Quantity = booksQuantity.getText().toString().trim();
                if(books_Quantity.equals("")){
                    flags++;
                    booksQuantity.setError("Please enter a quantity ");
                } else { books_Quantity = booksQuantity.getText().toString().trim(); }
                //
                //
                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                //
                //
                // bookRequest add into DB starts here:
                if(flags == 0){
                    Map<String, Object> bookRequest = new HashMap<>();
                    bookRequest.put("Book's Name", books_Name);
                    bookRequest.put("Class Name", class_Name );
                    bookRequest.put("Quantity", books_Quantity);
                    bookRequest.put("isbn", isbnX);

                    //Creating a collection path: 'requests' into DB
                    fStore.collection("requests").add(bookRequest).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){ Toast.makeText(getApplicationContext(), "Books were requested successful!", Toast.LENGTH_SHORT).show();}
                            else{Toast.makeText(getApplicationContext(), "Books failed on request!", Toast.LENGTH_SHORT).show();}
                        }
                    });

                    //--------------------------------------------------
                    // Clear all the fields after submit a book:
                        booksName.setText("");
                        className.setText("");
                        isbn.setText("");
                        booksQuantity.setText("");
                    //
                    //----------------------------------------------------------------------------
                    // finish(); -> In case you  want to logout right after requesting the book  |
                    //----------------------------------------------------------------------------

                }// END of bookRequest into DB
            }
        }));// END OF btnRequest
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Clear the Fields
        //
        //////////////////////////////////////////////////////////////
        btnClear.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booksName.setText("");
                className.setText("");
                isbn.setText("");
                booksQuantity.setText("");
            }
        }));// END OF btnCLear

        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Logout
        //
        //////////////////////////////////////////////////////////////
        btnLogout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(ProfessorHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin); }
        }));
    }
}