package org.os;

import java.util.*;

public class Schedule {
    private List<Process> processes; // list of processes

    // Constructor
    public Schedule() {
        processes = new ArrayList<>(); // Properly initialize the processes list
    }

    public void addProcess(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        processes.add(new Process(name, color, arrivalTime, burstTime, priority, quantum));
    } // addProcess function to add process

    public void nonPreemptivePrioritySchedulingWithAging(int contextSwitchingTime) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        Process previousProcess = null; // To keep track of the previous process for context switching.
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));
        List<Process> completed = new ArrayList<>();
        int sizeOfCompleted = completed.size();
        int sizeOfProcesses = processes.size();

        while (sizeOfCompleted < sizeOfProcesses) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !readyQueue.contains(p) && !completed.contains(p)) {
                    readyQueue.add(p);
                }
            }
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            for (Process p : readyQueue) {
                p.waitingTimeUnit++;
                if (p.waitingTimeUnit % 10 == 0) {
                    p.priority--;
                }
            }
            Process current = readyQueue.poll();
            // Add context switching time if the current process is different from the previous one.
            if (previousProcess != null && previousProcess != current) {
                System.out.println("Context switching at time " + currentTime);
                currentTime += contextSwitchingTime;
            }

            previousProcess = current;
            System.out.println("Executing: " + current.name + " from time " + currentTime + " to " + (currentTime + current.burstTime));
            current.waitingTime = currentTime - current.arrivalTime;
            current.turnaroundTime = currentTime + current.burstTime - current.arrivalTime;
            totalWaitingTime += current.waitingTime;
            totalTurnaroundTime += current.turnaroundTime;
            currentTime += current.burstTime;
            completed.add(current);
            sizeOfCompleted = completed.size(); // Update sizeOfCompleted to avoid infinite loop
            printResults(completed, totalWaitingTime, totalTurnaroundTime, processes.size());
        }
    }

    public void shortestRemainingTimeFirst(int contextSwitchingTime) {
        // Hello :)

        // Sort the processes by arrival time.
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0; // Current time in the simulation.
        int totalWaitingTime = 0; // Total waiting time of all processes.
        int totalTurnaroundTime = 0; // Total turnaround time of all processes.

        // Initialize a priority queue to store ready processes, sorted by shortest remaining time.
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        List<Process> completed = new ArrayList<>(); // A list to keep track of completed processes.
        int sizeOfCompleted = completed.size(); // Number of completed processes.
        int sizeOfProcesses = processes.size(); // Total number of processes.
        Process previousProcess = null; // To keep track of the previous process for context switching.

        // Main loop to add new processes to the ready queue and manage execution.
        while (sizeOfCompleted < sizeOfProcesses) {
            for (Process p : processes) {
                // Add processes to the ready queue if they have arrived and are not in the queue or completed.
                if (p.arrivalTime <= currentTime && !readyQueue.contains(p) && !completed.contains(p)) {
                    readyQueue.add(p);
                }
            }

            // If the ready queue is empty, increment the current time.
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Implement aging to prevent starvation.
            for (Process p : readyQueue) {
                p.waitingTimeUnit++;
                if (p.waitingTimeUnit % 5 == 0) { // Increase priority every 5 times.
                    p.priority--;
                    if (p.priority < 0) p.priority = 0; // Ensure priority doesn't go below 0.
                }
            }

            // Get the current process to execute from the ready queue.
            Process current = readyQueue.poll();

            // Add context switching time if the current process is different from the previous one.
            if (previousProcess != null && previousProcess != current) {
                System.out.println("Context switching at time " + currentTime);
                currentTime += contextSwitchingTime;
            }

            previousProcess = current;

            // Determine the next arrival time of processes that are not completed.
            int nextArrival = -1;
            for (Process p : processes) {
                if (p.arrivalTime > currentTime && !p.isCompleted()) {
                    nextArrival = p.arrivalTime;
                    break;
                }
            }

            // Calculate the time slice for the current process.
            int timeSlice;
            if (nextArrival == -1) {
                // If there is no next arrival, set the timeSlice to the remaining time of the current process.
                timeSlice = current.remainingTime;
            } else {
                // Otherwise, set the timeSlice to the minimum of the current process's remaining time and the time until the next process arrives.
                timeSlice = Math.min(current.remainingTime, nextArrival - currentTime);
            }

            // Execute the process.
            System.out.println("Executing: " + current.name + " from time " + currentTime + " to " + (currentTime + timeSlice));
            current.remainingTime -= timeSlice; // Update the remaining time of the current process.
            currentTime += timeSlice; // Update the current time.

            // Check if the process has finished executing.
            if (current.remainingTime == 0) {
                current.waitingTime = currentTime - current.arrivalTime - current.burstTime; // Calculate waiting time.
                current.turnaroundTime = currentTime - current.arrivalTime; // Calculate turnaround time.
                totalWaitingTime += current.waitingTime; // Update total waiting time.
                totalTurnaroundTime += current.turnaroundTime; // Update total turnaround time.
                completed.add(current); // Add the process to the completed list.
            } else {
                readyQueue.add(current); // If the process is not finished, add it back to the ready queue.
            }

            sizeOfCompleted = completed.size(); // Update the number of completed processes.
            printResults(completed, totalWaitingTime, totalTurnaroundTime, processes.size()); // Print the results after each iteration.
            // Bye :)
        }
    }
    public void shortestJobFirstWithAging() {
        System.out.println("Shortest job first ");
        this.processes.sort(Comparator.comparingInt(px -> px.arrivalTime));
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(px -> px.burstTime));
        List<Process> completed = new ArrayList<>();

        while (completed.size() < this.processes.size()) {
            for (Process p : this.processes) {
                if (p.arrivalTime <= currentTime && !readyQueue.contains(p) && !completed.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
            } else {
                for (Process p : readyQueue) {
                    p.waitingTimeUnit++;
                    if (p.waitingTimeUnit % 5 == 0) {
                        p.burstTime = Math.max(1, p.burstTime - 1); // Reducing burst time as aging mechanism
                    }
                }

                Process current = readyQueue.poll();
                System.out.println("Executing: " + current.name + " from time " + currentTime + " to " + (currentTime + current.burstTime));
                current.waitingTime = currentTime - current.arrivalTime;
                current.turnaroundTime = currentTime + current.burstTime - current.arrivalTime;
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;
                currentTime += current.burstTime;
                completed.add(current);

                printResults(completed, totalWaitingTime, totalTurnaroundTime, this.processes.size());
            }
        }
    }
    public void fcaiScheduling(int contextSwitchingTime) {
        // Calculate V1 and V2
        int lastArrivalTime = processes.get(processes.size() - 1).arrivalTime;
        int maxBurstTime = processes.stream().mapToInt(p -> p.burstTime).max().orElse(1);
        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> completed = new ArrayList<>();

        // Initial FCAI Factor Calculation and Display
        System.out.println("Processes Burst time Arrival time Priority Quantum Initial FCAI Factors");
        for (Process p : processes) {
            p.fcaiFactor = (10 - p.priority) + (p.arrivalTime / V1) + (p.burstTime / V2);
            System.out.println(String.format("%s %d %d %d %d %.2f",
                    p.name, p.burstTime, p.arrivalTime, p.priority, p.quantum, p.fcaiFactor));
        }

        // Initialize remaining burst time for each process
        for (Process p : processes) {
            p.remainingTime = p.burstTime; // Set remaining time to burst time initially
        }

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to readyQueue when they arrive
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                Process arrivingProcess = processes.remove(0);
                readyQueue.add(arrivingProcess);
                System.out.println("Process " + arrivingProcess.name + " added to ready queue at time " + currentTime);
            }

            if (!readyQueue.isEmpty()) {
                // Recalculate FCAI Factor for all processes in ready queue
                for (Process process : readyQueue) {
                    process.fcaiFactor = (10 - process.priority) +
                            (process.arrivalTime / V1) +
                            (process.remainingTime / V2);
                }

                // Sort readyQueue based on FCAI Factor (in descending order)
                readyQueue = new LinkedList<>(readyQueue.stream()
                        .sorted((p1, p2) -> Double.compare(p2.fcaiFactor, p1.fcaiFactor))  // Reverse order
                        .toList());

                Process current = readyQueue.poll();

                // Execute process for 40% of its quantum
                int quantum = current.quantum;
                int executeTime = (int) Math.ceil(quantum * 0.4);

                // Ensure executeTime does not exceed remainingTime
                executeTime = Math.min(executeTime, current.remainingTime);

                // If executeTime is 0, then set it to at least 1 for valid execution
                if (executeTime <= 0) {
                    executeTime = 1;
                }

                current.remainingTime -= executeTime;
                currentTime += executeTime;

                System.out.println("Executing: " + current.name + " from time " + (currentTime - executeTime) + " to " + currentTime);

                // Ensure remainingTime does not go negative
                if (current.remainingTime <= 0) {
                    current.remainingTime = 0;
                    // Process completed
                    current.completionTime = currentTime;
                    current.turnaroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;

                    totalWaitingTime += current.waitingTime;
                    totalTurnaroundTime += current.turnaroundTime;

                    completed.add(current);

                    System.out.println("Process " + current.name + " completed at time " + currentTime +
                            ". Turnaround Time: " + current.turnaroundTime + ", Waiting Time: " + current.waitingTime);
                } else {
                    // Process preempted or continues execution
                    boolean preempt = false;

                    // Check if any other process has a better FCAI factor and should preempt the current process
                    for (Process other : readyQueue) {
                        if (other.fcaiFactor > current.fcaiFactor) {
                            preempt = true;
                            break;
                        }
                    }

                    if (preempt) {
                        current.quantum += (quantum - executeTime); // Add unused quantum back to the process
                        readyQueue.add(current); // Re-add process to queue for re-execution
                        System.out.println("Process " + current.name + " preempted. Updated Quantum: " + current.quantum);
                    } else {
                        readyQueue.add(current); // Re-add current process back to the ready queue if it is not preempted
                    }
                }

                currentTime += contextSwitchingTime;
                System.out.println("Context switching at time " + currentTime);

                // Print process execution details after each context switch
                System.out.println("\nProcess Execution Details:");
                System.out.println("-----------------------------------------------------");
                System.out.println("| Process | Waiting Time | Turnaround Time | Priority |");
                System.out.println("-----------------------------------------------------");
                for (Process p : completed) {
                    System.out.println(String.format("|   %s   |      %d       |       %d        |    %d    |",
                            p.name, p.waitingTime, p.turnaroundTime, p.priority));
                }
                System.out.println("-----------------------------------------------------");

                if (!completed.isEmpty()) {
                    double avgWaitingTime = totalWaitingTime / (double) completed.size();
                    double avgTurnaroundTime = totalTurnaroundTime / (double) completed.size();
                    System.out.println("Average Waiting Time: " + avgWaitingTime);
                    System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
                } else {
                    System.out.println("No processes completed. Average times cannot be calculated.");
                }
            } else {
                // No process ready, increment time
                System.out.println("No process ready. System idle at time " + currentTime);
                currentTime++;
            }
        }
    }



    private static void printResults(List<Process> completed, int totalWaitingTime, int totalTurnaroundTime, int totalProcesses) {
        System.out.println("\nProcess Execution Details:");
        System.out.println("-----------------------------------------------------");
        System.out.println("| Process | Waiting Time | Turnaround Time | Priority |");
        System.out.println("-----------------------------------------------------");

        for (Process p : completed) {
            System.out.printf("|   %s    |      %d       |       %d         |    %d    |\n",
                    p.name, p.waitingTime, p.turnaroundTime, p.priority);
        }

        System.out.println("-----------------------------------------------------");

        // Avoid division by zero
        if (totalProcesses > 0) {
            System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / totalProcesses);
            System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / totalProcesses);
        } else {
            System.out.println("No processes completed. Average times cannot be calculated.");
        }
    }

}