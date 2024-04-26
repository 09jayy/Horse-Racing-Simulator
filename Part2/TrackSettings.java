package Part2;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

public class TrackSettings extends JPanel {
    private static JSlider trackLengthSlider;
    private static JCheckBox[] selectedHorses;
    private static List<TrackSettingsListener> listeners = new ArrayList<>();

    public TrackSettings() {
        final int NUM_HORSES = 8;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Select length of track
        trackLengthSlider = new JSlider();
        trackLengthSlider.setMinimum(1);
        trackLengthSlider.setMaximum(20);
        trackLengthSlider.setValue(5);
        trackLengthSlider.setMajorTickSpacing(1);
        trackLengthSlider.setSnapToTicks(true);

        // listener for track length select slider
        trackLengthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    JLabel label = (JLabel) getComponent(1);
                    label.setText("Length of Track: " + source.getValue());
                }
            }

        });

        // track length slider value indicator
        JLabel trackLengthLabel = new JLabel("Length of Track: " + trackLengthSlider.getValue());

        // Select horses to bet on
        selectedHorses = new JCheckBox[NUM_HORSES];
        for (int i = 0; i < NUM_HORSES; i++) {
            selectedHorses[i] = new JCheckBox("Horse " + (i + 1));
        }

        // Back and Next buttons
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.previous(getParent());
            }
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.next(getParent());
                notifyListeners();
            }
        });

        // adding elements
        add(trackLengthSlider);
        add(trackLengthLabel);

        for (int i = 0; i < NUM_HORSES; i++) {
            add(selectedHorses[i]);
        }

        add(backButton);
        add(nextButton);
    }

    // Getter method for trackLengthSlider
    public static int getTrackLength() {
        return trackLengthSlider.getValue();
    }

    // Getter method for selectedHorses
    public static JCheckBox[] getSelectedHorses() {
        return selectedHorses;
    }

    // Register a listener to be notified when trackLength changes
    public static void addTrackSettingsListener(TrackSettingsListener listener) {
        listeners.add(listener);
    }

    // Remove a registered listener
    public static void removeTrackSettingsListener(TrackSettingsListener listener) {
        listeners.remove(listener);
    }

    // Notify all registered listeners when trackLength changes
    private static void notifyListeners() {
        for (TrackSettingsListener listener : listeners) {
            List<Integer> selectedHorsesList = new ArrayList<>();
            for (int i = 0; i < selectedHorses.length; i++) {
                if (selectedHorses[i].isSelected()) {
                    selectedHorsesList.add(i + 1);
                }
            }

            listener.updateTrackAndHorses(trackLengthSlider.getValue(), selectedHorsesList);
        }
    }
}
