package me.khruslan.spotifyreleasenotifier.release;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import static me.khruslan.spotifyreleasenotifier.TestUtils.expectRunnable;
import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK_AFTER_TOKEN_EXPIRATION;
import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK_BEFORE_TOKEN_EXPIRATION;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.*;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_CHAT_ID;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.*;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class ReleaseNotifierTest extends EasyMockSupport {
    @Mock
    private UserService userService;

    @Mock
    private SpotifyService spotifyService;

    @Mock
    private TelegramService telegramService;

    private ReleaseNotifier releaseNotifier;

    private void init(Clock clock) {
        releaseNotifier = new ReleaseNotifier(clock, userService, spotifyService, telegramService);
    }

    @Test
    public void givenAccessTokenExpired_andRefreshTokenInvalid_whenCheckForNewReleases_thenSkipUser() {
        init(CLOCK_AFTER_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expect(spotifyService.refreshAccessToken(REFRESH_TOKEN)).andReturn(null);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenExpired_andRefreshTokenValid_whenCheckForNewReleases_thenUpdateCredentials() {
        init(CLOCK_AFTER_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expect(spotifyService.refreshAccessToken(REFRESH_TOKEN)).andReturn(REFRESHED_SPOTIFY_CREDENTIALS);
        expect(spotifyService.getAlbumsFromFollowedArtists(REFRESHED_ACCESS_TOKEN)).andReturn(new ArrayList<>());
        expect(userService.updateUser(mockUserWithRefreshedToken())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseNotFound_whenCheckForNewReleases_thenDoNotSendAnyMessages() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expect(spotifyService.getAlbumsFromFollowedArtists(ACCESS_TOKEN)).andReturn(List.of(OLD_ALBUM));
        expect(userService.updateUser(mockUser())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseFound_whenCheckForNewReleases_thenSendMessageWithReleaseUrl() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expect(spotifyService.getAlbumsFromFollowedArtists(ACCESS_TOKEN)).andReturn(List.of(NEW_ALBUM));
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, NEW_ALBUM_URL));
        expect(userService.updateUser(mockUserWithNewReleaseHistory())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseFound_whenCheckForNewReleases_thenUpdateReleaseHistory() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUserWithReleaseHistory()));
        expect(spotifyService.getAlbumsFromFollowedArtists(ACCESS_TOKEN)).andReturn(List.of(NEW_ALBUM));
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, NEW_ALBUM_URL));
        expect(userService.updateUser(mockUserWithNewReleaseHistory())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseAlreadyNotified_whenCheckForNewReleases_thenDoNotSendAnyMessages() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUserWithReleaseHistory()));
        expect(spotifyService.getAlbumsFromFollowedArtists(ACCESS_TOKEN)).andReturn(List.of(OLD_ALBUM));
        expect(userService.updateUser(mockUserWithReleaseHistory())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseDateUnknown_whenCheckForNewReleases_thenDoNotSendAnyMessages() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expect(spotifyService.getAlbumsFromFollowedArtists(ACCESS_TOKEN)).andReturn(List.of(ALBUM_WITH_UNKNOWN_DATE));
        expect(userService.updateUser(mockUser())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }
}
