package com.example.libraryreservationapp;

public class Room {

    //private member variables
    private String building;
    private int roomNumber;
    private int capacity;
    private boolean whiteboard;
    private boolean wifi;
    private boolean computer;

    //empty constructor
    public Room(){

    }

    //constructor for Room
    public Room(String building, int roomNumber, int capacity, boolean whiteboard, boolean wifi, boolean computer) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.whiteboard = whiteboard;
        this.wifi = wifi;
        this.computer = computer;
    }

    //gets building
    public String getBuilding() {
        return building;
    }

    //sets building
    public void setBuilding(String building) {
        this.building = building;
    }

    //gets room number
    public int getRoomNumber() {
        return roomNumber;
    }

    //sets room number
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    //gets capacity
    public int getCapacity() {
        return capacity;
    }

    //sets capacity
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    //gets whiteboard value
    public boolean isWhiteboard() {
        return whiteboard;
    }

    //sets whiteboard value
    public void setWhiteboard(boolean whiteboard) {
        this.whiteboard = whiteboard;
    }

    //gets wifi value
    public boolean isWifi() {
        return wifi;
    }

    //sets wifi value
    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    //gets computer value
    public boolean isComputer() {
        return computer;
    }

    //sets computer value
    public void setComputer(boolean computer) {
        this.computer = computer;
    }
}
