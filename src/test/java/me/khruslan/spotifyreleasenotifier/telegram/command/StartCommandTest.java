package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StartCommandTest {
    @Test
    public void givenStartCommand_whenExecute_thenReturnWelcomeMessage() {
        assertThat(new StartCommand().execute()).isEqualTo(Messages.WELCOME);
    }
}
