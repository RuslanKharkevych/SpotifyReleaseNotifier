package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_CREDENTIALS;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.UNKNOWN_COMMAND_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EasyMockExtension.class)
public class CommandFactoryTest extends EasyMockSupport {
    private CommandFactory commandFactory;

    @BeforeEach
    public void init() {
        UserService userService = mock(UserService.class);
        SpotifyService spotifyService = mock(SpotifyService.class);
        commandFactory = new CommandFactory(userService, spotifyService);
    }

    @Test
    public void givenStartCommandName_whenCreate_createStartCommand() {
        var command = commandFactory.create(StartCommand.NAME, TELEGRAM_CREDENTIALS);
        assertThat(command).isInstanceOf(StartCommand.class);
    }

    @Test
    public void givenLoginCommandName_whenCreate_createLoginCommand() {
        var command = commandFactory.create(LoginCommand.NAME, TELEGRAM_CREDENTIALS);
        assertThat(command).isInstanceOf(LoginCommand.class);
    }

    @Test
    public void givenLogoutCommandName_whenCreate_createLogoutCommand() {
        var command = commandFactory.create(LogoutCommand.NAME, TELEGRAM_CREDENTIALS);
        assertThat(command).isInstanceOf(LogoutCommand.class);
    }

    @Test
    public void givenUnknownCommandName_whenCreate_createUnknownCommand() {
        var command = commandFactory.create(UNKNOWN_COMMAND_NAME, TELEGRAM_CREDENTIALS);
        assertThat(command).isInstanceOf(UnknownCommand.class);
    }
}
