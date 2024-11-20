package me.khruslan.spotifyreleasenotifier.spotify;

import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
import me.khruslan.spotifyreleasenotifier.spotify.paging.CursorPagingAdapter;
import me.khruslan.spotifyreleasenotifier.spotify.paging.OffsetPagingAdapter;
import me.khruslan.spotifyreleasenotifier.spotify.paging.PagingUtil;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class SpotifyService {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    private final Clock clock;
    private final SpotifyApiProxy spotifyApi;

    @Autowired
    public SpotifyService(Clock clock, SpotifyApiProxy spotifyApi) {
        this.clock = clock;
        this.spotifyApi = spotifyApi;
    }

    public String getAuthUrl(String authState) {
        logger.debug("Fetching authorization URL: authState={}", authState);
        var url = spotifyApi.getAuthUri(authState).toString();
        logger.debug("Fetched authorization URL: {}", url);
        return url;
    }

    public SpotifyCredentials getAuthCredentials(String code) {
        logger.debug("Fetching auth credentials: code={}", code);

        try {
            var authCredentials = spotifyApi.getAuthCredentials(code);
            var credentials = SpotifyCredentials.create(authCredentials, clock);
            logger.debug("Successfully fetched auth credentials: {}", credentials);
            return credentials;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to fetch auth credentials", e);
            return null;
        }
    }

    public SpotifyCredentials refreshAccessToken(String refreshToken) {
        logger.debug("Refreshing access token: refreshToken={}", refreshToken);

        try {
            var authCredentials = spotifyApi.refreshAccessToken(refreshToken);
            var credentials = SpotifyCredentials.create(authCredentials, clock).copyWith(refreshToken);
            logger.debug("Successfully refreshed access token: credentials={}", credentials);
            return credentials;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to refresh access token", e);
            return null;
        }
    }

    public void getAlbumsFromFollowedArtists(Supplier<String> accessToken,
                                             Consumer<List<AlbumSimplified>> onPageLoaded) {
        logger.debug("Fetching albums from followed artists");
        getFollowedArtists(accessToken, (artists) -> {
            for (var artist : artists) {
                getAlbums(accessToken, artist.getId(), onPageLoaded);
            }
        });
    }

    private void getFollowedArtists(Supplier<String> accessToken, Consumer<List<Artist>> onPageLoaded) {
        logger.debug("Fetching followed artists");
        var artists = PagingUtil.getAllItems((String cursor) -> {
            var page = spotifyApi.getFollowedArtists(accessToken.get(), cursor);
            var adapter = new CursorPagingAdapter<>(page);
            onPageLoaded.accept(adapter.getItems());
            return adapter;
        });
        logger.debug("Fetched followed artists: {}", artists);
    }

    private void getAlbums(Supplier<String> accessToken, String artistId,
                           Consumer<List<AlbumSimplified>> onPageLoaded) {
        logger.debug("Fetching albums: artistId={}", artistId);
        var albums = PagingUtil.getAllItems((Integer offset) -> {
            var page = spotifyApi.getAlbums(accessToken.get(), artistId, offset);
            var adapter = new OffsetPagingAdapter<>(page);
            onPageLoaded.accept(adapter.getItems());
            return adapter;
        });
        logger.debug("Fetched albums: {}", albums);
    }
}
