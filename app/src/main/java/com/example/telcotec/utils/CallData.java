package com.example.telcotec.utils;

public class CallData {
    String number ;
    String name ;
    String duration ;
    String type ;

    public CallData(String number, String name, String duration, String type) {
        this.number = number;
        this.name = name;
        this.duration = duration;
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "number='" + number + '\'' +
                ", date='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
