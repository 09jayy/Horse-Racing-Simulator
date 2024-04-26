package Part2;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.*;

public class Results extends JPanel implements HorseRaceWindowListener {
    private JLabel winnersLabel;
    private JLabel previousMoneyLabel;
    private JLabel newMoneyLabel;

    public Results() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel resultsLabel = new JLabel("Results");
        add(resultsLabel);

        winnersLabel = new JLabel("Winners: ...");
        add(winnersLabel);

        previousMoneyLabel = new JLabel("Previous Balance: " + BetMenu.getMoney());
        add(previousMoneyLabel);

        newMoneyLabel = new JLabel();
        add(newMoneyLabel);

        // quit button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(quitButton);

        // Register Results as a listener for race finish
        HorseRaceWindow.addHorseRaceWindowListener(this);
    }

    public void updateResults(List<Horse> winners, Horse[] horsesInRace, int trackLength, JTextField[] betFields) {
        // Update the results
        System.out.println("Results updated");

        // Update the winners label
        winnersLabel.setText("Winners: " + formatWinners(winners));

        // update confidence levels
        updateHorseCSV(winners, horsesInRace);

        updateHistoryCSV(winners, horsesInRace, trackLength);

        updateMoney(winners, horsesInRace, betFields);
    }

    public void updateHorseCSV(List<Horse> winners, Horse[] horsesInRace) {
        // paths
        final String inputFilePath = "Part2/data/horses.csv";
        final String tempFilePath = "Part2/data/temp_horses.csv";

        // write confidence levels to file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
            String line;
            int horsePointer = 0;

            while ((line = reader.readLine()) != null) {
                String[] dataSplit = line.split(",");

                if (horsePointer < horsesInRace.length && dataSplit[0].equals(horsesInRace[horsePointer].getName())) {
                    // Update the confidence level
                    writer.write(horsesInRace[horsePointer].getName() + "," + dataSplit[1] + "," +
                            horsesInRace[horsePointer].getConfidence() + "\n");
                    horsePointer++;
                } else {
                    // Write the existing line as it is
                    writer.write(line + "\n");
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateTempFile(inputFilePath, tempFilePath);
    }

    public void updateTempFile(String inputFilePath, String tempFilePath) {
        try {
            // Delete the original file
            File originalFile = new File(inputFilePath);
            if (originalFile.delete()) {
                System.out.println("Original file deleted successfully");
            } else {
                System.out.println("Failed to delete the original file");
            }

            // Rename the temp file to the original file
            File tempFile = new File(tempFilePath);
            if (tempFile.renameTo(originalFile)) {
                System.out.println("Temp file renamed successfully");
            } else {
                System.out.println("Failed to rename the temp file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHistroyPathInput(String name) {
        return "Part2/data/history/" + name + "_history.csv";
    }

    public String getHistoryPathTemp(String name) {
        return "Part2/data/history/temp_" + name + "_history.csv";
    }

    public void updateHistoryCSV(List<Horse> winners, Horse[] horsesInRace, int trackLength) {
        // paths
        for (Horse horse : horsesInRace) {
            final String inputFilePath = getHistroyPathInput(horse.getName());
            final String tempFilePath = getHistoryPathTemp(horse.getName());

            // write history to file
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
                writer.write(((winners.contains(horse)) ? "1," : "0,") + (trackLength + "\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateTempFile(inputFilePath, tempFilePath);
        }
    }

    public String formatWinners(List<Horse> winners) {
        String winnersString = "";
        for (Horse horse : winners) {
            winnersString += "Horse" + horse.getName() + " ";
        }
        return winnersString;
    }

    public void updateMoney(List<Horse> winners, Horse[] horsesInRace, JTextField[] betFields) {
        int balance = BetMenu.getMoney();

        for (int i = 0; i < horsesInRace.length; i++) {
            if (winners.contains(horsesInRace[i])) {
                balance += (!betFields[i].getText().isEmpty()) ? Integer.parseInt(betFields[i].getText()) : 0;
            } else {
                balance -= (!betFields[i].getText().isEmpty()) ? Integer.parseInt(betFields[i].getText()) : 0;
            }
        }
        System.out.println(balance);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Part2/data/money.txt"))) {
            writer.write(Integer.toString(balance));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        newMoneyLabel.setText("New Balance: " + balance);
    }
}
