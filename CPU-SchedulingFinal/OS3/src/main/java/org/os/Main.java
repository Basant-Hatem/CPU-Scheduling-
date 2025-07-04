package org.os;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create scanner for input
        Schedule scheduler = new Schedule();

        System.out.print("Enter context switching time: ");
        int contextSwitchingTime = scanner.nextInt();

        // Ask the user for the number of processes
        System.out.print("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after the number input

        // Loop through and add each process
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");

            // Get process name
            System.out.print("Enter Process Name: ");
            String name = scanner.nextLine();

            // Get process color
            System.out.print("Enter Process Color (Graphical Representation, e.g., Red): ");
            String color = scanner.nextLine();

            // Get process arrival time
            System.out.print("Enter Process Arrival Time: ");
            int arrivalTime = scanner.nextInt();

            // Get process burst time
            System.out.print("Enter Process Burst Time: ");
            int burstTime = scanner.nextInt();

            // Get process priority
            System.out.print("Enter Process Priority: ");
            int priority = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Get process quantum
            System.out.print("Enter Process Quantum: ");
            int quantum = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Add the process to the scheduler
            scheduler.addProcess(name, color, arrivalTime, burstTime, priority, quantum);
        }

        // Run the scheduling algorithms
        System.out.println("Non-preemptive Priority Scheduling with Individual Aging Thresholds:");
        scheduler.nonPreemptivePrioritySchedulingWithAging(contextSwitchingTime);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Shortest- Remaining Time First (SRTF) Scheduling using context switching:");
        scheduler.shortestRemainingTimeFirst(contextSwitchingTime);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Shortest Job First (SJF) Scheduling with Aging:");
        scheduler.shortestJobFirstWithAging();
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("FCAI Scheduling:");
        scheduler.fcaiScheduling(contextSwitchingTime);

        scanner.close(); // Close scanner
    }
}