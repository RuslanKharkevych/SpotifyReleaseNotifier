package me.khruslan.spotifyreleasenotifier.release.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ReleaseHistory(LocalDate date, List<Release> releases) {
    public ReleaseHistory(List<Release> releases) {
        this(LocalDate.now(), releases);
    }

    public ReleaseHistory() {
        this(new ArrayList<>());
    }
}
