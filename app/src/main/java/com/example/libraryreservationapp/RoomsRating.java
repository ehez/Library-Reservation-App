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

    private static final String KEY_BUILDING = "building";
    private static final String KEY_TXTREVIEW = "review";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_RATING = "rating";



    private RadioGroup radioGroupBuildings;
    private EditText txtReviewRoom;
    private EditText txtEmail;
    private Button btnSubmitReview;
    private RadioButton radioWhitman;
    private Ratings ratings;
    private RatingBar ratingBar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_rating);

        // /Calling Firebase Database method:
        FirebaseDB();

        // Passing values:
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        radioGroupBuildings = (RadioGroup) findViewById(R.id.radioGroupBuildings);
        radioWhitman = (RadioButton) findViewById(R.id.radioWhitman);
        txtReviewRoom = (EditText) findViewById(R.id.txtReviewRoom);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSubmitReview = (Button) findViewById(R.id.btnSubmitReview);
        ratings = new Ratings();

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
                String building = "";

                // Clears the errors to ensure there is no false error
                radioWhitman.setError(null);
                // Selecting options from Radio Group Buttons
                int selectBuilding = radioGroupBuildings.getCheckedRadioButtonId();
                if(selectBuilding == -1){
                    flags++;
                    // Error message if not selected
                    radioWhitman.setError("Please select a building");
                }else{
                    switch(selectBuilding)
                    {
                        case R.id.radioBusiness:
                            building = "Business School";
                            break;
                        case R.id.radionCampus:
                            building = "Campus Center";
                            break;
                        case R.id.radioConklin:
                            building = "Conklin Hall";
                            break;
                        case R.id.radioGleeson:
                            building = "Gleenson Hall";
                            break;
                        case R.id.radioGreenley:
                            building = "Greenley Library";
                            break;
                        case R.id.radioHale:
                            building = "Hale Hall";
                            break;
                        case R.id.radioLupton:
                            building = "Lupton Hall";
                            break;
                        case  R.id.radionSinclair:
                            building = "Sinclair Hall";
                            break;
                        case R.id.radioThompson:
                            building = "Thompson Hall";
                            break;
                        case R.id.radioWhitman:
                            building = "Whitman Hall";
                            break;
                    }// End of SWITCH statement
                }// End of ELSE statement

//-----------------------------------------------------------------------------------------------------------------
                // Creates the room review text inputed by user:

                String test_review = txtReviewRoom.getText().toString().trim();
                if(test_review.equals("")) {
                    flags++;
                    txtReviewRoom.setError("Please write a review");
                } else { String review  = txtReviewRoom.getText().toString().trim(); }

                String test_email = txtEmail.getText().toString().trim();
                if(test_email.equals("")) {
                    flags++;
                    txtEmail.setError("Please write a review");
                } else { String email  = txtEmail.getText().toString().trim(); }

//-----------------------------------------------------------------------------------------------------------------
                // Creates the Rating Bar stars:


//-----------------------------------------------------------------------------------------------------------------
                //Creating a collection path: 'Room_Review' into DB:


                if(flags == 0) {
                    // Creates a hashmap to store reviews
                    Map<String, Object> roomReviews = new HashMap<>();
                    roomReviews.put(KEY_BUILDING, building);
                    roomReviews.put(KEY_TXTREVIEW, test_review);
                    roomReviews.put(KEY_EMAIL, test_email);
                   // roomReviews.put(KEY_RATING, rateValue);

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

                    // Closes the activity
                    finish();
                }
//-----------------------------------------------------------------------------------------------------------------
            }
        }); // END of btnSubmitReview   + + + + + + + + + + + + + + + + + + + +
    }// END of getReviews method  + + + + + + + + + + + + + + + + + + + + + + +
} // END of RoomsRating extends AppCompatActivity + + + + + + + + + + + + + + +