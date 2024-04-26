package Part2;

import javax.swing.*;
import java.awt.*;

public class startRaceGUI extends JFrame {
    private static HorseRaceWindow raceWindow;

    public startRaceGUI() {
        setTitle("Horse Race Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        setLayout(new CardLayout());
        add(new StartMenu());
        add(new TrackSettings());
        add(new BetMenu());
        add(new Results());

        raceWindow = new HorseRaceWindow();
    }

    public static HorseRaceWindow getRaceWindow() {
        return raceWindow;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            startRaceGUI horseRace = new startRaceGUI();
            horseRace.setVisible(true);
        });
    }
}