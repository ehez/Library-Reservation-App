package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryreservationapp.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginActivity extends AppCompatActivity {
    private final int REQUEST_EMAIL = 0;

    private EditText email, password;
    private Button btnLogin;
    private TextView register;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore fStore;
    private boolean isDisabled;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        register = findViewById(R.id.register);

        if (mFirebaseAuth.getCurrentUser() != null){
            checkIfDisabled();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailX = email.getText().toString();
                String pwdX = password.getText().toString();
                int flags = 0;

                if(!isValidEmail(emailX)){
                    email.setError("Not a valid email address");
                    flags++;
                }

                if(pwdX.length() < 6){
                    password.setError("Must be at least 6 characters");
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

                //Try to Login
                if (flags == 0){
                    mFirebaseAuth.signInWithEmailAndPassword(emailX, pwdX).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login error, please try again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //tests to see if the account is disabled before logging in
                                checkIfDisabled();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will return to this activity after Registration is completed
                startActivityForResult(new Intent(LoginActivity.this, RegistrationActivity.class), REQUEST_EMAIL);
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public boolean isValidEmail(String e) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return e.matches(regex);
    }

    public void checkWhichTypeThenCreateInt(){
        String userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(LoginActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.getString("type").equals("Admin")){
                    Toast.makeText(LoginActivity.this, documentSnapshot.getString("type"), Toast.LENGTH_SHORT).show();
                    Intent intToAdminHome = new Intent(LoginActivity.this, AdminHomeActivity.class);
                    startActivity(intToAdminHome);
                    finish();
                }
                else if (documentSnapshot.getString("type").equals("Professor")){
                    Toast.makeText(LoginActivity.this, documentSnapshot.getString("type"), Toast.LENGTH_SHORT).show();
                    Intent intToProfessorHome = new Intent(LoginActivity.this, ProfessorHomeActivity.class);
                    startActivity(intToProfessorHome);
                    finish();
                }
                else if (documentSnapshot.getString("type").equals("Librarian")){
                    Toast.makeText(LoginActivity.this, documentSnapshot.getString("type"), Toast.LENGTH_SHORT).show();
                    Intent intToLibrarianHome = new Intent(LoginActivity.this, LibrarianHomeActivity.class);
                    startActivity(intToLibrarianHome);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, documentSnapshot.getString("type"), Toast.LENGTH_SHORT).show();
                    Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intToHome);
                    finish();
                }
            }
        });

        Common.userID = userID;
    }

    public void checkIfDisabled(){
        String userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document snapshot
                    DocumentSnapshot documentSnapshot = task.getResult();
                    //checks to see if the document exists
                    if(documentSnapshot.exists()){
                        //gets documents value of isDisabled to see if it is disabled
                        isDisabled = documentSnapshot.getBoolean("isDisabled");

                        //checks if the value of isDisabled is true or false
                        if(isDisabled){
                            //gets the reason
                            String reason = documentSnapshot.getString("reason");
                            //gets their name
                            String fName = documentSnapshot.getString("fName");
                            Toast.makeText(getApplicationContext(), fName + ", your account has been disabled", Toast.LENGTH_LONG).show();
                        }
                        else{
                            //checks which role they are to start the correct activity
                            checkWhichTypeThenCreateInt();
                        }
                    }
                    else{
                        Log.d("MYDEBUG","There is no such document");
                    }
                }
                else{
                    Log.d("MYDEBUG", "The document could not be obtained");
                }
            }
        });
    }

}