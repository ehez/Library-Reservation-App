package com.example.libraryreservationapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddRoomActivity extends AppCompatActivity {

    private SeekBar capacitySeekBar;
    private Button addRoomButton;
    private EditText roomNumberEditText;
    private RadioGroup buildingRadioGroup;
    private Switch computerSwitch;
    private Switch wifiSwitch;
    private Switch whiteboardSwitch;
    private RadioButton whitmanRadioButton;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        fStore = FirebaseFirestore.getInstance();

        capacitySeekBar = findViewById(R.id.seekBarCapacity);
        addRoomButton = findViewById(R.id.btnAddRoom);
        roomNumberEditText = findViewById(R.id.editTextRoomNumber);
        buildingRadioGroup = findViewById(R.id.radioGroupBuilding);
        computerSwitch = findViewById(R.id.switchComputer);
        wifiSwitch = findViewById(R.id.switchWifi);
        whiteboardSwitch = findViewById(R.id.switchWhiteboard);
        whitmanRadioButton = findViewById(R.id.radioButtonWhitman);

        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flags = 0;
                int roomNumber = 0;
                String building = "";

                roomNumberEditText.setError(null);
                whitmanRadioButton.setError(null);

                int capacity = capacitySeekBar.getProgress();
                String sRoomNumber = roomNumberEditText.getText().toString();
                if(sRoomNumber.equals("")){
                    flags++;
                    roomNumberEditText.setError("Please enter a numeric room number");
                }
                else{
                    roomNumber = Integer.parseInt(sRoomNumber);
                }

                boolean computer = computerSwitch.isChecked();
                boolean wifi = wifiSwitch.isChecked();
                boolean whiteboard = whiteboardSwitch.isChecked();

                int selectedBuilding = buildingRadioGroup.getCheckedRadioButtonId();
                if(selectedBuilding == -1){
                    flags++;
                    whitmanRadioButton.setError("Please select a building");
                }
                else{
                    switch (selectedBuilding){
                        case R.id.radioButtonBusiness:
                            building = getString(R.string.business);
                            break;
                        case R.id.radioButtonCampusCenter:
                            building = getString(R.string.campus_center);
                            break;
                        case R.id.radioButtonConklin:
                            building = getString(R.string.conklin);
                            break;
                        case R.id.radioButtonGleeson:
                            building = getString(R.string.gleeson);
                            break;
                        case R.id.radioButtonGreenley:
                            building = getString(R.string.greenley);
                            break;
                        case R.id.radioButtonHale:
                            building = getString(R.string.hale);
                            break;
                        case R.id.radioButtonLupton:
                            building = getString(R.string.lupton);
                            break;
                        case R.id.radioButtonSinclair:
                            building = getString(R.string.sinclair);
                            break;
                        case R.id.radioButtonThompson:
                            building = getString(R.string.thompson);
                            break;
                        case R.id.radioButtonWhitman:
                            building = getString(R.string.whitman);
                            break;
                    }
                }


                if(flags == 0){
                    Map<String, Object> roomInfo = new HashMap<>();
                    roomInfo.put("capacity", capacity);
                    roomInfo.put("computer", computer);
                    roomInfo.put("wifi", wifi);
                    roomInfo.put("whiteboard", whiteboard);
                    roomInfo.put("building", building);
                    roomInfo.put("roomNumber", roomNumber);

                    fStore.collection("room").add(roomInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Add was successful!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    finish();
                }

            }
        });
    }
}
