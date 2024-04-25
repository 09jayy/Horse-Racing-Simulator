package Part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JPanel {
    public StartMenu() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.next(getParent());
            }
        });

        // quit button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // adding elements
        add(startButton);
        add(quitButton);
    }
}