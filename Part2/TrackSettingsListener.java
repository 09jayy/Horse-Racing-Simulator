package Part2;

import java.util.List;

public interface TrackSettingsListener {
    void trackLengthChanged(int newTrackLength);

    void selectedHorsesChanged(List<Integer> selectedHorses);
}