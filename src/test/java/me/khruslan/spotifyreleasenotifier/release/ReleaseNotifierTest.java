package me.khruslan.spotifyreleasenotifier.release;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static me.khruslan.spotifyreleasenotifier.TestUtils.expectRunnable;
import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK_AFTER_TOKEN_EXPIRATION;
import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK_BEFORE_TOKEN_EXPIRATION;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.*;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_CHAT_ID;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.*;
import static org.easymock.EasyMock.*;

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
    public void givenAccessTokenExpired_andRefreshTokenInvalid_whenCheckForNewReleases_thenProceedWithExpiredToken() {
        init(CLOCK_AFTER_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expectGetAlbumsFromFollowedArtistsAndReturn(new ArrayList<>());
        expect(spotifyService.refreshAccessToken(REFRESH_TOKEN)).andReturn(null);
        expect(userService.updateUser(mockUser())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenExpired_andRefreshTokenValid_whenCheckForNewReleases_thenUpdateCredentials() {
        init(CLOCK_AFTER_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expect(spotifyService.refreshAccessToken(REFRESH_TOKEN)).andReturn(REFRESHED_SPOTIFY_CREDENTIALS);
        expectGetAlbumsFromFollowedArtistsAndReturn(new ArrayList<>());
        expect(userService.updateUser(mockUserWithRefreshedToken())).andStubReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseNotFound_whenCheckForNewReleases_thenDoNotSendAnyMessages() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expectGetAlbumsFromFollowedArtistsAndReturn(List.of(OLD_ALBUM));
        expect(userService.updateUser(mockUser())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseFound_whenCheckForNewReleases_thenSendMessageWithReleaseUrl() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expectGetAlbumsFromFollowedArtistsAndReturn(List.of(NEW_ALBUM));
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, NEW_ALBUM_URL));
        expect(userService.updateUser(mockUserWithNewReleaseHistory())).andStubReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseFound_whenCheckForNewReleases_thenUpdateReleaseHistory() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUserWithReleaseHistory()));
        expectGetAlbumsFromFollowedArtistsAndReturn(List.of(NEW_ALBUM));
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, NEW_ALBUM_URL));
        expect(userService.updateUser(mockUserWithTransitionalReleaseHistory())).andReturn(true);
        expect(userService.updateUser(mockUserWithNewReleaseHistory())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseAlreadyNotified_whenCheckForNewReleases_thenDoNotSendAnyMessages() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUserWithReleaseHistory()));
        expectGetAlbumsFromFollowedArtistsAndReturn(List.of(OLD_ALBUM));
        expect(userService.updateUser(mockUser())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    @Test
    public void givenAccessTokenValid_andNewReleaseDateUnknown_whenCheckForNewReleases_thenDoNotSendAnyMessages() {
        init(CLOCK_BEFORE_TOKEN_EXPIRATION);

        expect(userService.getAllUsers()).andReturn(List.of(mockUser()));
        expectGetAlbumsFromFollowedArtistsAndReturn(List.of(ALBUM_WITH_UNKNOWN_DATE));
        expect(userService.updateUser(mockUser())).andReturn(true);
        replayAll();

        releaseNotifier.checkForNewReleases();
        verifyAll();
    }

    private void expectGetAlbumsFromFollowedArtistsAndReturn(List<AlbumSimplified> albums) {
        Capture<Supplier<String>> accessTokenSupplier = Capture.newInstance();
        Capture<Consumer<List<AlbumSimplified>>> albumsConsumer = Capture.newInstance();
        spotifyService.getAlbumsFromFollowedArtists(capture(accessTokenSupplier), capture(albumsConsumer));

        expectLastCall().andAnswer(() -> {
            accessTokenSupplier.getValue().get();
            albumsConsumer.getValue().accept(albums);
            return null;
        });
    }
}
