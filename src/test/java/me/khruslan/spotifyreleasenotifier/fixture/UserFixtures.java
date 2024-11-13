package me.khruslan.spotifyreleasenotifier.fixture;

import me.khruslan.spotifyreleasenotifier.user.builder.UserBuilder;
import me.khruslan.spotifyreleasenotifier.user.builder.UserDtoBuilder;
import me.khruslan.spotifyreleasenotifier.user.model.User;
import me.khruslan.spotifyreleasenotifier.user.model.UserDto;

import java.util.List;

import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK;
import static me.khruslan.spotifyreleasenotifier.fixture.ReleaseFixtures.*;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.*;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.*;

public class UserFixtures {
    public static final long USER_ID = 1L;

    public static final UserDto USER_DTO = new UserDtoBuilder()
            .setId(USER_ID)
            .setTelegramId(TELEGRAM_USER_ID)
            .setTelegramChatId(TELEGRAM_CHAT_ID)
            .setSpotifyAccessToken(ACCESS_TOKEN)
            .setSpotifyRefreshToken(REFRESH_TOKEN)
            .setSpotifyTokenExpirationTimestamp(TOKEN_EXPIRATION_TIMESTAMP)
            .setReleaseHistoryDate(RELEASE_HISTORY_DATE.toString())
            .setReleases(List.of(RELEASE_DTO))
            .build();

    public static User mockNewUser() {
        return new UserBuilder(CLOCK)
                .setTelegramCredentials(TELEGRAM_CREDENTIALS)
                .setSpotifyCredentials(SPOTIFY_CREDENTIALS)
                .build();
    }

    public static User mockUser() {
        var user = mockNewUser();
        user.setId(USER_ID);
        return user;
    }

    public static User mockUserWithRefreshedToken() {
        var user = mockUser();
        user.setSpotifyCredentials(REFRESHED_SPOTIFY_CREDENTIALS);
        return user;
    }

    public static User mockUserWithReleaseHistory() {
        var user = mockUser();
        user.setReleaseHistory(RELEASE_HISTORY);
        return user;
    }

    public static User mockUserWithNewReleaseHistory() {
        var user = mockUser();
        user.setReleaseHistory(NEW_RELEASE_HISTORY);
        return user;
    }
}