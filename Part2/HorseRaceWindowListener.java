package Part2;

import java.util.List;
import javax.swing.JTextField;

public interface HorseRaceWindowListener {
    void updateResults(List<Horse> winners, Horse[] horsesInRace, int trackLength, JTextField[] betFields);
}
