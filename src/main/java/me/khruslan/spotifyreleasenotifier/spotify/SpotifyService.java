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
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.AuthorizationScope;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyService {
    private static final long DELAY_BETWEEN_REQUESTS_MILLIS = 30_000L;

    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    private final SpotifyApi spotifyApi;

    @Autowired
    public SpotifyService(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public String getAuthUrl(String authState) {
        logger.debug("Fetching authorization URL: authState={}", authState);
        var request = spotifyApi.authorizationCodeUri()
                .state(authState)
                .scope(AuthorizationScope.USER_FOLLOW_READ)
                .build();

        var url = request.execute().toString();
        logger.debug("Fetched authorization URL: {}", url);
        return url;
    }

    public SpotifyCredentials getAuthCredentials(String code) {
        logger.debug("Fetching auth credentials: code={}", code);
        var request = spotifyApi.authorizationCode(code).build();

        try {
            var credentials = new SpotifyCredentials(request.execute());
            logger.debug("Successfully fetched auth credentials: {}", credentials);
            return credentials;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to fetch auth credentials", e);
            return null;
        }
    }

    public SpotifyCredentials refreshAccessToken(String refreshToken) {
        logger.debug("Refreshing access token: refreshToken={}", refreshToken);
        spotifyApi.setRefreshToken(refreshToken);
        var request = spotifyApi.authorizationCodeRefresh().build();

        try {
            var credentials = new SpotifyCredentials(request.execute());
            logger.debug("Successfully refreshed access token: credentials={}", credentials);
            return credentials;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to refresh access token", e);
            return null;
        } finally {
            spotifyApi.setRefreshToken(null);
        }
    }

    public List<AlbumSimplified> getNewAlbums(String accessToken, LocalDate lastCheckedDate) {
        logger.debug("Fetching new albums: accessToken={}, lastCheckedDate={}", accessToken, lastCheckedDate);
        spotifyApi.setAccessToken(accessToken);
        List<AlbumSimplified> albums = new ArrayList<>();

        for (var artist : getFollowedArtists()) {
            var newAlbums = getAlbums(artist.getId())
                    .stream()
                    .filter(album -> releasedAfter(album, lastCheckedDate))
                    .toList();
            logger.debug("Fetch new albums progress: newAlbums={}, artist={}", newAlbums, artist);
            albums.addAll(newAlbums);
        }

        logger.debug("Fetched new albums: {}", albums);
        spotifyApi.setAccessToken(null);
        return albums;
    }

    private List<Artist> getFollowedArtists() {
        logger.debug("Fetching followed artists");
        var artists = PagingUtil.getAllItems((String cursor) -> getFollowedArtists(cursor));
        logger.debug("Fetched followed artists: {}", artists);
        return artists;
    }

    private List<AlbumSimplified> getAlbums(String artistId) {
        logger.debug("Fetching albums: artistId={}", artistId);
        var albums = PagingUtil.getAllItems((Integer offset) -> getAlbums(artistId, offset));
        logger.debug("Fetched albums: {}", albums);
        return albums;
    }

    private CursorPagingAdapter<Artist> getFollowedArtists(String after)
            throws IOException, ParseException, SpotifyWebApiException {
        waitBeforeNextRequest();
        var requestBuilder = spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST);
        if (after != null) requestBuilder.after(after);
        var request = requestBuilder.limit(PagingUtil.PAGE_SIZE).build();
        return new CursorPagingAdapter<>(request.execute());
    }

    private OffsetPagingAdapter<AlbumSimplified> getAlbums(String artistId, Integer offset)
            throws IOException, ParseException, SpotifyWebApiException {
        waitBeforeNextRequest();
        var requestBuilder = spotifyApi.getArtistsAlbums(artistId);
        if (offset != null) requestBuilder.offset(offset);
        var request = requestBuilder.limit(PagingUtil.PAGE_SIZE).build();
        return new OffsetPagingAdapter<>(request.execute());
    }

    // TODO: Move to ReleaseNotifier
    private boolean releasedAfter(AlbumSimplified album, LocalDate lastCheckedDate) {
        try {
            var releaseDate = LocalDate.parse(album.getReleaseDate());
            return !releaseDate.isBefore(lastCheckedDate);
        } catch (DateTimeParseException e) {
            logger.debug("Failed to parse release date: album={}", album);
            return false;
        }
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
