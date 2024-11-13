package me.khruslan.spotifyreleasenotifier.telegram;

import me.khruslan.spotifyreleasenotifier.telegram.message.MessageHandler;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_BOT_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class TelegramBotTest extends EasyMockSupport {
    @Mock
    private TelegramConfig telegramConfig;

    @Mock
    private MessageHandler messageHandler;

    private TelegramBot telegramBot;

    @BeforeEach
    public void init() {
        telegramBot = new TelegramBot(telegramConfig, messageHandler);
    }

    @Test
    public void givenTelegramConfig_whenGetBotToken_returnToken() {
        expect(telegramConfig.getToken()).andReturn(TELEGRAM_BOT_TOKEN);
        replayAll();

        assertThat(telegramBot.getBotToken()).isEqualTo(TELEGRAM_BOT_TOKEN);
        verifyAll();
    }

    @Test
    public void givenMessageHandler_whenGetUpdatesConsumer_thenReturnMessageHandler() {
        replayAll();

        assertThat(telegramBot.getUpdatesConsumer()).isEqualTo(messageHandler);
        verifyAll();
    }
}
