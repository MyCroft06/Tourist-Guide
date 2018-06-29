package com.test.ertugrulemre.htmlparsing;

public class Near {

    private String  name;
    private String  distance;
    private String  duration;

    public Near(String name, String distance, String duration) {
        this.name = name;
        this.distance = distance;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
