package me.khruslan.spotifyreleasenotifier.spotify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

@Configuration
public class SpotifyConfig {
    @Bean
    public SpotifyApi api(@Value("${spotify.app.clientId}") String clientId,
                          @Value("${spotify.app.clientSecret}") String clientSecret,
                          @Value("${spotify.app.redirectUrl}") String redirectUrl) {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectUrl))
                .build();
    }
}
