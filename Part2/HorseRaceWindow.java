package Part2;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HorseRaceWindow extends JFrame implements BetMenuListener {
    private Horse[] horsesInRace;
    private int trackLength;

    public HorseRaceWindow() {
        setTitle("Horse Race Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);

        // Register HorseRaceWindow as a listener for selectedHorses changes
        BetMenu.addBetMenuListener(this);
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

    public void startRace(int trackLength, Horse[] horsesInRace) {
        HorseRacingSim.raceWindow.setVisible(true);

        System.out.println(trackLength);
        for (Horse horse : horsesInRace) {
            System.out.println(horse.toString());
        }
    }
}
