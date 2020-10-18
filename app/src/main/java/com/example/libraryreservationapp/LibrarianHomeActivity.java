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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LibrarianHomeActivity extends AppCompatActivity
{
    private Button btnLogout;
    private Button btnAddBook;
    private Button btnRequest;


    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_home);

        btnLogout = findViewById(R.id.logout);
        btnAddBook = findViewById(R.id.btnGoToAddBook);
        btnRequest = findViewById(R.id.btnGoToSeeRequest);



        fStore = FirebaseFirestore.getInstance();


        //calls the recycler view for it to be set up
        MakeRecyclerView();

        // Log Out On Click Listener
        btnLogout.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(LibrarianHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);

            }
        }));

        // Add Book On Click Listener
        btnAddBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Starts AddBookActivity if the button is clicked
                Intent intToAddBook = new Intent(LibrarianHomeActivity.this, AddBookActivity.class);
                startActivity(intToAddBook);
            }
        });

        // Request from professor On Click Listener
        btnRequest.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View view)
        {
            //Starts RequestActivity if the button is clicked
            Intent intToRequest = new Intent(LibrarianHomeActivity.this, SeeRequestActivity.class);
            startActivity(intToRequest);
        }
        });
        //listens on the book adapter
        adapter.setOnItemClickListener(new BookAdapter.BookAdapterListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //gets the documentID of the item clicked
                String docID = documentSnapshot.getReference().getId();
                //Starts UpdateDeleteRoomActivity if an item in the recyclerView is clicked passing the documentID
                Intent intToUpdateDeleteBook = new Intent(LibrarianHomeActivity.this, UpdateDeleteBookActivity.class);
                intToUpdateDeleteBook.putExtra("docID", docID);
                startActivity(intToUpdateDeleteBook);
            }
        });

    }

    private void MakeRecyclerView() {
        // creates a query that uses the collection reference to get the courses in ascending order
        Query query = fStore.collection("books").orderBy("combo", Query.Direction.ASCENDING);

        // creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>().setQuery(query, Book.class).setLifecycleOwner(this).build();

        // create the adapter with corresponding parameter
        adapter = new BookAdapter(options);

        // gets the recyclerView id for reference
        recyclerView = findViewById(R.id.recyclerViewBooks);
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