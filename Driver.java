package com.example.ujian;

public class Driver {
    private int id;
    private String name;
    private String vehicle;
    private String phone;
    private String status;

    public Driver() {}

    public Driver(String name, String vehicle, String phone, String status) {
        this.name = name;
        this.vehicle = vehicle;
        this.phone = phone;
        this.status = status;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
