package com.example.libraryreservationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AccountsAdapter extends FirestoreRecyclerAdapter<Accounts, AccountsAdapter.MyViewHolder> {
    //creates an interface for the listener
    interface AccountsAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private AccountsAdapter.AccountsAdapterListener listener;

    public void setOnItemClickListener(AccountsAdapter.AccountsAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public AccountsAdapter(@NonNull FirestoreRecyclerOptions<Accounts> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView ramIDTextView;
        public TextView nameTextView;
        public TextView roleTextView;


        //constructor for ViewHolder
        public MyViewHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews and imageViews
            ramIDTextView = view.findViewById(R.id.textViewRAM);
            nameTextView = view.findViewById(R.id.textViewName);
            roleTextView = view.findViewById(R.id.textViewRole);


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
    protected void onBindViewHolder(@NonNull AccountsAdapter.MyViewHolder myViewHolder, int i, @NonNull Accounts accounts) {

        // Puts the ram id, name, and role into the textViews for the position (i)
        myViewHolder.ramIDTextView.setText(accounts.getRam_id());
        myViewHolder.nameTextView.setText("Name: " + accounts.getfName() + " " + accounts.getlName());
        myViewHolder.roleTextView.setText(accounts.getType());


    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public AccountsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_recyclerview_item, parent, false);
        return new AccountsAdapter.MyViewHolder(itemView);
    }
}