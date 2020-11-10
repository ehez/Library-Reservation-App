package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Ratings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomsRating extends AppCompatActivity {

    private static final String KEY_REVIEW = "review";
    private static final String KEY_RATING = "rating";
    private static final String KEY_USERID = "type";

    private EditText txtReviewRoom;
    private Button btnSubmitReview;
    private RatingBar ratingBar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_rating);

        // Calling Firebase Database method:
        FirebaseDB();

        // Passing values:
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtReviewRoom = (EditText) findViewById(R.id.txtReviewRoom);
        btnSubmitReview = (Button) findViewById(R.id.btnSubmitReview);

        // Sets number of stars:
        ratingBar.setNumStars(5);
        // Sets default rating to 3.5 as float:
        ratingBar.setRating((float) 3.0);

        // Calls all the actions when button is clicked
        getReviews();

    }// END of onCreate + + + + + + + + + + + + + + + + + + + + + + +

    public void FirebaseDB(){
        //-----------------------------------------------------
        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }// END of FirebaseDB method + + + + + + + + + + + +


    public void getReviews(){
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flags = 0;
                String type = "";
//-----------------------------------------------------------------------------------------------------------------
                // Creates the room review text inputted by user:

                String test_review = txtReviewRoom.getText().toString().trim();
                if(test_review.equals("")) {
                    flags++;
                    txtReviewRoom.setError("Please write a review");
                } else { String review  = txtReviewRoom.getText().toString().trim(); }



//-----------------------------------------------------------------------------------------------------------------
                     // Rating Bar Section :
                // Creates an int to get Rating value:
                int totalNumOfStars = ratingBar.getNumStars();
                // Creates float to get Rating Value:
                float RatedValue = ratingBar.getRating();

                // Toast message displaying ratings out of /5
                Toast.makeText(getApplicationContext(), "Your rating: " + RatedValue + "/" + totalNumOfStars, Toast.LENGTH_SHORT).show();

//-----------------------------------------------------------------------------------------------------------------
                //Creating a collection path: 'Room_Review' into DB:
                String userID = mFirebaseAuth.getCurrentUser().getUid();

                if(flags == 0) {
                    // Creates a hashmap to store reviews
                    Map<String, Object> roomReviews = new HashMap<>();
                    roomReviews.put(KEY_REVIEW , test_review);
                    roomReviews.put(KEY_RATING, RatedValue);
                    roomReviews.put(KEY_USERID, userID);

                    // Adds to the database the new room with the hashmap
                    db.collection("room").document("4IowB8hjjaOUrFBe6CXZ")
                            .collection("reviews").add(roomReviews)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Add was successful!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //-----------------------------------------------------------------------------------------------------------------
                    // Clears section into blank fields after clicked:
                    txtReviewRoom.setText("");
                    //-----------------------------------------------------------------------------------------------------------------
                    // Closes the activity
                    //finish();
                }
//-----------------------------------------------------------------------------------------------------------------
            }
        }); // END of btnSubmitReview   + + + + + + + + + + + + + + + + + + + +
    }// END of getReviews method  + + + + + + + + + + + + + + + + + + + + + + +
} // END of RoomsRating extends AppCompatActivity + + + + + + + + + + + + + + +