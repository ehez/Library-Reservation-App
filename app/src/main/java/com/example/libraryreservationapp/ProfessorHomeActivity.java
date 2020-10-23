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
import com.google.firebase.firestore.auth.EmptyCredentialsProvider;
import com.google.protobuf.Empty;

import java.util.HashMap;
import java.util.Map;

public class ProfessorHomeActivity extends AppCompatActivity {

    // Created Constants to add the values to the database
    //private static final String TAG = "ProfessorHomeActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COURSE = "course";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_ISBN  = "isbn";
    private static final String KEY_STATUS  = "status";



    // Declared Variables
    EditText booksName, className, txtIsbn, booksQuantity;
    Button btnLogout, btnRequest, btnClear, btnOrders;

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
        btnOrders = findViewById(R.id.btnOrders);
        btnClear = findViewById(R.id.btnClear);
        booksName = findViewById(R.id.booksName);
        className = findViewById(R.id.className);
        txtIsbn =  findViewById(R.id.txtIsbn);
        booksQuantity =  findViewById(R.id.booksQuantity);
        // END of Assigning the xml Variables
        //-------------------------------------------------------
        //
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Request the Books
        //
        //////////////////////////////////////////////////////////////
        btnRequest.setOnClickListener((new View.OnClickListener() { //
            //////////////////////////////////////////////////////////////
            @Override
            public void onClick(View view)
            {
                int flags = 0;
                String title, course, isbn, quantity, status;

                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                //
                // If/Else statements to Check for Errors
                //
                // Checking Book's Name - - - - - - - - - - - - - - - - - -
                String test_title = booksName.getText().toString().trim();
                if(test_title.equals("")) {
                    flags++;
                    booksName.setError("Please enter a book title");
                } else { title = booksName.getText().toString().trim(); }


                // Checking Class Name - - - - - - - - - - - - - - - - - -
                String test_course = className.getText().toString().trim();
                if(test_course.equals("")){
                    flags++;
                    className.setError("Please enter a Class Name");
                } else { course = className.getText().toString().trim(); }


                // Checking isbn - - - - - - - - - - - - - - - - - -
                String test_isbn = txtIsbn.getText().toString().trim();
                if(test_isbn.equals("")){
                    if (txtIsbn.length() < 0 || txtIsbn.length() >13){
                        flags++;
                        txtIsbn.setError("Please enter the 13 isbn number"); }
                } else{ isbn = txtIsbn.getText().toString().trim(); }


                // Checking quantity - - - - - - - - - - - - - - - - - -
                String test_quantity = booksQuantity.getText().toString().trim();
                if(test_quantity.equals("")){
                    flags++;
                    booksQuantity.setError("Please enter a quantity ");
                } else {
                    quantity = booksQuantity.getText().toString().trim();

                }

                //
                //
                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +  + + + + + + + + + + + + + + + + + + + + +
                //
                //
                // bookRequest add into DB starts here:
                if(flags == 0)
                {   //  Keys  | Value | Reference    |    Implementation
                    Map<String, Object> bookRequest = new HashMap<>();
                    bookRequest.put(KEY_TITLE, test_title);
                    bookRequest.put(KEY_COURSE, test_course );
                    bookRequest.put(KEY_QUANTITY, test_quantity);
                    bookRequest.put(KEY_ISBN, test_isbn);
                    bookRequest.put(KEY_STATUS, " "); // --> CREATES AN EMPTY FIELD SO LIBRARIAN CAN BIND DATA
                 //                                      --> (APPROVED / DENIED)
                //
                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                // ---------------------------------------------------------------------------------------------------
                //
                //
                //Creating a collection path: 'requests' into DB
                    fStore.collection("requests").add(bookRequest)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),
                                                "Books were requested successful!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Books failed on request!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //--------------------------------------------------
                    // Clear all the fields after submit a book:
                    booksName.setText("");
                    className.setText("");
                    txtIsbn.setText("");
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
                txtIsbn.setText("");
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
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Check Orders
        //
        //////////////////////////////////////////////////////////////

        btnOrders.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Create an Intent to next page check orders
                Intent intToProfCheckOrders = new Intent(ProfessorHomeActivity.this, ProfCheckOrders.class);
                startActivity(intToProfCheckOrders);
            }
        }));// END OF Check Orders


    }
}