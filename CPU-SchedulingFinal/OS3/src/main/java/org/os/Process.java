package org.os;
import java.util.*;
public class Process {
    public double fcaiFactor;
    public int completionTime;
    String name; // Process name
    String color; // Graphical representation
    int arrivalTime; // Arrival time
    int burstTime; // Total burst time
    int remainingTime; // Remaining burst time (used in SRTF)
    int priority; // Priority number
    int quantum; // Quantum (used in FCAI Scheduling)

    // Calculated values
    int waitingTime;
    int turnaroundTime;
    int waitingTimeUnit;

    // Constructor
    public Process(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.waitingTimeUnit=0;
        this.fcaiFactor = 0.0;
        this.completionTime = 0;
    }
    //a method to check if the process is completed or not
    public boolean isCompleted() {
        return remainingTime == 0;
    }
}