package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.Common.NonSwipeViewPager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class ReserveRoomActivity extends AppCompatActivity {
    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference roomRef;

    StepView stepView;
    NonSwipeViewPager viewPager;
    Button btn_previous_step, btn_next_step;

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_room);
        ButterKnife.bind(ReserveRoomActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));;

        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btn_previous_step = findViewById(R.id.btn_previous_step);
        btn_next_step = findViewById(R.id.btn_next_step);

        //Events
        btn_previous_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.step == 2 || Common.step > 0){
                    Common.step--;
                    viewPager.setCurrentItem(Common.step);
                    if (Common.step < 2) //always enable NEXT when Step < 2
                    {
                        btn_next_step.setEnabled(true);
                        setColorButton();
                    }
                }
            }
        });

        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.step < 2 || Common.step == 0){
                    Common.step++;
                    if(Common.step == 1) { //pick time
                        if (Common.currentRoom != null){
                            loadTimeSlotOfRoom(Common.currentRoom.getRoomId());
                        }
                    }
                    else if (Common.step == 2){ //confirm
                        if(Common.currentTimeSlot != -1){
                            confirmReservation();
                        }
                    }
                }
                viewPager.setCurrentItem(Common.step);
            }
        });

        setupStepView();
        setColorButton();


        // View
        viewPager.setAdapter(new SelectRoomAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3); //keep state of 3 screens
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //show step
                stepView.go(position, true);
                if(position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void confirmReservation() {
        //Send broadcast to fragment step 3
        Intent intent = new Intent(Common.KEY_CONFIRM_RESERVATION);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfRoom(String roomId) {
        //Send Local Broadcast to Fragment step2
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }

        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Room");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }


    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if (step == 1)
                Common.currentRoom = intent.getParcelableExtra(Common.KEY_ROOM_SELECTED);
            if (step == 2)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);

            btn_next_step.setEnabled(true);
            setColorButton();

        }
    };


}