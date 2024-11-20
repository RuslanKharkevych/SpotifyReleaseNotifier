package me.khruslan.spotifyreleasenotifier.fixture;

import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.*;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_CHAT_ID;
import static me.khruslan.spotifyreleasenotifier.fixture.TelegramFixtures.TELEGRAM_USER_ID;

public class SpotifyFixtures {
    // region Authentication
    public static final String AUTH_STATE = TELEGRAM_USER_ID + "_" + TELEGRAM_CHAT_ID;
    public static final String AUTH_CODE = "7f24d581-abb2-4570-b3ff-0381ba63409a";
    public static final String AUTH_ERROR = "Access denied";
    public static final String AUTH_URL = "https://accounts.spotify.com/authorize" +
            "?response_type=code" +
            "&client_id=5aacd1f3-428d-4a25-a21b-c215f0d221a5" +
            "&scope=user-follow-read" +
            "&state=435319_365835" +
            "&redirect_uri=https://example.com/callback";

    public static final String ACCESS_TOKEN = "7d897ecc-f012-49f0-9a21-3ef84bd006bf";
    public static final String REFRESHED_ACCESS_TOKEN = "f1f7abb2-6b60-4fdb-a7e8-01e55fc800f5";
    public static final String REFRESH_TOKEN = "61c0be0c-4bd9-4179-873f-b4544c128424";

    public static final long TOKEN_EXPIRATION_TIMESTAMP =
            CLOCK_BEFORE_TOKEN_EXPIRATION.millis() + TimeUnit.HOURS.toMillis(6);

    private static final long REFRESHED_TOKEN_EXPIRATION_TIMESTAMP =
            CLOCK_AFTER_TOKEN_EXPIRATION.millis() + TimeUnit.HOURS.toMillis(6);

    public static final AuthorizationCodeCredentials AUTH_CREDENTIALS = new AuthorizationCodeCredentials.Builder()
            .setAccessToken(ACCESS_TOKEN)
            .setRefreshToken(REFRESH_TOKEN)
            .setExpiresIn(Math.toIntExact(TimeUnit.HOURS.toSeconds(6)))
            .build();

    public static final SpotifyCredentials SPOTIFY_CREDENTIALS =
            new SpotifyCredentials(ACCESS_TOKEN, REFRESH_TOKEN, TOKEN_EXPIRATION_TIMESTAMP);

    public static final SpotifyCredentials REFRESHED_SPOTIFY_CREDENTIALS =
            new SpotifyCredentials(REFRESHED_ACCESS_TOKEN, REFRESH_TOKEN, REFRESHED_TOKEN_EXPIRATION_TIMESTAMP);
    // endregion Authentication

    // region Artists
    public static final String ARTIST1_ID = "0TnOYISbd1XYRBk9myaseg";
    public static final String ARTIST2_ID = "1vCWHaC5f2uS3yhpwWbIA6";
    private static final Artist ARTIST1 = new Artist.Builder().setId(ARTIST1_ID).build();
    private static final Artist ARTIST2 = new Artist.Builder().setId(ARTIST2_ID).build();
    public static final String ARTISTS_PAGE_CURSOR = "0I2XqVXqHScXjHhk6AYYRe";

    public static final PagingCursorbased<Artist> ARTISTS_PAGE1 = new PagingCursorbased.Builder<Artist>()
            .setItems(new Artist[]{ARTIST1})
            .setNext("https://api.spotify.com/v1/me/following?type=artist&after=0TnOYISbd1XYRBk9myaseg")
            .setCursors(new Cursor.Builder().setAfter(ARTISTS_PAGE_CURSOR).build())
            .build();

    public static final PagingCursorbased<Artist> ARTISTS_PAGE2 = new PagingCursorbased.Builder<Artist>()
            .setItems(new Artist[]{ARTIST2})
            .setCursors(new Cursor.Builder().build())
            .build();
    // endregion Artists

    // region Albums
    public static final String NEW_ALBUM_URL = "https://open.spotify.com/album/39pNTUQZlpcOpQ0WmCjllU";
    public static final int ALBUMS_PAGE_OFFSET = 1;

    public static final AlbumSimplified NEW_ALBUM = new AlbumSimplified.Builder()
            .setId("b4e85e7d-36de-4172-bf74-be05aa07bd7f")
            .setArtists(new ArtistSimplified.Builder().setId(ARTIST1.getId()).build())
            .setReleaseDate(LocalDate.now(CLOCK).toString())
            .setExternalUrls(buildExternalUrls(NEW_ALBUM_URL))
            .build();

    public static final AlbumSimplified OLD_ALBUM = new AlbumSimplified.Builder()
            .setId("a122a423-d83e-41f8-a17d-dc87669be5e6")
            .setArtists(new ArtistSimplified.Builder().setId(ARTIST1.getId()).build())
            .setReleaseDate(LocalDate.now(CLOCK).minusDays(1).toString())
            .setExternalUrls(buildExternalUrls("https://open.spotify.com/album/1gf4tdMN4aMMYEkXeUJTKG"))
            .build();

    public static final AlbumSimplified ALBUM_WITH_UNKNOWN_DATE = new AlbumSimplified.Builder()
            .setId("dda5de87-57fe-490c-94cd-10512e240329")
            .setArtists(new ArtistSimplified.Builder().setId(ARTIST2.getId()).build())
            .setReleaseDate("2024")
            .setExternalUrls(buildExternalUrls("https://open.spotify.com/album/3uei5LFX4boIwVER1zdLD8"))
            .build();

    private static final AlbumSimplified ALBUM1 = NEW_ALBUM;
    private static final AlbumSimplified ALBUM2 = OLD_ALBUM;
    private static final AlbumSimplified ALBUM3 = ALBUM_WITH_UNKNOWN_DATE;
    public static final List<AlbumSimplified> ALBUMS = List.of(ALBUM1, ALBUM2, ALBUM3);

    public static final Paging<AlbumSimplified> ARTIST1_ALBUMS_PAGE1 = new Paging.Builder<AlbumSimplified>()
            .setItems(new AlbumSimplified[]{ALBUM1})
            .setNext("https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg/albums?offset=1")
            .setOffset(0)
            .build();

    public static final Paging<AlbumSimplified> ARTIST1_ALBUMS_PAGE2 = new Paging.Builder<AlbumSimplified>()
            .setItems(new AlbumSimplified[]{ALBUM2})
            .setOffset(ALBUMS_PAGE_OFFSET)
            .build();

    public static final Paging<AlbumSimplified> ARTIST2_ALBUMS_PAGE1 = new Paging.Builder<AlbumSimplified>()
            .setItems(new AlbumSimplified[]{ALBUM3})
            .setOffset(0)
            .build();

    private static ExternalUrl buildExternalUrls(String url) {
        return new ExternalUrl.Builder()
                .setExternalUrls(Map.of("spotify", url))
                .build();
    }
    // endregion Albums
}