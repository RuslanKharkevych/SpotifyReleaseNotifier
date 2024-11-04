package me.khruslan.spotifyreleasenotifier.spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

@Service
public class SpotifyService {
    private final SpotifyApi api;

    @Autowired
    public SpotifyService(SpotifyApi api) {
        this.api = api;
    }

    public String getAuthorizationUrl() {
        var request = api.authorizationCodeUri().build();
        var uri = request.execute();
        return uri.toString();
    }
}
