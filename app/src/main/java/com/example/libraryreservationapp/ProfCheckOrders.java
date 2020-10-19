package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfCheckOrders extends AppCompatActivity
{
    // Created Constants to add the values to the database
    private static final String TAG = "ProfessorHomeActivity";
    private static final String KEY_REQUESTS = "requests";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COURSE = "course";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_ISBN = "isbn";
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    //-----------------------------------------------------
    //Firebase db
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;

    //-----------------------------------------------------
    // Variables Declared
    Button btnLogout, btnGoBack;


    //-----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_check_orders);
        //-----------------------------------------------------
        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //-----------------------------------------------------
        // Calls the RecyclerView method
        MakeRecyclerView();
        //-----------------------------------------------------
        // Assigning the XML Variables
        btnLogout = findViewById(R.id.logout);
        btnGoBack = findViewById(R.id.btnGoBack);
//-------------------------------------------------------------------------------------------------------------------------------------------------------
        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ BUTTONS' CONFIGURATIONS ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
//-------------------------------------------------------------------------------------------------------------------------------------------------------


        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //                                                           |
        // Button Configuration to go back to main menu              |
        //                                                           |
        //////////////////////////////////////////////////////////////
        btnGoBack.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intGoBaCK = new Intent(ProfCheckOrders.this, ProfessorHomeActivity.class);
                startActivity(intGoBaCK);
            }
        }));
//-------------------------------------------------------------------------------------------------------------------------------------------------------
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //                                                           |
        // Button Configuration to Logout                            |
        //                                                           |
        //////////////////////////////////////////////////////////////
        btnLogout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(ProfCheckOrders.this, LoginActivity.class);
                startActivity(intToLogin);
            }
        }));

    }// END of onCreate  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void MakeRecyclerView() {
        // Creates a query that uses the collection reference to get the courses in ascending order
        Query query = fStore.collection(KEY_REQUESTS)
                .orderBy(KEY_COURSE, Query.Direction.ASCENDING)
                .orderBy(KEY_TITLE, Query.Direction.ASCENDING)
                .orderBy(KEY_QUANTITY, Query.Direction.ASCENDING)
                .orderBy(KEY_ISBN, Query.Direction.ASCENDING);;

        // Creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<Orders> options = new FirestoreRecyclerOptions.Builder<Orders>().setQuery(query, Orders.class).setLifecycleOwner(this).build();
        //-----------------------------------------------------
        // Create the adapter with corresponding parameter
        adapter = new OrdersAdapter(options);
        //-----------------------------------------------------
        // Gets the recyclerView id for reference
        recyclerView = findViewById(R.id.recyclerViewBooks);
        //-----------------------------------------------------
        // Sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //-----------------------------------------------------
        //Adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        //-----------------------------------------------------
        // Sets the adapter
        recyclerView.setAdapter(adapter);
    }// END of MakeRecycleView - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}// END OF ProfCheckOrders extends AppCompatActivity - - - - - - - - - - - - - - - - - - - - - - - -




