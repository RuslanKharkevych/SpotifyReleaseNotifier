package me.khruslan.spotifyreleasenotifier.telegram.command;

import org.junit.jupiter.api.Test;

import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.UNKNOWN_COMMAND;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.UNRECOGNIZED_COMMAND_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class UnknownCommandTest {
    @Test
    public void givenUnknownCommand_whenExecute_thenReturnUnrecognizedCommandMessage() {
        assertThat(UNKNOWN_COMMAND.execute()).isEqualTo(UNRECOGNIZED_COMMAND_MESSAGE);
    }
}
