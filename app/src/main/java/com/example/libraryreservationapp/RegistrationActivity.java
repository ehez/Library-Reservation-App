package com.example.libraryreservationapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    EditText firstName, lastName, ramId, email, password;
    Button btnRegister;
    Spinner spinner;

    //Firebase db
    FirebaseAuth mFirebaseAuth;
    //Firestore db
    FirebaseFirestore fStore;
    //Firestore user id
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Get Firestore Instance
        fStore = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        ramId = findViewById(R.id.ramId);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);


        spinner = findViewById(R.id.spinner_user_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstX = firstName.getText().toString();
                final String lastX = lastName.getText().toString();
                final String ramX = ramId.getText().toString();
                final String emailX = email.getText().toString();
                final String typeX = spinner.getSelectedItem().toString();
                String pwdX = password.getText().toString();
                int flags = 0;

                if(firstX.length() < 3 || firstX.length() > 30){
                    firstName.setError("Must be between 3-30 characters");
                    flags++;
                }

                if(lastX.length() < 3 || lastX.length() > 30){
                    lastName.setError("Must be between 3-30 characters");
                    flags++;
                }

                if(!isValidEmail(emailX)){
                    email.setError("Not a valid email address");
                    flags++;
                }

                if(!isValidRam(ramX)){
                    ramId.setError("Format must be: R0#######");
                    flags++;
                }

                if(pwdX.length() < 6){
                    password.setError("Must be at least 6 characters");
                    flags++;
                }

                if(firstX.isEmpty()){
                    firstName.setError("Please enter first name");
                    flags++;
                }
                if (lastX.isEmpty()){
                    lastName.setError("Please enter last name");
                    flags++;
                }
                if (ramX.isEmpty()){
                    ramId.setError("Please enter Ram ID");
                    flags++;
                }
                if (emailX.isEmpty()){
                    email.setError("Please enter an email");
                    flags++;
                }
                if (pwdX.isEmpty()){
                    password.setError("Please enter a password");
                    flags++;
                }

                if (flags == 0){
                    //Register user in Firebase
                    mFirebaseAuth.createUserWithEmailAndPassword(emailX, pwdX).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(RegistrationActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "User Created!", Toast.LENGTH_SHORT).show();

                                //Add user information to Firestore
                                userID = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("fName", firstX);
                                user.put("lName", lastX);
                                user.put("ram_id", ramX);
                                user.put("email", emailX);
                                user.put("type", typeX);

                                user.put("isDisabled", false);
                                user.put("reason", "");

                                documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onFailure" + e.toString());
                                    }
                                });
                                // Return to Login after Successfully being registered
                                finish();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isValidEmail(String e) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return e.matches(regex);
    }

    public boolean isValidRam(String r) {
        String regex = "^R0\\d{7}$";
        return r.matches(regex);
    }
}