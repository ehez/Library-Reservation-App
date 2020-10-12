package com.example.libraryreservationapp;

public class Room {

    //private member variables
    private String building;
    private int roomNumber;
    private int capacity;
    private boolean whiteboard;
    private boolean wifi;
    private boolean computer;
    private boolean available;

    //empty constructor
    public Room(){

    }

    //constructor for Room
    public Room(String building, int roomNumber, int capacity, boolean whiteboard, boolean wifi, boolean computer, boolean available) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.whiteboard = whiteboard;
        this.wifi = wifi;
        this.computer = computer;
        this.available = available;
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

    //gets available value
    public boolean isAvailable() {
        return available;
    }

    //sets available value
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
