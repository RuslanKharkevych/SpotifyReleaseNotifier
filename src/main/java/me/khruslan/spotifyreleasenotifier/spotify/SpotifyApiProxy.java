package me.khruslan.spotifyreleasenotifier.spotify;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.AuthorizationScope;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.requests.AbstractRequest;

import java.io.IOException;
import java.net.URI;

@Component
public class SpotifyApiProxy {
    private static final int PAGE_SIZE = 50;
    private static final long DELAY_BETWEEN_REQUESTS_MILLIS = 30_000L;

    private static final Logger logger = LoggerFactory.getLogger(SpotifyApiProxy.class);

    private final SpotifyApi spotifyApi;

    @Autowired
    public SpotifyApiProxy(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public URI getAuthUri(String state) {
        var request = spotifyApi.authorizationCodeUri()
                .state(state)
                .scope(AuthorizationScope.USER_FOLLOW_READ)
                .build();

        logRequest(request);
        return request.execute();
    }

    public AuthorizationCodeCredentials getAuthCredentials(String code)
            throws IOException, ParseException, SpotifyWebApiException {
        var request = spotifyApi.authorizationCode(code).build();
        logRequest(request);
        return request.execute();
    }

    public AuthorizationCodeCredentials refreshAccessToken(String refreshToken)
            throws IOException, ParseException, SpotifyWebApiException {
        spotifyApi.setRefreshToken(refreshToken);
        var request = spotifyApi.authorizationCodeRefresh().build();
        logRequest(request);

        try {
            return request.execute();
        } finally {
            spotifyApi.setRefreshToken(refreshToken);
        }
    }

    public PagingCursorbased<Artist> getFollowedArtists(String accessToken, String after)
            throws IOException, ParseException, SpotifyWebApiException {
        waitBeforeNextRequest();
        spotifyApi.setAccessToken(accessToken);

        var requestBuilder = spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST);
        if (after != null) requestBuilder.after(after);
        var request = requestBuilder.limit(PAGE_SIZE).build();
        logRequest(request);

        try {
            return request.execute();
        } finally {
            spotifyApi.setAccessToken(null);
        }
    }

    public Paging<AlbumSimplified> getAlbums(String accessToken, String artistId, Integer offset)
            throws IOException, ParseException, SpotifyWebApiException {
        waitBeforeNextRequest();
        spotifyApi.setAccessToken(accessToken);

        var requestBuilder = spotifyApi.getArtistsAlbums(artistId);
        if (offset != null) requestBuilder.offset(offset);
        var request = requestBuilder.limit(PAGE_SIZE).build();
        logRequest(request);

        try {
            return request.execute();
        } finally {
            spotifyApi.setAccessToken(null);
        }
    }

    private void logRequest(AbstractRequest<?> request) {
        logger.debug("Executing request: {}", request.getUri());
    }

    private void waitBeforeNextRequest() {
        logger.debug("Waiting {} ms before the next request", DELAY_BETWEEN_REQUESTS_MILLIS);

        try {
            Thread.sleep(DELAY_BETWEEN_REQUESTS_MILLIS);
        } catch (InterruptedException e) {
            logger.warn("Thread has been interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
