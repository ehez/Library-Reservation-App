package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    //private member variables
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    Button btnLogout, btnReserveRoom, btnRoomRate;
    private RoomReservationAdapter adapter;
    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbarStudent);
        btnLogout = findViewById(R.id.logout);
        btnReserveRoom = findViewById(R.id.reserveRoom);
        btnRoomRate = findViewById(R.id.btnRoomRate);

        //gets instance of firestore
        fStore = FirebaseFirestore.getInstance();
        //gets the instance of the firebase auth
        auth = FirebaseAuth.getInstance();
        //gets the logged in users user id
        userID = auth.getUid();

        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);

        fStore.collection("users").document(userID).collection("currentReservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(getApplicationContext(), "You don't have any reservations yet", Toast.LENGTH_LONG).show();
                }
                else{
                    //calls the recycler view for it to be set up
                    setUpRecyclerView();
                }
            }
        });



        btnLogout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                finish();
            }
        }));

        btnReserveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToReserveRoom = new Intent(HomeActivity.this, ReserveRoomActivity.class);
                startActivity(intToReserveRoom);
            }
        });
        btnRoomRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToRoomRate = new Intent(HomeActivity.this, RoomsRating.class);
                startActivity(intToRoomRate);
            }
        });


    }

    private void setUpRecyclerView() {
        // creates a query that uses the collection reference to get the current reservations
        Query query = fStore.collection("users").document(userID).collection("currentReservations").orderBy("date", Query.Direction.ASCENDING);

        // creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<RoomReservationInformation> options = new FirestoreRecyclerOptions.Builder<RoomReservationInformation>().setQuery(query, RoomReservationInformation.class).setLifecycleOwner(this).build();

        // sets the adapter with the configurations that were just made
        adapter = new RoomReservationAdapter(options);

        // gets the recyclerView id for reference
        recyclerView = findViewById(R.id.recyclerViewReservations);
        // sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter
        recyclerView.setAdapter(adapter);
    }

    //inflates the menu and toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    //selects the proper idea when an item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //converts the selected menu item to do the proper activity
        switch(item.getItemId()){
            case R.id.menuItemAdminAddRoom:
                //Starts AddRoomActivity if the button is clicked
                Intent intToAddRoom = new Intent(HomeActivity.this, AddRoomActivity.class);
                startActivity(intToAddRoom);
                return true;
            case R.id.menuItemAdminLogout:
                //signs out user
                FirebaseAuth.getInstance().signOut();
                //Starts LoginActivity if this button is clicked
                Intent intToLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}