package me.khruslan.spotifyreleasenotifier.telegram.message;

import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.telegram.command.CommandFactory;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static me.khruslan.spotifyreleasenotifier.TestUtils.expectRunnable;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.*;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class MessageHandlerTest extends EasyMockSupport {
    @Mock
    private CommandFactory commandFactory;

    @Mock
    private TelegramService telegramService;

    private MessageHandler messageHandler;

    @BeforeEach
    public void init() {
        messageHandler = new MessageHandler(telegramService, commandFactory);
    }

    @Test
    public void givenUpdateWithoutMessage_whenConsume_thenSkipUpdate() {
        replayAll();

        messageHandler.consume(mockUpdateWithoutMessage());
        verifyAll();
    }

    @Test
    public void givenUpdateWithNonTextMessage_whenConsume_thenSkipUpdate() {
        replayAll();

        messageHandler.consume(mockUpdateWithNonTextMessage());
        verifyAll();
    }

    @Test
    public void givenUpdateWithTextMessage_andMessageWithText_whenConsume_thenSendAnswer() {
        expect(commandFactory.create(MESSAGE_TEXT, TELEGRAM_CREDENTIALS)).andReturn(MOCK_COMMAND);
        expectRunnable(() -> telegramService.sendMessage(TELEGRAM_CHAT_ID, MOCK_ANSWER));
        replayAll();

        messageHandler.consume(mockUpdateWithTextMessage());
        verifyAll();
    }
}
