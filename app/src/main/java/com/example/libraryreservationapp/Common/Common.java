package com.example.libraryreservationapp.Common;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Room;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class Common {

    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ROOM_SELECTED = "ROOM_SELECTED";
    public static final String KEY_ENABLE_BUTTON_NEXT = "KEY_ENABLE_BUTTON_NEXT";
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



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertTimeSlotToString(int slot) {
        if (slot == 0 && (LocalTime.now().isBefore(LocalTime.parse("09:00"))  || isDayAfterToday())) {
            return "9:00a-9:30a";
        }
        else if (slot == 1 && (LocalTime.now().isBefore(LocalTime.parse("09:30"))  || isDayAfterToday()))
            return "9:30a-10:00a";
        else if (slot == 2 && (LocalTime.now().isBefore(LocalTime.parse("10:00"))  || isDayAfterToday()))
            return "10:00a-10:30a";
        else if (slot == 3 && (LocalTime.now().isBefore(LocalTime.parse("10:30"))  || isDayAfterToday()))
            return "10:30a-11:00a";
        else if (slot == 4 && (LocalTime.now().isBefore(LocalTime.parse("11:00"))  || isDayAfterToday()))
            return "11:00a-11:30a";
        else if (slot == 5 && (LocalTime.now().isBefore(LocalTime.parse("11:30"))  || isDayAfterToday()))
            return "11:30a-12:00p";
        else if (slot == 6 && (LocalTime.now().isBefore(LocalTime.parse("12:00"))  || isDayAfterToday()))
            return "12:00p-12:30p";
        else if (slot == 7 && (LocalTime.now().isBefore(LocalTime.parse("12:30"))  || isDayAfterToday()))
            return "12:30p-1:00p";
        else if (slot == 8)
            return "1:00p-1:30p";
        else if (slot == 9)
            return "1:30p-2:00p";
        else if (slot == 10)
            return "2:00p-2:30p";
        else if (slot == 11)
            return "2:30p-3:00p";
        else if (slot == 12)
            return "3:00p-3:30p";
        else if (slot == 13)
            return "3:30p-4:00p";
        else if (slot == 14)
            return "4:00p-4:30p";
        else if (slot == 15)
            return "4:30p-5:00p";
        else if (slot == 16)
            return "5:00p-5:30p";
        else if (slot == 17)
            return "5:30p-6:00p";
        else if (slot == 18)
            return "6:00p-6:30p";
        else if (slot == 19)
            return "6:30p-7:00p";
        else
            return String.valueOf(R.string.closed);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isDayAfterToday(){
        return LocalDate.now().getDayOfYear() < currentDate.get(Calendar.DAY_OF_YEAR);
    }
}




    /*public static String convertTimeSlotToString(int slot) {
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
    }*/

