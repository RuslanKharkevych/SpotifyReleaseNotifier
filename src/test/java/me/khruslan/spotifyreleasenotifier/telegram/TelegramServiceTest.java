package me.khruslan.spotifyreleasenotifier.telegram;

import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.*;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class TelegramServiceTest extends EasyMockSupport {
    @Mock
    private TelegramClient telegramClient;

    private TelegramService telegramService;

    @BeforeEach
    public void init() {
        telegramService = new TelegramService(telegramClient);
    }

    @Test
    public void givenMessageIsSendable_whenSendMessage_thenDeliverMessage() throws TelegramApiException {
        expect(telegramClient.execute(SEND_MESSAGE)).andReturn(MESSAGE);
        replayAll();

        telegramService.sendMessage(TELEGRAM_CHAT_ID, MESSAGE_TEXT);
        verifyAll();
    }

    @Test
    public void givenMessageIsNotSendable_whenSendMessage_thenDoNotDeliverMessage() throws TelegramApiException {
        expect(telegramClient.execute(SEND_MESSAGE)).andThrow(new TelegramApiException());
        replayAll();

        telegramService.sendMessage(TELEGRAM_CHAT_ID, MESSAGE_TEXT);
        verifyAll();
    }
}
