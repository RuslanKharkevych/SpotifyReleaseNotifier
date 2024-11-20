package me.khruslan.spotifyreleasenotifier.fixture;

import me.khruslan.spotifyreleasenotifier.release.builder.ReleaseBuilder;
import me.khruslan.spotifyreleasenotifier.release.builder.ReleaseDtoBuilder;
import me.khruslan.spotifyreleasenotifier.release.model.Release;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseDto;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistory;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistoryDto;

import java.time.LocalDate;
import java.util.List;

import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.NEW_ALBUM;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.OLD_ALBUM;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.USER_ID;

public class ReleaseFixtures {
    private static final Release RELEASE = new ReleaseBuilder()
            .setId(1L)
            .setUserId(USER_ID)
            .setAlbumId(OLD_ALBUM.getId())
            .build();

    public static final ReleaseDto RELEASE_DTO = new ReleaseDtoBuilder()
            .setId(1L)
            .setUserId(USER_ID)
            .setAlbumId(OLD_ALBUM.getId())
            .build();

    public static final LocalDate RELEASE_HISTORY_DATE = LocalDate.now(CLOCK).minusDays(1);

    public static final ReleaseHistory RELEASE_HISTORY =
            new ReleaseHistory(RELEASE_HISTORY_DATE, List.of(RELEASE));

    public static final ReleaseHistoryDto RELEASE_HISTORY_DTO =
            new ReleaseHistoryDto(RELEASE_HISTORY_DATE.toString(), List.of(RELEASE_DTO));

    private static final Release NEW_RELEASE = new ReleaseBuilder()
            .setUserId(USER_ID)
            .setAlbumId(NEW_ALBUM.getId())
            .build();

    public static final ReleaseHistory NEW_RELEASE_HISTORY =
            new ReleaseHistory(LocalDate.now(CLOCK), List.of(NEW_RELEASE));

    public static final ReleaseHistory TRANSITIONAL_RELEASE_HISTORY =
            new ReleaseHistory(RELEASE_HISTORY_DATE, List.of(NEW_RELEASE, RELEASE));

    public static final ReleaseHistoryDto RELEASE_HISTORY_DTO_WITH_INVALID_DATE =
            new ReleaseHistoryDto("", List.of(RELEASE_DTO));

    public static final ReleaseHistory RELEASE_HISTORY_WITH_CURRENT_DATE =
            new ReleaseHistory(LocalDate.now(CLOCK), List.of(RELEASE));
}
