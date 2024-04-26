package Part2;

import java.util.List;

public interface TrackSettingsListener {
    void updateTrackAndHorses(int newTrackLength, List<Integer> newHorsesInRace);
}