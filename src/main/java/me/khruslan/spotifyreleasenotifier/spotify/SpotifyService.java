package me.khruslan.spotifyreleasenotifier.spotify;

import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
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
import java.util.Arrays;
import java.util.List;

@Service
public class SpotifyService {
    private static final int PAGE_SIZE = 50;
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
            waitBeforeNextRequest();
            var artistAlbums = getAlbums(artist.getId());
            var newAlbums = Arrays.stream(artistAlbums)
                    .filter(album -> releasedAfter(album, lastCheckedDate))
                    .toList();
            logger.debug("Fetch new albums progress: newAlbums={}, artist={}", newAlbums, artist);
            albums.addAll(newAlbums);
        }

        logger.debug("Fetched new albums: {}", albums);
        spotifyApi.setAccessToken(null);
        return albums;
    }

    // TODO: Pagination
    private Artist[] getFollowedArtists() {
        logger.debug("Fetching followed artists");
        var request = spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST)
                .limit(PAGE_SIZE)
                .build();

        try {
            var artists = request.execute().getItems();
            logger.debug("Fetched followed artists: {}", Arrays.toString(artists));
            return artists;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to get followed artists", e);
            return new Artist[]{};
        }
    }

    // TODO: Pagination
    private AlbumSimplified[] getAlbums(String artistId) {
        logger.debug("Fetching albums: artistId={}", artistId);
        var request = spotifyApi.getArtistsAlbums(artistId)
                .limit(PAGE_SIZE)
                .build();

        try {
            var albums = request.execute().getItems();
            logger.debug("Fetched albums: {}", Arrays.toString(albums));
            return albums;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            logger.error("Failed to fetch albums", e);
            return new AlbumSimplified[]{};
        }
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
