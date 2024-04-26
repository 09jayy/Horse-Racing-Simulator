import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom exception for invalid input values
 */
class ArguementOutofBounds extends Exception {
    public ArguementOutofBounds(String message) {
        super(message);
    }
}

/**
 * Custom exception for invalid assignment of a variable
 */
class InvalidAssignment extends Exception {
    public InvalidAssignment(String message) {
        super(message);
    }
}

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race {
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) throws ArguementOutofBounds {
        if (distance <= 0) {
            throw new ArguementOutofBounds("Distance must be >0");
        }

        // initialise instance variables
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse   the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) throws ArguementOutofBounds, InvalidAssignment {
        // check valid confidence arguement
        if (theHorse.getConfidence() <= 0 || theHorse.getConfidence() > 1) {
            throw new ArguementOutofBounds("Confidence level must be >0 and <=1");
        }

        // check valid lane number
        if (laneNumber > 3) {
            throw new ArguementOutofBounds("Lane number must be between 1 and 3 (inclusive)");
        }

        if (laneNumber == 1 && lane1Horse == null) {
            lane1Horse = theHorse;
        } else if (laneNumber == 2 && lane2Horse == null) {
            lane2Horse = theHorse;
        } else if (laneNumber == 3 && lane3Horse == null) {
            lane3Horse = theHorse;
        } else {
            throw new InvalidAssignment("Lane " + laneNumber + " already occupied");
        }
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {
        // declare a local variable to tell us when the race is finished
        boolean finished = false;
        List<Horse> winners = new ArrayList<>();

        // reset all the lanes (all horses not fallen and back to 0).
        if (lane1Horse != null) {
            lane1Horse.goBackToStart();
        }
        if (lane2Horse != null) {
            lane2Horse.goBackToStart();
        }
        if (lane3Horse != null) {
            lane3Horse.goBackToStart();
        }

        while (!finished) {
            // move each horse
            if (lane1Horse != null) {
                moveHorse(lane1Horse);
            }
            if (lane2Horse != null) {
                moveHorse(lane2Horse);
            }
            if (lane3Horse != null) {
                moveHorse(lane3Horse);
            }

            // set any of the three horses has won the race is finished
            winners = findWinner(winners);

            // if any of the three horses has won the race is finished
            if (!winners.isEmpty()
                    || (lane1Horse == null || lane1Horse.hasFallen()) && (lane2Horse == null || lane2Horse.hasFallen())
                            && (lane3Horse == null || lane3Horse.hasFallen())) {
                finished = true;
            }

            // print the race positions
            printRace();

            // wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
        }

        printWinner(winners);
    }

    /**
     * Print the winnder
     * 
     * @param winners the list of winners
     */

    private void printWinner(List<Horse> winners) {
        if (!winners.isEmpty()) {
            if (winners.size() > 1) {
                System.out.print("And the winners are ");
            } else {
                System.out.print("The winner is ");
            }
            for (Horse horse : winners) {
                System.out.print(horse.getName() + " ");
            }
        } else {
            System.out.println("No winner, all horses have fallen");
        }
    }

    /**
     * Find the winner
     * 
     * @param winners the list of winners
     * @return
     */
    private List<Horse> findWinner(List<Horse> winners) {
        if (lane1Horse != null && raceWonBy(lane1Horse)) {
            winners.add(lane1Horse);
        }
        if (lane2Horse != null && raceWonBy(lane2Horse)) {
            winners.add(lane2Horse);
        }
        if (lane3Horse != null && raceWonBy(lane3Horse)) {
            winners.add(lane3Horse);
        }

        return winners;
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse) {
        // if the horse has fallen it cannot move,
        // so only run if it has not fallen

        if (!theHorse.hasFallen()) {
            // the probability that the horse will fall is very small (max is 0.1)
            // but will also will depends exponentially on confidence
            // so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
                theHorse.setConfidence(
                        (theHorse.getConfidence() > 0.1) ? ((theHorse.getConfidence() * 10 - 1) / 10) : 0.1);
            }
        }

        if (!theHorse.hasFallen()) {
            // the probability that the horse will move forward depends on the confidence
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }
        }
    }

    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {
        if (theHorse.getDistanceTravelled() == raceLength) {
            theHorse.setConfidence((theHorse.getConfidence() < 1) ? (theHorse.getConfidence() * 10 + 1) / 10 : 1);
            return true;
        } else {
            return false;
        }
    }

    /***
     * Print the race on the terminal
     */
    private void printRace() {
        System.out.print("\033[H\033[2J"); // clear the terminal window

        multiplePrint('=', raceLength + 3); // top edge of track
        System.out.println();

        if (lane1Horse != null) {
            printLane(lane1Horse);
            System.out.println();
        }

        if (lane2Horse != null) {
            printLane(lane2Horse);
            System.out.println();
        }

        if (lane3Horse != null) {
            printLane(lane3Horse);
            System.out.println();
        }

        multiplePrint('=', raceLength + 3); // bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race
     * for example
     * | X |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse) {
        // calculate how many spaces are needed before
        // and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        // print a | for the beginning of the lane
        System.out.print('|');

        // print the spaces before the horse
        multiplePrint(' ', spacesBefore);

        // if the horse has fallen then print dead
        // else print the horse's symbol
        if (theHorse.hasFallen()) {
            System.out.print('\u2717');
        } else {
            System.out.print(theHorse.getSymbol());
        }

        // print the spaces after the horse
        multiplePrint(' ', spacesAfter);

        // print the | for the end of the track
        System.out.print('|');

        // print the confidence level of the horse
        System.out.print(" " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ") ");
    }

    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times) {
        int i = 0;
        while (i < times) {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    public static void main(String[] args)
            throws ArguementOutofBounds, InvalidAssignment {
        Race race = new Race(10);
        race.addHorse(new Horse('a', "PIPPI", 0.1), 1);
        race.addHorse(new Horse('b', "KOKOMO", 0.8), 2);
        // race.addHorse(new Horse('c', "Horsey", 0.9), 3);
        race.startRace();
    }
}