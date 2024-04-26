package Part2;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HorseRaceWindow extends JFrame implements BetMenuListener {
    private JLabel[] horseLabels;
    private JPanel racePanel;
    private JPanel finishPanel;
    private JLabel finishLabel;
    private JPanel confidencePanel;
    private JLabel[] confidenceLabels;
    private List<Horse> winners;
    private static List<HorseRaceWindowListener> listeners = new ArrayList<>();

    public HorseRaceWindow() {
        setTitle("Horse Race Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Register HorseRaceWindow as a listener for selectedHorses changes
        BetMenu.addBetMenuListener(this);
    }

    public void createWindowElements(int trackLength, Horse[] horsesInRace, String trackColour) {
        JPanel titlePanel = new JPanel(null);
        racePanel = new JPanel(null);
        racePanel.setBackground(setColour(trackColour));
        racePanel.setSize(trackLength, horsesInRace.length);

        JLabel title = new JLabel("HORSE RACE SIMULATOR");

        // adding elements
        title.setBounds(0, 0, 200, 20); // Set bounds for position and size
        titlePanel.add(title);

        racePanel.setBounds(0, 20, (trackLength + 1) * 72, horsesInRace.length * 90);

        finishPanel = new JPanel(null);
        finishPanel.setBounds(0, horsesInRace.length * 90 + 10, ((trackLength + 1) * 72) + 200, 30);
        finishPanel.setBackground(Color.YELLOW);

        confidenceLabels = new JLabel[horsesInRace.length];
        confidencePanel = new JPanel(null);
        confidencePanel.setBounds(((trackLength + 1) * 72) + 10, 20, 200, horsesInRace.length * 90);
        confidencePanel.setBackground(Color.LIGHT_GRAY);
        for (int i = 0; i < horsesInRace.length; i++) {
            confidenceLabels[i] = new JLabel(
                    "Horse" + horsesInRace[i].getName() + " confidence: " + horsesInRace[i].getConfidence());
            confidenceLabels[i].setBounds(0, i * 90, 200, 20);
            confidencePanel.add(confidenceLabels[i]);
        }

        add(confidencePanel);
        add(finishPanel);
        add(racePanel);
        add(titlePanel);
    }

    private Color setColour(String trackColour) {
        switch (trackColour) {
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            case "Red":
                return Color.RED;
            default:
                return Color.GREEN;
        }
    }

    public void addHorses(JPanel racePanel, Horse[] horsesInRace) {
        horseLabels = new JLabel[horsesInRace.length];
        for (int i = 0; i < horsesInRace.length; i++) {
            ImageIcon horseIcon = new ImageIcon(horsesInRace[i].getSymbol());
            horseLabels[i] = new JLabel(horseIcon);
            horseLabels[i].setBounds(0, i * 90, 72, 47);
            racePanel.add(horseLabels[i]);
        }
    }

    public void startRace(int trackLength, Horse[] horsesInRace, JTextField[] betFields, String trackColour) {
        winners = new ArrayList<>();

        setSize(((trackLength + 1) * 72) + 200, horsesInRace.length * 90 + 100); // Set size of window
        HorseRacingSim.getRaceWindow().setVisible(true);
        createWindowElements(trackLength, horsesInRace, trackColour);
        addHorses(racePanel, horsesInRace);

        int delay = 100; // Delay in ms
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean finished = false;
                boolean fallen;
                int fallenCount = 0;

                // move each horse and update its position
                for (int i = 0; i < horsesInRace.length; i++) {
                    fallen = moveHorse(horsesInRace[i]);
                    if (!fallen) {
                        horseLabels[i].setBounds(horsesInRace[i].getDistanceTravelled() * 72, i * 90, 72, 47);
                    } else {
                        ImageIcon fallenIcon = new ImageIcon("Part2/assets/fallen.png");
                        horseLabels[i].setIcon(fallenIcon);
                        confidenceLabels[i].setText(
                                "Horse" + horsesInRace[i].getName() + " confidence: "
                                        + horsesInRace[i].getConfidence());
                    }
                }

                // check if all horses have fallen
                for (Horse horse : horsesInRace) {
                    if (horse.hasFallen()) {
                        fallenCount++;
                    }
                }

                // check if horses have finished
                for (int i = 0; i < horsesInRace.length; i++) {
                    if (horsesInRace[i].getDistanceTravelled() >= trackLength) {
                        horsesInRace[i].setConfidence(
                                (horsesInRace[i].getConfidence() < 1) ? (horsesInRace[i].getConfidence() * 10 + 1) / 10
                                        : 1);
                        winners.add(horsesInRace[i]);
                        confidenceLabels[i].setText(
                                "Horse" + horsesInRace[i].getName() + " confidence: "
                                        + horsesInRace[i].getConfidence());
                        finished = true;
                    }
                }

                if (finished || fallenCount == horsesInRace.length) {
                    ((Timer) e.getSource()).stop(); // Stop the timer when the race is finished
                    System.out.println(winners.toString());

                    if (fallenCount == horsesInRace.length) {
                        finishLabel = new JLabel("All horses have fallen!");
                    } else if (winners.size() == 1) {
                        finishLabel = new JLabel("And the winner is " + winnersString(winners));
                    } else {
                        finishLabel = new JLabel("And the winners are " + winnersString(winners));
                    }

                    finishLabel.setBounds(10, 0, 800, 30);
                    finishPanel.add(finishLabel);
                    finishPanel.revalidate();
                    finishPanel.repaint();

                    // notifyListeners
                    for (HorseRaceWindowListener listener : listeners) {
                        listener.updateResults(winners, horsesInRace, trackLength, betFields);
                    }
                }
            }
        });

        // Start the timer
        timer.start();
    }

    private boolean moveHorse(Horse theHorse) {
        // if the horse has fallen it cannot move,
        // so only run if it has not fallen

        if (!theHorse.hasFallen()) {
            // the probability that the horse will fall is very small (max is 0.1)
            // but will also will depends exponentially on confidence
            // so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
                System.out.println(theHorse.toString() + " has fallen!");
                theHorse.setConfidence(
                        (theHorse.getConfidence() > 0.1) ? ((theHorse.getConfidence() * 10 - 1) / 10) : 0.1);
                return true;
            }
        }

        if (!theHorse.hasFallen()) {
            // the probability that the horse will move forward depends on the confidence
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }
        }
        return false;
    }

    private String winnersString(List<Horse> winners) {
        String winnersString = "";
        for (Horse horse : winners) {
            winnersString += "Horse" + horse.getName() + " ";
        }
        return winnersString;
    }

    public static void addHorseRaceWindowListener(HorseRaceWindowListener listener) {
        listeners.add(listener);
    }
}
