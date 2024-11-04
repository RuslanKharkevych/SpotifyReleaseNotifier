package me.khruslan.spotifyreleasenotifier.spotify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

@Service
public class SpotifyService {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    private final SpotifyApi api;

    @Autowired
    public SpotifyService(SpotifyApi api) {
        this.api = api;
    }

    public String getAuthorizationUrl() {
        var request = api.authorizationCodeUri().build();
        var url = request.execute().toString();
        logger.debug("Fetched authorization URL: {}", url);
        return url;
    }
}
