/**
 * Write a description of class Horse here.
 * 
 * @author Jibril Hairulzihan
 * @version 1.0
 */
public class Horse {
    // Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;

    // Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
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

    public char getSymbol() {
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

    public void setSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }

    public static void main(String[] args) {
        Horse horse = new Horse('X', "Horse", 0.5);
        System.out.println("Constructor");
        System.out.println((horse instanceof Horse) ? "Horse object created" : "Horse object not created");

        System.out.println("\nAccessor methods");
        System.out.println("confidence: " + horse.getConfidence());
        System.out.println("distanceTravelled: " + horse.getDistanceTravelled());
        System.out.println("name: " + horse.getName());
        System.out.println("symbol: " + horse.getSymbol());
        System.out.println("hasFallen: " + horse.hasFallen());

        System.out.println("\nMutator methods");
        System.out.println("Move forward 1 step");
        horse.moveForward();
        System.out.println("distanceTravelled: " + horse.getDistanceTravelled());

        System.out.println("Fall");
        horse.fall();
        System.out.println(horse.hasFallen());

        System.out.println("Go back to start");
        horse.goBackToStart();
        System.out.println(horse.getDistanceTravelled());

        System.out.println("Set confidence to 0.7");
        horse.setConfidence(0.7);
        System.out.println(horse.getConfidence());

        System.out.println("Set symbol to 'Y'");
        horse.setSymbol('Y');
        System.out.println(horse.getSymbol());
    }
}