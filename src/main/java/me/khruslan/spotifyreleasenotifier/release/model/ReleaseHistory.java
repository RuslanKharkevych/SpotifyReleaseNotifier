package me.khruslan.spotifyreleasenotifier.release.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ReleaseHistory(LocalDate date, List<Release> releases) {
    public ReleaseHistory(LocalDate date) {
        this(date, new ArrayList<>());
    }

    public ReleaseHistory copyWith(List<Release> releases) {
        return new ReleaseHistory(date, releases);
    }
}
