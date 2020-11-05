package com.example.libraryreservationapp.Common;

import android.content.Intent;

import com.example.libraryreservationapp.Room;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {

    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ROOM_SELECTED = "ROOM_SELECTED";
    public static final String KEY_ENABLE_BUTTON_NEXT = "KEY_ENABLE_BUTTON_NEXT" ;
    public static final int TIME_SLOT_TOTAL = 20;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_RESERVATION = "CONFIRM_RESERVATION";
    public static int step = 0; // first step is 0
    public static Room currentRoom;
    public static int currentTimeSlot = -1;
    public static Calendar currentDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy"); // only use it when needed for key
    public static String userID;

    public static String convertTimeSlotToString(int slot) {
        switch(slot) {
            case 0:
                return "9:00a-9:30a";
            case 1:
                return "9:30a-10:00a";
            case 2:
                return "10:00a-10:30a";
            case 3:
                return "10:30a-11:00a";
            case 4:
                return "11:00a-11:30a";
            case 5:
                return "11:30a-12:00p";
            case 6:
                return "12:00p-12:30p";
            case 7:
                return "12:30p-1:00p";
            case 8:
                return "1:00p-1:30p";
            case 9:
                return "1:30p-2:00p";
            case 10:
                return "2:00p-2:30p";
            case 11:
                return "2:30p-3:00p";
            case 12:
                return "3:00p-3:30p";
            case 13:
                return "3:30p-4:00p";
            case 14:
                return "4:00p-4:30p";
            case 15:
                return "4:30p-5:00p";
            case 16:
                return "5:00p-5:30p";
            case 17:
                return "5:30p-6:00p";
            case 18:
                return "6:00p-6:30p";
            case 19:
                return "6:30p-7:00p";
            default:
                return "Closed";
        }
    }
}
