package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.AUTH_STATE;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.AUTH_URL;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class LoginCommandTest extends EasyMockSupport {
    @Mock
    private UserService userService;

    @Mock
    private SpotifyService spotifyService;

    private LoginCommand loginCommand;

    @BeforeEach
    public void init() {
        loginCommand = new LoginCommand(userService, spotifyService, TELEGRAM_CREDENTIALS);
    }

    @Test
    public void givenUserExists_whenExecute_thenReturnLoginBadStateMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(true);
        replayAll();

        assertThat(loginCommand.execute()).isEqualTo(Messages.LOGIN_BAD_STATE);
        verifyAll();
    }

    @Test
    public void givenUserDoesNotExist_whenExecute_thenReturnLoginUrlMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(false);
        expect(spotifyService.getAuthUrl(AUTH_STATE)).andReturn(AUTH_URL);
        replayAll();

        assertThat(loginCommand.execute()).isEqualTo(LOGIN_URL_MESSAGE);
        verifyAll();
    }
}