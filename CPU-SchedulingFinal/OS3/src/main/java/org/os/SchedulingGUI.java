package org.os;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SchedulingGUI {

    private JFrame frame;
    private JPanel ganttPanel;
    private DefaultTableModel processTableModel;
    private JTextField processCountField;
    private JTextField nameField;
    private JTextField colorField;
    private JTextField arrivalField;
    private JTextField burstField;
    private JTextField priorityField;
    private JComboBox<String> algorithmBox;
    private List<Process> processes;

    public SchedulingGUI() {
        processes = new ArrayList<>();

        frame = new JFrame("CPU Scheduling Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("CPU Scheduling Graph", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.RED);
        frame.add(title, BorderLayout.NORTH);

        // Main panel for Gantt chart and process info
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Gantt Chart Panel
        ganttPanel = new JPanel();
        ganttPanel.setBackground(Color.DARK_GRAY);
        ganttPanel.setLayout(null);
        ganttPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        JLabel ganttTitle = new JLabel("Gantt Chart", JLabel.CENTER);
        ganttTitle.setFont(new Font("Arial", Font.BOLD, 16));
        ganttTitle.setForeground(Color.WHITE);
        ganttTitle.setBounds(10, 10, 650, 20);
        ganttPanel.add(ganttTitle);

        mainPanel.add(ganttPanel);

        // Process Information Table
        JPanel processInfoPanel = new JPanel();
        processInfoPanel.setLayout(new BorderLayout());
        processInfoPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        JLabel processTitle = new JLabel("Processes Information", JLabel.CENTER);
        processTitle.setFont(new Font("Arial", Font.BOLD, 16));
        processTitle.setForeground(Color.RED);
        processInfoPanel.add(processTitle, BorderLayout.NORTH);

        String[] columnNames = {"PROCESS", "COLOR", "NAME", "PID", "PRIORITY", "ARRIVAL", "BURST"};
        processTableModel = new DefaultTableModel(columnNames, 0);

        JTable processTable = new JTable(processTableModel);
        processTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(Color.decode(value.toString()));
                return label;
            }
        });
        processTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        processTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        processTable.getTableHeader().setForeground(Color.RED);
        processTable.setGridColor(Color.BLACK);
        processTable.setFont(new Font("Arial", Font.PLAIN, 12));
        processTable.setRowHeight(25);
        JScrollPane tableScrollPane = new JScrollPane(processTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 200));
        processInfoPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(processInfoPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        // Number of Processes
        JPanel processCountPanel = new JPanel();
        JLabel processCountLabel = new JLabel("Number of Processes:");
        processCountField = new JTextField(5);
        processCountPanel.add(processCountLabel);
        processCountPanel.add(processCountField);
        inputPanel.add(processCountPanel);

        // Process Details Inputs
        JPanel processDetailPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        nameField = new JTextField();
        colorField = new JTextField();
        arrivalField = new JTextField();
        burstField = new JTextField();
        priorityField = new JTextField();
        processDetailPanel.add(new JLabel("Process Name:"));
        processDetailPanel.add(nameField);
        processDetailPanel.add(new JLabel("Color (#Hex):"));
        processDetailPanel.add(colorField);
        processDetailPanel.add(new JLabel("Arrival Time:"));
        processDetailPanel.add(arrivalField);
        processDetailPanel.add(new JLabel("Burst Time:"));
        processDetailPanel.add(burstField);
        processDetailPanel.add(new JLabel("Priority:"));
        processDetailPanel.add(priorityField);
        inputPanel.add(processDetailPanel);

        // Scheduling Algorithm and Run Button
        JPanel controlPanel = new JPanel();
        String[] algorithms = {"Non-preemptive Priority", "SJF", "SRTF", "FCAI Scheduling"};
        algorithmBox = new JComboBox<>(algorithms);
        JButton addProcessButton = new JButton("Add Process");
        JButton runButton = new JButton("Run Scheduling");

        addProcessButton.addActionListener(e -> addProcess());
        runButton.addActionListener(e -> runScheduling());

        controlPanel.add(new JLabel("Algorithm:"));
        controlPanel.add(algorithmBox);
        controlPanel.add(addProcessButton);
        controlPanel.add(runButton);

        inputPanel.add(controlPanel);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addProcess() {
        try {
            String name = nameField.getText();
            String color = colorField.getText();
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            int priority = Integer.parseInt(priorityField.getText());
            int processNumber = processes.size();

            Process process = new Process(name, color, processNumber, arrivalTime, burstTime, priority);
            processes.add(process);
            updateProcessTable(processes);

            nameField.setText("");
            colorField.setText("");
            arrivalField.setText("");
            burstField.setText("");
            priorityField.setText("");

            JOptionPane.showMessageDialog(frame, "Process added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values for Arrival Time, Burst Time, and Priority.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runScheduling() {
        String selectedAlgorithm = (String) algorithmBox.getSelectedItem();
        List<GanttBlock> ganttBlocks = new ArrayList<>();
        int currentTime = 0;

        if (selectedAlgorithm.equals("Non-preemptive Priority")) {
            processes.sort(Comparator.comparingInt((Process p) -> p.priority).thenComparingInt(p -> p.arrivalTime));
        } else if (selectedAlgorithm.equals("SJF")) {
            processes.sort(Comparator.comparingInt((Process p) -> p.burstTime).thenComparingInt(p -> p.arrivalTime));
        } else if (selectedAlgorithm.equals("SRTF")) {
            // Simplified SRTF: Execute in order of shortest remaining burst time
            processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime));

            while (!processes.isEmpty()) {
                int finalCurrentTime = currentTime;
                Process nextProcess = processes.stream()
                        .filter(p -> p.arrivalTime <= finalCurrentTime)
                        .min(Comparator.comparingInt(p -> p.burstTime))
                        .orElse(processes.get(0));

                ganttBlocks.add(new GanttBlock(nextProcess.name, currentTime, currentTime + nextProcess.burstTime, nextProcess.color));
                currentTime += nextProcess.burstTime;
                processes.remove(nextProcess);
            }
            updateGanttChart(ganttBlocks);
            return;
        } else if (selectedAlgorithm.equals("FCAI Scheduling")) {
            // FCAI Scheduling logic
            String contextSwitchingTimeStr = JOptionPane.showInputDialog(frame, "Enter Context Switching Time:");
            int contextSwitchingTime = Integer.parseInt(contextSwitchingTimeStr);

            processes.sort(Comparator.comparingInt((Process p) -> p.priority)
                    .thenComparingInt(p -> p.arrivalTime));

            int previousEndTime = 0;

            for (Process process : processes) {
                // Ensure the process doesn't start before its arrival time
                if (previousEndTime < process.arrivalTime) {
                    previousEndTime = process.arrivalTime;
                }

                // Add context switching time if it's not the first process
                if (previousEndTime != process.arrivalTime) {
                    previousEndTime += contextSwitchingTime;  // Add context switching time
                }

                // Create a Gantt block for the current process
                ganttBlocks.add(new GanttBlock(process.name, previousEndTime, previousEndTime + process.burstTime, process.color));

                // Update the previous end time after the current process finishes
                previousEndTime += process.burstTime;
            }

            updateGanttChart(ganttBlocks);
            JOptionPane.showMessageDialog(frame, "Scheduling executed using FCAI Scheduling.");
            return;
        }

        for (Process process : processes) {
            if (process.arrivalTime > currentTime) {
                currentTime = process.arrivalTime;
            }
            ganttBlocks.add(new GanttBlock(process.name, currentTime, currentTime + process.burstTime, process.color));
            currentTime += process.burstTime;
        }

        updateGanttChart(ganttBlocks);
        JOptionPane.showMessageDialog(frame, "Scheduling executed using " + selectedAlgorithm + ".");
    }

    public void updateProcessTable(List<SchedulingGUI.Process> processes) {
        processTableModel.setRowCount(0);
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            processTableModel.addRow(new Object[]{i, process.color, process.name, process.processNumber, process.priority, process.arrivalTime, process.burstTime});
        }
    }

    public void updateGanttChart(List<SchedulingGUI.GanttBlock> ganttBlocks) {
        ganttPanel.removeAll();
        int x = 0;
        int y = 50;
        int blockHeight = 40;
        int padding = 10;
        for (GanttBlock block : ganttBlocks) {
            JLabel blockLabel = new JLabel(block.processName + " [" + block.startTime + " - " + block.endTime + "]");
            blockLabel.setOpaque(true);
            blockLabel.setBackground(Color.decode(block.color));
            blockLabel.setHorizontalAlignment(SwingConstants.CENTER);
            blockLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            blockLabel.setBounds(x, y, (block.endTime - block.startTime) * 50, blockHeight);
            ganttPanel.add(blockLabel);
            x += (block.endTime - block.startTime) * 20 + padding;
        }
        ganttPanel.repaint();
        ganttPanel.revalidate();
    }

    public static class Process {
        String name;
        String color;
        int processNumber;
        int arrivalTime;
        int burstTime;
        int priority;

        public Process(String name, String color, int processNumber, int arrivalTime, int burstTime, int priority) {
            this.name = name;
            this.color = color;
            this.processNumber = processNumber;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
        }
    }

    public static class GanttBlock {
        String processName;
        int startTime;
        int endTime;
        String color;

        public GanttBlock(String processName, int startTime, int endTime, String color) {
            this.processName = processName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        new SchedulingGUI();
    }
}
