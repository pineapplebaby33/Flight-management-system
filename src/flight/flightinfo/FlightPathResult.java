/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightinfo;
import java.util.*;
/**
 *
 * @author shan
 */

public class FlightPathResult {
    private List<FlightInfo> fastestPath;
    private List<FlightInfo> cheapestPath;

    public FlightPathResult(List<FlightInfo> fastestPath, List<FlightInfo> cheapestPath) {
        this.fastestPath = fastestPath;
        this.cheapestPath = cheapestPath;
    }

    public List<FlightInfo> getFastestPath() {
        return fastestPath;
    }

    public List<FlightInfo> getCheapestPath() {
        return cheapestPath;
    }
}
