package com.example.ujian;

public class Trip {
    private int id;
    private int driverId;
    private String from;
    private String to;
    private String time;

    public Trip() {}

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
