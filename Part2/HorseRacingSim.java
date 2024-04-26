package Part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HorseRacingSim extends JFrame {
    private static HorseRaceWindow raceWindow;

    public HorseRacingSim() {
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
            HorseRacingSim horseRace = new HorseRacingSim();
            horseRace.setVisible(true);
        });
    }
}