package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {

    //private member variables
    private RecyclerView recyclerView;
    private TextView textViewNoRooms;
    private Button btnReserveRoom, btnRoomRate;
    private RoomReservationAdapter adapter;
    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userID;

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
        Query query = fStore.collection("users").document(userID).collection("currentReservations").orderBy("date", Query.Direction.ASCENDING);

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
    }

    //what occurs when the fragment is started
    @Override
    public void onStart() {
        super.onStart();
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
