package com.example.libraryreservationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class RoomAdapter extends FirestoreRecyclerAdapter<Room, RoomAdapter.MyViewHolder>{
    //creates an interface for the listener
    interface RoomAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private RoomAdapterListener listener;

    public void setOnItemClickListener(RoomAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public RoomAdapter(@NonNull FirestoreRecyclerOptions<Room> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView buildingTextView;
        public TextView roomNumberTextView;

        //constructor for ViewHolder
        public MyViewHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews
            buildingTextView = view.findViewById(R.id.textViewBuilding);
            roomNumberTextView = view.findViewById(R.id.textViewRoomNumber);

            //onClickListener for the items in the recyclerView
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gets the position of the clicked
                    int position = getAdapterPosition();
                    //makes sure the position is valid and listener exists
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    //binds the correct item into the recyclerView
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Room room) {
        String sRoomNumber = Integer.toString(room.getRoomNumber());

        // Puts the building and room numbers into the textViews for the position (i)
        myViewHolder.buildingTextView.setText(room.getBuilding());
        myViewHolder.roomNumberTextView.setText("Room Number: " + sRoomNumber);
    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }
}
