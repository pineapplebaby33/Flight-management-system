/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightinfo;

//import java.util.*;
//import java.io.*;
//import java.util.*;

public class FlightInfo {
    private String flightNumber;
    private String fromCity;
    private String toCity;
    private String airline;
    private double price;
    private int departureTime; // 用分钟表示，如 10:00 转化为 600
    private int arrivalTime;   // 用分钟表示，如 12:00 转化为 720

    public FlightInfo(String flightNumber, String fromCity, String toCity, String airline, double price, int departureTime, int arrivalTime) {
        this.flightNumber = flightNumber;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.airline = airline;
        this.price = price;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public int getFlightDuration() {
        return arrivalTime - departureTime;
    }

    public double getPrice() {
        return price;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    // Other getters and toString method...
}
