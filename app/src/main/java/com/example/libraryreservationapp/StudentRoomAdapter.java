package com.example.libraryreservationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentRoomAdapter extends RecyclerView.Adapter<StudentRoomAdapter.MyViewHolder> {

    Context context;
    List<Room> roomList;

    public StudentRoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.txt_room_number.setText(String.valueOf(roomList.get(i).getRoomNumber()));
        holder.txt_room_desc.setText(String.valueOf(roomList.get(i).getCapacity()));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_room_number, txt_room_desc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_room_number = (TextView) itemView.findViewById(R.id.text_room_number);
            txt_room_desc = (TextView) itemView.findViewById(R.id.text_room_desc);
        }
    }
}
