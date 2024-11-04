package me.khruslan.spotifyreleasenotifier.spotify;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;

@Service
public class SpotifyService {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    private final SpotifyApi api;

    @Autowired
    public SpotifyService(SpotifyApi api) {
        this.api = api;
    }

    public String getAuthUrl(String authState) {
        logger.debug("Fetching authorization URL: authState={}", authState);
        var request = api.authorizationCodeUri().state(authState).build();
        var url = request.execute().toString();
        logger.debug("Fetched authorization URL: {}", url);
        return url;
    }

    public AuthorizationCodeCredentials getAuthCredentials(String code) {
        logger.debug("Fetching auth credentials: code={}", code);
        var request = api.authorizationCode(code).build();
        try {
            var credentials = request.execute();
            logger.debug("Successfully fetched auth credentials: {}", credentials);
            return credentials;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to fetch auth credentials", e);
            return null;
        }
    }
}
