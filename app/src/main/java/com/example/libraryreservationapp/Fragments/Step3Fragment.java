package com.example.libraryreservationapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.RoomReservationInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Step3Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    TextView txt_reservation_date_text, txt_reservation_time_text, txt_reservation_building_text, txt_reservation_room_text, txt_reservation_capacity_text;
    Button btn_confirm;
    ImageView img_computer, img_wifi, img_whiteboard;
    FirebaseFirestore fStore;

    BroadcastReceiver confirmReservationReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {

        resetVectorImages();

        txt_reservation_building_text.setText(Common.currentRoom.getBuilding());
        txt_reservation_room_text.setText("Room# "+Common.currentRoom.getRoomNumber());
        txt_reservation_capacity_text.setText("Capacity: "+Common.currentRoom.getCapacity());
        txt_reservation_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot)));
        txt_reservation_date_text.setText(simpleDateFormat.format(Common.currentDate.getTime()));

        if(Common.currentRoom.isComputer())
            img_computer.setColorFilter(getResources().getColor(R.color.available));
        if(Common.currentRoom.isWifi())
            img_wifi.setColorFilter(getResources().getColor(R.color.available));
        if(Common.currentRoom.isWhiteboard())
            img_whiteboard.setColorFilter(getResources().getColor(R.color.available));

    }

    static Step3Fragment instance;

    public static Step3Fragment getInstance(){
        if (instance == null)
            instance = new Step3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply format to date display
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmReservationReceiver, new IntentFilter(Common.KEY_CONFIRM_RESERVATION));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmReservationReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_step3, container, false);

        txt_reservation_date_text = itemView.findViewById(R.id.txt_reservation_date_text);
        txt_reservation_time_text = itemView.findViewById(R.id.txt_reservation_time_text);
        txt_reservation_building_text = itemView.findViewById(R.id.txt_reservation_building_text);
        txt_reservation_room_text = itemView.findViewById(R.id.txt_reservation_room_text);
        txt_reservation_capacity_text = itemView.findViewById(R.id.txt_reservation_capacity_text);

        img_computer = itemView.findViewById(R.id.img_computer);
        img_wifi = itemView.findViewById(R.id.img_wifi);
        img_whiteboard = itemView.findViewById(R.id.img_whiteboard);

        btn_confirm = itemView.findViewById(R.id.btn_confirm);

        fStore = FirebaseFirestore.getInstance();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

//                //create reservation information
//                final RoomReservationInformation roomReservationInformation = new RoomReservationInformation();
//
//                roomReservationInformation.setBuilding(Common.currentRoom.getBuilding());
//                roomReservationInformation.setRoomNumber(String.valueOf(Common.currentRoom.getRoomNumber()));
//                roomReservationInformation.setRoomId(Common.currentRoom.getRoomId());
//                roomReservationInformation.setTime(Common.convertTimeSlotToString(Common.currentTimeSlot));
//                roomReservationInformation.setDate(simpleDateFormat.format(Common.currentDate.getTime()));
//                roomReservationInformation.setSlot(Long.valueOf(Common.currentTimeSlot));
//                roomReservationInformation.setUserId(Common.userID);

                //creates a hashmap to store all the data for the room reservation to put into the specific users collection of reservations
                Map<String, Object> info = new HashMap<>();
                info.put("building", Common.currentRoom.getBuilding());
                info.put("roomNumber", String.valueOf(Common.currentRoom.getRoomNumber()));
                info.put("date", simpleDateFormat.format(Common.currentDate.getTime()));
                info.put("time", Common.convertTimeSlotToString(Common.currentTimeSlot));
                info.put("roomId", Common.currentRoom.getRoomId());

                fStore.collection("users").document(Common.userID).collection("currentReservations").add(info).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("MYDEBUG", "The info for the reservation was added to the user");
                        String reservationID = documentReference.getId();
                        Log.d("MYDEBUG", "In the fragment reservationid is " + reservationID);

                        Map<String, Object> reservationInfo = new HashMap<>();
                        reservationInfo.put("building", Common.currentRoom.getBuilding());
                        reservationInfo.put("roomNumber", String.valueOf(Common.currentRoom.getRoomNumber()));
                        reservationInfo.put("roomId", Common.currentRoom.getRoomId());
                        reservationInfo.put("time", Common.convertTimeSlotToString(Common.currentTimeSlot));
                        reservationInfo.put("date", simpleDateFormat.format(Common.currentDate.getTime()));
                        reservationInfo.put("slot", Long.valueOf(Common.currentTimeSlot));
                        reservationInfo.put("userId", Common.userID);
                        reservationInfo.put("reservationId", reservationID);

                        //submit to room document
                        DocumentReference reservationDate = fStore.collection("room")
                                .document(Common.currentRoom.getRoomId()).collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                                .document(String.valueOf(Common.currentTimeSlot));

                        //Write reservation data to Firestore
                        reservationDate.set(reservationInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                resetStaticData();
                                getActivity().finish(); //close activity
                                Toast.makeText(getContext(), "Reservation Confirmed!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }

        });

        unbinder = ButterKnife.bind(this, itemView);

        return itemView;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentRoom = null;
        Common.currentDate.add(Calendar.DATE, 0); //current date

    }

    private void resetVectorImages(){
        img_computer.setColorFilter(getResources().getColor(R.color.colorButton));
        img_wifi.setColorFilter(getResources().getColor(R.color.colorButton));
        img_whiteboard.setColorFilter(getResources().getColor(R.color.colorButton));
    }
}
