package Part2;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.event.ChangeListener;

public class BetMenu extends JPanel implements TrackSettingsListener {
    private JLabel trackLengthLabel;
    private JLabel selectedHorsesLabel;
    private static Horse[] horsesInRace;
    private static int trackLength;
    private static List<BetMenuListener> listeners = new ArrayList<>();

    public BetMenu() {
        int money = getMoney();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel trackLength = new JLabel(Integer.toString(TrackSettings.getTrackLength()));
        add(trackLength);

        JLabel selectedHorses = new JLabel(TrackSettings.getSelectedHorses().toString());
        add(selectedHorses);

        System.out.println(TrackSettings.getSelectedHorses()[0]);

        JLabel moneyLabel = new JLabel(Integer.toString(money));
        add(moneyLabel);

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
    public void trackLengthChanged(int newTrackLength) {
        trackLength = newTrackLength;
        trackLengthLabel = (JLabel) getComponent(0);
        trackLengthLabel.setText(Integer.toString(newTrackLength));
    }

    // Update the label when selectedHorses changes
    public void selectedHorsesChanged(List<Integer> newSelectedHorses) {
        selectedHorsesLabel = (JLabel) getComponent(1);
        selectedHorsesLabel.setText(newSelectedHorses.toString());
        horsesInRace = createHorses(newSelectedHorses);

        for (Horse horse : horsesInRace) {
            System.out.println(horse.toString());
        }
    }

    // create horses
    private Horse[] createHorses(List<Integer> selectedHorses) {
        int numHorses = selectedHorses.size();
        Horse[] horses = new Horse[numHorses];
        double[] confidence = getConfidenceFromFile(selectedHorses);

        System.out.println((TrackSettings.getSelectedHorses().toString()));

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
                System.out.println(horseData.toString());

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

    private int getMoney() {
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
        System.out.println("Notifying listeners");
        for (BetMenuListener listener : listeners) {
            listener.startRace(trackLength, horsesInRace);
        }
    }
}
