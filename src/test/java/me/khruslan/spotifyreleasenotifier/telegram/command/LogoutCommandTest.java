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
public class LogoutCommandTest extends EasyMockSupport {
    @Mock
    private UserService userService;

    private LogoutCommand logoutCommand;

    @BeforeEach
    public void init() {
        logoutCommand = new LogoutCommand(userService, TELEGRAM_CREDENTIALS);
    }

    @Test
    public void givenUserDoesNotExist_whenExecute_thenReturnLogoutBadStateMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(false);
        replayAll();

        assertThat(logoutCommand.execute()).isEqualTo(Messages.LOGOUT_BAD_STATE);
        verifyAll();
    }

    @Test
    public void givenUserExists_andDeletable_whenExecute_thenReturnLogoutSuccessMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(true);
        expect(userService.deleteUser(TELEGRAM_USER_ID)).andReturn(true);
        replayAll();

        assertThat(logoutCommand.execute()).isEqualTo(Messages.LOGOUT_SUCCESS);
        verifyAll();
    }

    @Test
    public void givenUserExists_andNotDeletable_whenExecute_thenReturnInternalErrorMessage() {
        expect(userService.userExists(TELEGRAM_USER_ID)).andReturn(true);
        expect(userService.deleteUser(TELEGRAM_USER_ID)).andReturn(false);
        replayAll();

        assertThat(logoutCommand.execute()).isEqualTo(Messages.INTERNAL_ERROR);
        verifyAll();
    }
}
