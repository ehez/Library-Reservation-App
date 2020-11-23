package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Map;

public class ViewReviews extends AppCompatActivity {

    // .:::: Static and global variables declared (NOT USING THEM)::::.
    private static final String TAG = "ViewReviews";
    private static final String KEY_REVIEW = "review";
    private static final String KEY_RATING = "rating";
    private static final String EMAIL = "email";

//  .::::: Variables :::::.
    private TextView textViewData;
    private FirebaseAuth mFirebaseAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference notebookRef = db.collection("room/7lVHHvffUwU4PWgyhuB1/reviews");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

//      .::::: get authentication from database :::::.
        mFirebaseAuth = FirebaseAuth.getInstance();
//      .::::: Creates a reference to a specific document in the collection :::::.
        textViewData = findViewById(R.id.text_view_data);
//      .::::: Call loadNotes method :::::.
        loadNotes();
    }// + + + + + END of onCreate + + + + +

    private void loadNotes(){

//              .::::: get information from database :::::.
        notebookRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {

//                 .::::: Create Object from RoomReviews.java file :::::.
                    RoomReviews note = documentSnapshot.toObject(RoomReviews.class);
                    float rating = note.getRating();
                    String review = note.getReview();
                    String email = note.getEmail();
                    data += "Rating: " + rating + " Stars"
                            + "\nE-mail: " + email + "\nReview: " + review + "..." + "\n\n";
                }

//              .::::: Display the results into Nested Scroll View :::::.
                textViewData.setText(data);
            }
        });
    }// + + + + + END of loadNotes method() + + + + +
}// + + + + + END of ViewReviews extends AppCompatActivity + + + + +










