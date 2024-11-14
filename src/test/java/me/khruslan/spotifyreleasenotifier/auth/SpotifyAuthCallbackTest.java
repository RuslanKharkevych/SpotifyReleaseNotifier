package me.khruslan.spotifyreleasenotifier.auth;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.telegram.TelegramConfig;
import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static me.khruslan.spotifyreleasenotifier.TestUtils.expectRunnable;
import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.*;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_BOT_URL;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_CHAT_ID;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.mockNewUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class SpotifyAuthCallbackTest extends EasyMockSupport {
    @Mock
    private TelegramConfig telegramConfig;

    @Mock
    private TelegramService telegramService;

    @Mock
    private SpotifyService spotifyService;

    @Mock
    private UserService userService;

    private SpotifyAuthCallback spotifyAuthCallback;

    @BeforeEach
    public void init() {
        spotifyAuthCallback =
                new SpotifyAuthCallback(CLOCK, telegramConfig, telegramService, spotifyService, userService);
    }

    @Test
    public void givenAuthCodeMissing_whenHandleRedirect_thenSendLoginErrorMessage() {
        expect(telegramConfig.getAbsoluteUrl()).andReturn(TELEGRAM_BOT_URL);
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, Messages.LOGIN_ERROR));
        replayAll();

        var redirectView = spotifyAuthCallback.handleRedirect(AUTH_STATE, null, AUTH_ERROR);
        assertThat(redirectView.getUrl()).isEqualTo(TELEGRAM_BOT_URL);
        verifyAll();
    }

    @Test
    public void givenAuthCodeInvalid_whenHandleRedirect_thenSendLoginErrorMessage() {
        expect(telegramConfig.getAbsoluteUrl()).andReturn(TELEGRAM_BOT_URL);
        expect(spotifyService.getAuthCredentials(AUTH_CODE)).andReturn(null);
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, Messages.LOGIN_ERROR));
        replayAll();

        var redirectView = spotifyAuthCallback.handleRedirect(AUTH_STATE, AUTH_CODE, null);
        assertThat(redirectView.getUrl()).isEqualTo(TELEGRAM_BOT_URL);
        verifyAll();
    }

    @Test
    public void givenAuthCodeValid_andUserSaveable_whenHandleRedirect_thenSendStatusLoggedInMessage() {
        expect(telegramConfig.getAbsoluteUrl()).andReturn(TELEGRAM_BOT_URL);
        expect(spotifyService.getAuthCredentials(AUTH_CODE)).andReturn(SPOTIFY_CREDENTIALS);
        expect(userService.createUser(mockNewUser())).andReturn(true);
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, Messages.STATUS_LOGGED_IN));
        replayAll();

        var redirectView = spotifyAuthCallback.handleRedirect(AUTH_STATE, AUTH_CODE, null);
        assertThat(redirectView.getUrl()).isEqualTo(TELEGRAM_BOT_URL);
        verifyAll();
    }

    @Test
    public void givenAuthCodeIsValid_andUserNotSaveable_whenHandleRedirect_thenSendInternalErrorMessage() {
        expect(telegramConfig.getAbsoluteUrl()).andReturn(TELEGRAM_BOT_URL);
        expect(spotifyService.getAuthCredentials(AUTH_CODE)).andReturn(SPOTIFY_CREDENTIALS);
        expect(userService.createUser(mockNewUser())).andReturn(false);
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, Messages.INTERNAL_ERROR));
        replayAll();

        var redirectView = spotifyAuthCallback.handleRedirect(AUTH_STATE, AUTH_CODE, null);
        assertThat(redirectView.getUrl()).isEqualTo(TELEGRAM_BOT_URL);
        verifyAll();
    }
}
