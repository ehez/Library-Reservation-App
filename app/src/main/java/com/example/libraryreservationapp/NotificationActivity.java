package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationActivity extends AppCompatActivity {

   private TextView notification;
   private EditText booksNameNotification;
   private EditText classNameNotification;
   private FirebaseFirestore fStore;
   private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notification = findViewById(R.id.text_view_notification);
        String message = getIntent().getStringExtra("message");
        notification.setText(message);
        booksNameNotification = findViewById(R.id.booksNameNotification);
        classNameNotification = findViewById(R.id.classNameNotification);
/*
        // gets firestore instance
        fStore = FirebaseFirestore.getInstance();

        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();


        String documentID = intent.getStringExtra("docID");

        //creates a reference to a specific document in the collection
        docRef = fStore.collection("requests").document(documentID);

        //retrieves the data from firestore for the request that is selected
        getDataFromFirestore();
*/
    }
    //Pulls  data from firestore to display information about the course
/*    public void getDataFromFirestore()
    {

        //gets the document for the specific book request
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //gets the document
                    DocumentSnapshot document = task.getResult();
                    // gets the title of the book requested
                    String title = document.getString("title");
                    // sets the title to its appropriate editText
                    booksNameNotification.setText(title);
                    // gets class name of the book requested
                    String classname = document.getString("course");
                    // sets the class name to its appropriate editText
                    classNameNotification.setText(classname);

                } else {
                    Log.d("MYDEBUG", "Error getting document values");
                }
            }
        });
    }

*/
}