package Part2;

/**
 * Write a description of class Horse here.
 * 
 * @author Jibril Hairulzihan
 * @version 1.0
 */
public class Horse {
    // Fields of class Horse
    private String name;
    private String symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;

    // Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(String horseName, String horseSymbol, double horseConfidence) {
        this.name = horseName;
        this.symbol = horseSymbol;
        this.distanceTravelled = 0;
        this.hasFallen = false;
        this.confidence = horseConfidence;
    }

    // Other methods of class Horse
    public void fall() {
        hasFallen = true;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public int getDistanceTravelled() {
        return this.distanceTravelled;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void goBackToStart() {
        this.distanceTravelled = 0;
    }

    public boolean hasFallen() {
        return this.hasFallen;
    }

    public void moveForward() {
        this.distanceTravelled++;
    }

    public void setConfidence(double newConfidence) {
        this.confidence = newConfidence;
    }

    public void setSymbol(String newSymbol) {
        this.symbol = newSymbol;
    }

    @Override
    public String toString() {
        return "Horse{name='" + name + "', symbol='" + symbol + "', distanceTravelled=" + distanceTravelled
                + ", hasFallen=" + hasFallen + ", confidence=" + confidence + "}";
    }
}