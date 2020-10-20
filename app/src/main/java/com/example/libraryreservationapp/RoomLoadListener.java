package com.example.libraryreservationapp;

import java.util.List;

public interface RoomLoadListener {
    void onAllRoomLoadSuccess(List<Room> roomList);
    void onAllRoomLoadFailed(String message);
}
