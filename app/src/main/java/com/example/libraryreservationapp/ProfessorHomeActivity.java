package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.EmptyCredentialsProvider;
import com.google.protobuf.Empty;

import java.util.HashMap;
import java.util.Map;

public class ProfessorHomeActivity extends AppCompatActivity
{
    // Created Constants to add the values to the database
    private static final String TAG = "ProfessorHomeActivity";
    private static final String KEY_REQUESTS = "requests";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COURSE = "course";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_STATUS = "status";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    //-----------------------------------------------------
    //Firebase db
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;

    //-----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home);
        //-----------------------------------------------------
        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Assigning the XML Variables
        toolbar = findViewById(R.id.toolbarProfessor);

        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);

        // Calls the RecyclerView method
        MakeRecyclerView();

    }// END of onCreate  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void MakeRecyclerView() {
        // Creates a query that uses the collection reference to get the courses in ascending order
        Query query = fStore.collection(KEY_REQUESTS)
                .orderBy(KEY_COURSE, Query.Direction.ASCENDING)
                .orderBy(KEY_TITLE, Query.Direction.ASCENDING)
                .orderBy(KEY_QUANTITY, Query.Direction.ASCENDING)
                .orderBy(KEY_ISBN, Query.Direction.ASCENDING)
                .orderBy(KEY_STATUS, Query.Direction.ASCENDING);

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

    //inflates the menu and toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_professor, menu);
        return true;
    }

    //selects the proper idea when an item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //converts the selected menu item to do the proper activity
        switch(item.getItemId()){
            case R.id.menuItemProfessorAddRequest:
                //Starts RequestBookActivity if the button is clicked
                Intent intToAddBookRequest = new Intent(ProfessorHomeActivity.this, RequestBookActivity.class);
                startActivity(intToAddBookRequest);
                return true;
            case R.id.menuItemProfessorLogout:
                //signs out user
                FirebaseAuth.getInstance().signOut();
                //Starts LoginActivity if this button is clicked
                Intent intToLogin = new Intent(ProfessorHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}// END OF ProfCheckOrders extends AppCompatActivity - - - - - - - - - - - - - - - - - - - - - - - -
