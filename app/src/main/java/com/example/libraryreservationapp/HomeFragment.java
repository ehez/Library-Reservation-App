package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment implements CheckInCheckOutRoomDialogFragment.CheckInCheckOutRoomDialogListener{

    //private member variables
    private RecyclerView recyclerView;
    private TextView textViewNoRooms;
    private Button btnReserveRoom, btnRoomRate;
    private RoomReservationAdapter adapter;
    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userID;
    private DocumentReference docRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //sets the view of the fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btnReserveRoom = v.findViewById(R.id.reserveRoom);
        btnRoomRate = v.findViewById(R.id.btnRoomRate);
        textViewNoRooms = v.findViewById(R.id.textViewNoRoomReservations);

        //gets instance of firestore
        fStore = FirebaseFirestore.getInstance();
        // gets the recyclerView id for reference
        recyclerView = v.findViewById(R.id.recyclerViewReservations);
        //gets the instance of the firebase auth
        auth = FirebaseAuth.getInstance();
        //gets the logged in users user id
        userID = auth.getUid();

        btnRoomRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intToRoomRate = new Intent(getActivity(), RoomsRating.class);
                startActivity(intToRoomRate);
            }
        });

        btnReserveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for the fragments to be replaced when items are selected in the drawer
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //replaces the fragment with the home fragment
                ft.replace(R.id.fragment_container, new ReserveRoomFragment()).addToBackStack("parent").commit();
            }
        });


        return v;
    }

    private void setUpRecyclerView() {
        // creates a query that uses the collection reference to get the current reservations
        Query query = fStore.collection("users").document(userID).collection("currentReservations").orderBy("date", Query.Direction.ASCENDING).orderBy("time", Query.Direction.ASCENDING)
                .orderBy("building", Query.Direction.ASCENDING).orderBy("roomNumber", Query.Direction.ASCENDING);

        // creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<RoomReservationInformation> options = new FirestoreRecyclerOptions.Builder<RoomReservationInformation>().setQuery(query, RoomReservationInformation.class).setLifecycleOwner(this).build();

        // sets the adapter with the configurations that were just made
        adapter = new RoomReservationAdapter(options);

        // sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter
        recyclerView.setAdapter(adapter);

        //listens on the roomReservation adapter
        adapter.setOnItemClickListener(new RoomReservationAdapter.RoomReservationAdapterListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //gets the documentID of the item clicked
                String docID = documentSnapshot.getReference().getId();
                //creates a reference to a specific document in the subcollection of the user
                docRef = fStore.collection("users").document(userID).collection("currentReservations").document(docID);

                //gets the value of checkedIn
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            //gets the document
                            DocumentSnapshot document = task.getResult();

                            //gets the value from the database for that individual room that was clicked on
                            boolean checkedIn = document.getBoolean("checkedIn");
                            showDialog(checkedIn);
                        }
                        else{
                            Log.d("MYDEBUG", "Couldn't get value of checkedIn for that room");
                        }
                    }
                });

            }
        });
    }

    //what occurs when the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();

        checkForReservations();
    }

    //shows the dialog
    public void showDialog(boolean checkedIn){
        DialogFragment dialog = new CheckInCheckOutRoomDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("checkedIn", checkedIn);
        Log.d("MYDEBUG", "!!!!!!!!!!!!!!!!! in showDialog: value of checkedIn is " +checkedIn);
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), "CheckInCheckOutRoomDialogFragment");

    }

    //if the user clicked the positive dialog button
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, boolean checkedIn) {
        //creates a hashmap with the data for the update
        Map<String, Object> update = new HashMap<>();
        update.put("checkedIn", checkedIn);

        //updates the document that was checked into or out of
        docRef.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "You successfully checked in/out", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Couldn't check you in/out", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("MYDEBUG", "************************* The value of checkedIn is " + checkedIn);

        if(!checkedIn){
            moveReservation();
        }
    }

    //if the user clicked the negative dialog button
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //closes the dialog
        dialog.dismiss();
    }

    //overrides the fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        //sets the listener equal to the instance of the checkInCheckOutRoomDialogFragment
        if (fragment instanceof CheckInCheckOutRoomDialogFragment) {
            ((CheckInCheckOutRoomDialogFragment) fragment).listener = this;
        }
    }

    public void moveReservation(){
        //gets the room reservation
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document at the end of the reference
                    DocumentSnapshot document = task.getResult();
                    //checks if the document is null before proceeding
                    if(document!=null){
                        //adds the reservation to the past reservations collection for the user
                        fStore.collection("users").document(userID).collection("pastReservations").add(Objects.requireNonNull(document.getData())).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    //deletes the reservation from the current reservations collection
                                    docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                //calls the recycler view for it to be re-set up
                                                checkForReservations();
                                                Log.d("MYDEBUG", "Reservation successfully deleted");
                                            }
                                            else{
                                                Log.d("MYDEBUG", "Deletion of reservation failed");
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.d("MYDEBUG", "Addition of room to the past reservations failed");
                                }
                            }
                        });
                    }
                }
                else{
                    Log.d("MYDEBUG", "Couldn't find the reservation in the database");
                }
            }
        });
    }

    public void checkForReservations(){
        //checks to see if the logged in user has any upcoming room reservations
        fStore.collection("users").document(userID).collection("currentReservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //if the query returns as empty
                if(queryDocumentSnapshots.isEmpty()){
                    //show some parts of the layout vs. others
                    recyclerView.setVisibility(View.GONE);
                    textViewNoRooms.setVisibility(View.VISIBLE);
                    btnReserveRoom.setVisibility(View.VISIBLE);
                }
                else{
                    //show some parts of the layout vs. others
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewNoRooms.setVisibility(View.GONE);
                    btnReserveRoom.setVisibility(View.GONE);
                    //calls the recycler view for it to be set up
                    setUpRecyclerView();
                }
            }
        });
    }

}
