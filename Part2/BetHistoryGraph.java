package Part2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BetHistoryGraph extends JPanel {
    private int[] data; // Sample data for the graph

    public BetHistoryGraph(List<Integer> betHistory) {
        data = new int[betHistory.size()];

        for (int i = 0; i < betHistory.size(); i++) {
            data[i] = betHistory.get(i);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define graph area
        int padding = 30;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        // Draw x-axis and y-axis
        g2d.drawLine(padding, padding + height, padding + width, padding + height); // x-axis
        g2d.drawLine(padding, padding, padding, padding + height); // y-axis

        // Calculate scaling factors
        int maxValue = getMaxValue(data);
        double xScale = (double) width / (data.length - 1);
        double yScale = (double) height / maxValue;

        // Plot data points
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < data.length - 1; i++) {
            int x1 = (int) (i * xScale) + padding;
            int y1 = (int) ((maxValue - data[i]) * yScale) + padding;
            int x2 = (int) ((i + 1) * xScale) + padding;
            int y2 = (int) ((maxValue - data[i + 1]) * yScale) + padding;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    private int getMaxValue(int[] data) {
        int max = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
