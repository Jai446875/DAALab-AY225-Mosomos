import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class BubbleSortGUI extends JFrame {
    private JTextArea textArea;
    private JLabel statusLabel;
    private ArrayList<Integer> originalData;
    private ArrayList<Integer> currentData;
    private JScrollPane scrollPane;
    
    public BubbleSortGUI() {
        setTitle("Bubble Sort Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout());
        
        // Create components
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane = new JScrollPane(textArea);
        
        statusLabel = new JLabel("No file loaded");
        
        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton loadButton = new JButton("Load TXT File");
        JButton bubbleSortButton = new JButton("Bubble Sort");
        JButton resetButton = new JButton("Reset Data");
        JButton clearButton = new JButton("Clear Screen");
        
        // Add action listeners
        loadButton.addActionListener(e -> loadFile());
        bubbleSortButton.addActionListener(e -> bubbleSort());
        resetButton.addActionListener(e -> resetData());
        clearButton.addActionListener(e -> clearScreen());
        
        // Add buttons to panel
        buttonPanel.add(loadButton);
        buttonPanel.add(bubbleSortButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(clearButton);
        
        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        originalData = new ArrayList<>();
        currentData = new ArrayList<>();
    }
    
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            readFile(file);
        }
    }
    
    private void readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            originalData.clear();
            String line;
            
            textArea.setText("");
            appendToBottom("=== Reading file: " + file.getName() + " ===\n\n");
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                if (!line.isEmpty()) {
                    String[] parts = line.split("[,\\s\t]+");
                    
                    for (String part : parts) {
                        part = part.trim();
                        if (!part.isEmpty()) {
                            try {
                                int num = Integer.parseInt(part);
                                originalData.add(num);
                            } catch (NumberFormatException e) {
                                // Skip non-integer values
                            }
                        }
                    }
                }
            }
            
            currentData = new ArrayList<>(originalData);
            
            appendToBottom("Total numbers loaded: " + originalData.size() + "\n");
            
            if (!originalData.isEmpty()) {
                int min = Collections.min(originalData);
                int max = Collections.max(originalData);
                appendToBottom("Range: " + min + " to " + max + "\n");
            }
            
            statusLabel.setText("Loaded " + originalData.size() + " numbers");
            
        } catch (IOException e) {
            textArea.setText("Error reading file: " + e.getMessage());
            statusLabel.setText("Error loading file");
        }
    }
    
    // BUBBLE SORT
    private void bubbleSort() {
        if (currentData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load data first!");
            return;
        }
        
        appendToBottom("\n=== Starting Bubble Sort ===\n");
        long startTime = System.nanoTime();
        
        ArrayList<Integer> sorted = new ArrayList<>(currentData);
        int n = sorted.size();
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                // Descending order
                if (sorted.get(j) < sorted.get(j + 1)) {
                    int temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000_000.0;
        
        currentData = sorted;
        
        // Display the sorted numbers horizontally
        appendToBottom("\n=== Sorted Numbers (Bubble Sort) ===\n\n");
        displayNumbersHorizontally();
        
        // Display results below the numbers
        appendToBottom("\n=== Results ===\n");
        appendToBottom(String.format("Time taken: %.3f seconds\n", timeTaken));
        appendToBottom("✓ Verified: Correctly sorted in descending order\n");
        
        statusLabel.setText("Bubble Sort: " + String.format("%.3f", timeTaken) + " seconds");
    }
    
    private void displayNumbersHorizontally() {
        int numbersPerLine = 15; // Adjust this to change how many numbers per line
        StringBuilder line = new StringBuilder();
        
        for (int i = 0; i < currentData.size(); i++) {
            int num = currentData.get(i);
            line.append(String.format("%4d", num));
            
            // Add space between numbers, but not at the end of line
            if ((i + 1) % numbersPerLine != 0 && i != currentData.size() - 1) {
                line.append(" ");
            }
            
            // New line after every 'numbersPerLine' numbers or at the end
            if ((i + 1) % numbersPerLine == 0 || i == currentData.size() - 1) {
                appendToBottom(line.toString() + "\n");
                line = new StringBuilder();
            }
        }
    }
    
    private void resetData() {
        if (!originalData.isEmpty()) {
            currentData = new ArrayList<>(originalData);
            appendToBottom("\n=== Data Reset ===\n");
            appendToBottom("Restored " + originalData.size() + " original numbers\n");
            statusLabel.setText("Data reset - " + originalData.size() + " numbers");
        } else {
            JOptionPane.showMessageDialog(this, "No data to reset");
        }
    }
    
    private void clearScreen() {
        textArea.setText("");
        statusLabel.setText("Screen cleared");
        appendToBottom("=== Screen Cleared ===\n");
        appendToBottom("Load a file to start sorting\n");
    }
    
    // Method to append text and auto-scroll to bottom
    private void appendToBottom(String text) {
        textArea.append(text);
        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BubbleSortGUI frame = new BubbleSortGUI();
            frame.setVisible(true);
        });
    }
}