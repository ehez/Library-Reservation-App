package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LibrarianHomeActivity extends AppCompatActivity
{
    //private member variables
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_home);

        toolbar = findViewById(R.id.toolbarLibrarian);

        //gets instance of firestore
        fStore = FirebaseFirestore.getInstance();

        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);

        //calls the recycler view for it to be set up
        MakeRecyclerView();

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

    //inflates the menu and toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_librarian, menu);
        return true;
    }

    //selects the proper idea when an item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //converts the selected menu item to do the proper activity
        switch(item.getItemId()){
            case R.id.menuItemLibrarianAddBook:
                //Starts AddBookActivity if the button is clicked
                Intent intToAddBook = new Intent(LibrarianHomeActivity.this, AddBookActivity.class);
                startActivity(intToAddBook);
                return true;
            case R.id.menuItemLibrarianSeeRequests:
                //Starts RequestActivity if the button is clicked
                Intent intToRequest = new Intent(LibrarianHomeActivity.this, SeeRequestActivity.class);
                startActivity(intToRequest);
                return true;
            case R.id.menuItemLibrarianLogout:
                //signs out user
                FirebaseAuth.getInstance().signOut();
                //Starts LoginActivity if this button is clicked
                Intent intToLogin = new Intent(LibrarianHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}