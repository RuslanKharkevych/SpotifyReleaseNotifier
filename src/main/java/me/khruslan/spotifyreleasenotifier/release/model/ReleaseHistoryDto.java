package me.khruslan.spotifyreleasenotifier.release.model;

import java.util.List;

public record ReleaseHistoryDto(String date, List<ReleaseDto> releases) {
}
