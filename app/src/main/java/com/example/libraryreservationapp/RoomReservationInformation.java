package com.example.libraryreservationapp;

public class RoomReservationInformation {
    private String time, roomId, roomNumber, building;
    private Long slot;

    public RoomReservationInformation() {
    }

    public RoomReservationInformation(String time, String roomId, String roomNumber, String building, Long slot) {
        this.time = time;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.building = building;
        this.slot = slot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
