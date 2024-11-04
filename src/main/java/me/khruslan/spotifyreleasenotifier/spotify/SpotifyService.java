package me.khruslan.spotifyreleasenotifier.spotify;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

@Service
public class SpotifyService {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    private final SpotifyApi api;

    @Autowired
    public SpotifyService(SpotifyApi api) {
        this.api = api;
    }

    public String getAuthorizationUrl(Long chatId) {
        logger.debug("Fetching authorization URL: chatId={}", chatId);
        var request = api.authorizationCodeUri().state(chatId.toString()).build();
        var url = request.execute().toString();
        logger.debug("Fetched authorization URL: {}", url);
        return url;
    }

    public boolean authorize(String code) {
        logger.debug("Authorizing with code: ${}", code);
        var request = api.authorizationCode(code).build();
        try {
            var credentials = request.execute();
            logger.debug("Successfully authorized: credentials={}", credentials);
            api.setAccessToken(credentials.getAccessToken());
            api.setRefreshToken(credentials.getRefreshToken());
            return true;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to authorize", e);
            return false;
        }
    }
}
