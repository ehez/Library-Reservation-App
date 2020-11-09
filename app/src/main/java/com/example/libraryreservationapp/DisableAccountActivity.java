package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DisableAccountActivity extends AppCompatActivity implements DisableAccountDialogFragment.DisableAccountDialogListener{

    //private member variables
    private FirebaseFirestore fStore;
    private Button disableButton;
    private Button updateButton;
    private EditText ramIDEditText;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText reasonEditText;
    private DocumentReference docRef;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_account);

        //gets the references to the layout components
        fStore = FirebaseFirestore.getInstance();
        disableButton = findViewById(R.id.btnDisableAccount);
        updateButton = findViewById(R.id.btnUpdateAccount);
        ramIDEditText = findViewById(R.id.editTextRamID);
        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);

        spinner = findViewById(R.id.spinner_user_role);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();
        String documentID = intent.getStringExtra("docID");
        //creates a reference to a specific document in the collection
        docRef = fStore.collection("users").document(documentID);

        //retrieves the data from firestore for the room that is selected
        getDataFromFirestore();

        //Update Account on click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls the method that updates firestore
                updateDataForUser();
            }
        });

        //Disable Account on click listener
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows the disable confirmation dialog
                showDisableDialog();
            }
        });

    }

    //updates the firestore
    public void updateDataForUser() {
        //gets the role that was selected in the spinner
        String role = spinner.getSelectedItem().toString();

        //creates a hashmap to store all the data for the room
        Map<String, Object> accountInfo = new HashMap<>();
        accountInfo.put("type", role);


        //updates the database for the specific room with the hashmap
        docRef.update(accountInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Update was successful!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //closes the activity
        finish();

    }

    //collects the data from firestore for the specific room
    public void getDataFromFirestore(){
        //gets the document for the specific room that was selected
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document
                    DocumentSnapshot document = task.getResult();

                    //gets the ram id value from database
                    String ramid = document.getString("ram_id").trim();
                    //sets the editText to the value
                    ramIDEditText.setText(ramid);

                    //gets the name values from database
                    String fName = document.getString("fName").trim();
                    String lName = document.getString("lName").trim();
                    //sets the editText to the value
                    nameEditText.setText(fName + " " + lName);

                    //gets the email value from database
                    String email = document.getString("email").trim();
                    //sets the editText to the value
                    emailEditText.setText(email);

                    //gets the role value from the database
                    String role = document.getString("type").trim();
                    //checks to see if role is null
                    if(role != null){
                        //selects the position in the type array that matches the role
                        int spinnerPosition = adapter.getPosition(role);
                        spinner.setSelection(spinnerPosition);
                    }
                }
                else{
                    Log.d("MYDEBUG", "Error getting document values");
                }
            }
        });
    }

    //shows the dialog
    public void showDisableDialog(){
        DialogFragment dialog = new DisableAccountDialogFragment();
        dialog.show(getSupportFragmentManager(), "DisableAccountDialogFragment");
    }

    //if the user clicked the positive dialog button
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String reason){

        //creates a hashmap with the data for the update
        Map<String, Object> update = new HashMap<>();
        update.put("isDisabled", true);
        update.put("reason", reason);

        //updates the account in the database
        docRef.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "The account was disabled", Toast.LENGTH_SHORT).show();
                    //closes the activity
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Update to disable failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //if the user clicked the negative dialog button
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        //closes the dialog
        dialog.dismiss();
    }

}
