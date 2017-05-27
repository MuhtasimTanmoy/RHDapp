package com.example.t.roadsandhighway;

/**
 * Created by t on 5/28/17.
 */

public class StatusObject {
    public String address;
    public double lat;
    public double lng;
    public String level;
    public String averageSpeed;
    public String trafficVolume;
    public String note;
    public String createdAt;
    public String contactNo;

    public StatusObject(String address, double lat, double lng,
                        String level, String averageSpeed, String trafficVolume,
                        String note, String createdAt, String contactNo) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.level = level;
        this.averageSpeed = averageSpeed;
        this.trafficVolume = trafficVolume;
        this.note = note;
        this.createdAt = createdAt;
        this.contactNo = contactNo;
    }
}
