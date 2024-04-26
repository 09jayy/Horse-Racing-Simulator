package Part2;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

public class BetMenu extends JPanel implements TrackSettingsListener {
    private JLabel trackLengthLabel;
    private JLabel selectedHorsesLabel;
    private JPanel betPanel;
    private static String trackColour;
    private static JTextField[] betFields;
    private static Horse[] horsesInRace;
    private static int trackLength;
    private static List<BetMenuListener> listeners = new ArrayList<>();

    public BetMenu() {
        int money = getMoney();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        trackLengthLabel = new JLabel();
        add(trackLengthLabel);

        selectedHorsesLabel = new JLabel();
        add(selectedHorsesLabel);

        JLabel moneyLabel = new JLabel("Current Balance: " + Integer.toString(money));
        add(moneyLabel);

        betPanel = new JPanel();

        // Register BetMenu as a listener for trackLength changes
        TrackSettings.addTrackSettingsListener(this);

        // Transition buttons
        // Back and Next buttons
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.previous(getParent());
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.next(getParent());

            // notifyListeners
            notifyListeners();
        });

        // adding elements
        add(backButton);
        add(nextButton);
    }

    // Update the label when trackLength changes
    public void updateTrackAndHorses(int newTrackLength, List<Integer> newHorsesInRace, String newTrackColour) {
        trackColour = newTrackColour;
        trackLengthLabel.setText("Track Length: " + newTrackLength);
        selectedHorsesLabel.setText("Selected Horses: " + formatHorses(newHorsesInRace));
        trackLength = newTrackLength;
        horsesInRace = createHorses(newHorsesInRace);

        // get odds
        double[] odds = getOdds();
        double sigmaOdds = 0;

        for (double odd : odds) {
            sigmaOdds += odd;
        }

        for (int i = 0; i < odds.length; i++) {
            odds[i] = Math.round(odds[i] / sigmaOdds * 100);
        }

        // update betPanel
        betPanel.setLayout(new GridLayout(newHorsesInRace.size(), 3));
        add(betPanel);
        betFields = new JTextField[newHorsesInRace.size()];

        for (int i = 0; i < newHorsesInRace.size(); i++) {
            JLabel horseLabel = new JLabel("Horse " + newHorsesInRace.get(i));
            betPanel.add(horseLabel);
            JLabel oddsLabel = new JLabel("Odds: " + odds[i] + "%");
            betPanel.add(oddsLabel);
            betFields[i] = new JTextField();
            betPanel.add(betFields[i]);
        }
    }

    // create horses
    private Horse[] createHorses(List<Integer> selectedHorses) {
        int numHorses = selectedHorses.size();
        Horse[] horses = new Horse[numHorses];
        double[] confidence = getConfidenceFromFile(selectedHorses);

        for (int i = 0; i < numHorses; i++) {
            horses[i] = new Horse(selectedHorses.get(i).toString(),
                    "Part2/assets/horse-" + selectedHorses.get(i) + ".png", confidence[i]);
        }
        return horses;
    }

    private double[] getConfidenceFromFile(List<Integer> selectedHorses) {
        final String PATH = "Part2/data/horses.csv";
        double[] confidence = new double[selectedHorses.size()];
        int indexPointer = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line;

            while ((line = reader.readLine()) != null && indexPointer < selectedHorses.size()) {
                String[] horseData = line.split(",");

                if (horseData[0].equals(selectedHorses.get(indexPointer).toString())) {
                    confidence[indexPointer] = Double.parseDouble(horseData[2]);
                    indexPointer++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return confidence;
    }

    public static int getMoney() {
        int money = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("Part2/data/money.txt"))) {
            money = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return money;
    }

    public static void addBetMenuListener(BetMenuListener listener) {
        listeners.add(listener);
    }

    public static void notifyListeners() {
        for (BetMenuListener listener : listeners) {
            listener.startRace(trackLength, horsesInRace, betFields, trackColour);
        }
    }

    public String formatHorses(List<Integer> horses) {
        String formattedHorses = "";
        for (int i = 0; i < horses.size(); i++) {
            formattedHorses += "Horse " + horses.get(i);
            if (i < horses.size() - 1) {
                formattedHorses += ", ";
            }
        }
        return formattedHorses;
    }

    public double[] getOdds() {
        double[] odds = new double[horsesInRace.length];

        for (int i = 0; i < horsesInRace.length; i++) {

            try (BufferedReader reader = new BufferedReader(
                    new FileReader("Part2/data/history/" + horsesInRace[i].getName() + "_history.csv"))) {
                String line;
                int numWins = 0;
                int numRaces = 0;
                int allRaceLength = 0;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    numWins = (data[0].equals("1")) ? numWins + 1 : numWins;
                    numRaces++;
                    allRaceLength += Integer.parseInt(data[1]);
                }

                numWins = (numWins == 0) ? 1 : numWins;
                numRaces = (numRaces == 0) ? 1 : numRaces;
                allRaceLength = (allRaceLength == 0) ? 1 : allRaceLength;

                System.out
                        .println(horsesInRace[i].getName() + " " + numWins + " " + numRaces + " " + allRaceLength + " "
                                + horsesInRace[i].getConfidence());
                double odd = ((double) numWins / (double) numRaces) * ((double) allRaceLength / (double) numRaces)
                        * ((double) horsesInRace[i].getConfidence());
                odds[i] = odd;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return odds;
    }
}
