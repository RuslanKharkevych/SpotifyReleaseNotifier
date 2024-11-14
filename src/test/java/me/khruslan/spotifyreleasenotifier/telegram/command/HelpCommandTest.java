package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelpCommandTest {
    @Test
    public void givenHelpCommand_whenExecute_thenReturnCommandsListMessage() {
        assertThat(new HelpCommand().execute()).isEqualTo(Messages.COMMANDS_LIST);
    }
}
