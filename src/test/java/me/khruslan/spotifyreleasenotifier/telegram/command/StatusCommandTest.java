package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_CREDENTIALS;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class StatusCommandTest extends EasyMockSupport {
    @Mock
    private UserService userService;

    private StatusCommand statusCommand;

    @BeforeEach
    public void init() {
        statusCommand = new StatusCommand(userService, TELEGRAM_CREDENTIALS);
    }

    @Test
    public void givenUserExists_whenExecute_thenReturnStatusLoggedInMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(true);
        replayAll();

        assertThat(statusCommand.execute()).isEqualTo(Messages.STATUS_LOGGED_IN);
        verifyAll();
    }

    @Test
    public void givenUserDoesNotExist_whenExecute_thenReturnStatusLoggedOutMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(false);
        replayAll();

        assertThat(statusCommand.execute()).isEqualTo(Messages.STATUS_LOGGED_OUT);
        verifyAll();
    }
}
