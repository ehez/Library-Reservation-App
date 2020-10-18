package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SeeRequestActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_request);

        fStore = FirebaseFirestore.getInstance();

        //calls the recycler view for it to be set up
        MakeRequestRecyclerView();


        //listens on the request adapter
        adapter.setOnItemClickListener(new RequestAdapter.RequestAdapterListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //gets the documentID of the item clicked
                String docID = documentSnapshot.getReference().getId();
                //Starts UpdateApproveRequestActivity if an item in the recyclerView is clicked passing the documentID
                Intent intToUpdateApproveRequest = new Intent(SeeRequestActivity.this, UpdateApproveRequestActivity.class);
                intToUpdateApproveRequest.putExtra("docID", docID);
                startActivity(intToUpdateApproveRequest);
            }
        });

    }
    private void MakeRequestRecyclerView() {
        // creates a query that uses the collection reference to get the courses in ascending order. changed Combo to Book's name
        Query query = fStore.collection("requests").orderBy("title", Query.Direction.ASCENDING);

        // creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>().setQuery(query, Request.class).setLifecycleOwner(this).build();

        // create the adapter with corresponding parameter
        adapter = new RequestAdapter(options);

        // gets the recyclerView id for reference
        recyclerView = findViewById(R.id.recyclerViewRequest);
        // sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter
        recyclerView.setAdapter(adapter);
    }

}