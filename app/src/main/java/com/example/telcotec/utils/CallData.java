package com.example.telcotec.utils;

import java.util.Date;

public class CallData {
    String number ;
    String operator ;
    String duration ;
    String type ;
    String date ;

    public CallData(String number, String operator, String duration, String type , String date) {
        this.number = number;
        this.operator = operator;
        this.duration = duration;
        this.type = type;
        this.date = date;

    }

    @Override
    public String toString() {
        return "{" +
                "number='" + number + '\'' +
                ", operator='" + operator + '\'' +
                ", duration='" + duration + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return operator;
    }

    public void setName(String name) {
        this.operator = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
