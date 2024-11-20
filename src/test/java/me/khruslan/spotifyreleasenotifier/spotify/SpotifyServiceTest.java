package me.khruslan.spotifyreleasenotifier.spotify;

import org.apache.hc.core5.http.ParseException;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK;
import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class SpotifyServiceTest extends EasyMockSupport {
    @Mock
    private SpotifyApiProxy spotifyApi;

    private SpotifyService spotifyService;

    @BeforeEach
    public void init() {
        spotifyService = new SpotifyService(CLOCK, spotifyApi);
    }

    @Test
    void givenAuthState_whenGetAuthUrl_thenReturnAuthUrl() {
        expect(spotifyApi.getAuthUri(AUTH_STATE)).andReturn(URI.create(AUTH_URL));
        replayAll();

        assertThat(spotifyService.getAuthUrl(AUTH_STATE)).isEqualTo(AUTH_URL);
        verifyAll();
    }

    @Test
    void givenAuthCodeValid_whenGetAuthCredentials_thenReturnCredentials()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.getAuthCredentials(AUTH_CODE)).andReturn(AUTH_CREDENTIALS);
        replayAll();

        assertThat(spotifyService.getAuthCredentials(AUTH_CODE)).isEqualTo(SPOTIFY_CREDENTIALS);
        verifyAll();
    }

    @Test
    void givenAuthCodeInvalid_whenGetAuthCredentials_thenReturnNull()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.getAuthCredentials(AUTH_CODE)).andThrow(new SpotifyWebApiException());
        replayAll();

        assertThat(spotifyService.getAuthCredentials(AUTH_CODE)).isEqualTo(null);
        verifyAll();
    }

    @Test
    void givenRefreshTokenValid_whenRefreshAccessToken_thenReturnCredentials()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.refreshAccessToken(REFRESH_TOKEN)).andReturn(AUTH_CREDENTIALS);
        replayAll();

        assertThat(spotifyService.refreshAccessToken(REFRESH_TOKEN)).isEqualTo(SPOTIFY_CREDENTIALS);
        verifyAll();
    }

    @Test
    void givenRefreshTokenInvalid_whenRefreshAccessToken_thenReturnNull()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.refreshAccessToken(REFRESH_TOKEN)).andThrow(new SpotifyWebApiException());
        replayAll();

        assertThat(spotifyService.refreshAccessToken(REFRESH_TOKEN)).isEqualTo(null);
        verifyAll();
    }

    @Test
    void givenAccessTokenValid_andArtistIdsValid_whenGetAlbumsFromFollowedArtists_thenReturnAlbums()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.getFollowedArtists(ACCESS_TOKEN, null)).andReturn(ARTISTS_PAGE1);
        expect(spotifyApi.getFollowedArtists(ACCESS_TOKEN, ARTISTS_PAGE_CURSOR)).andReturn(ARTISTS_PAGE2);
        expect(spotifyApi.getAlbums(ACCESS_TOKEN, ARTIST1_ID, null)).andReturn(ARTIST1_ALBUMS_PAGE1);
        expect(spotifyApi.getAlbums(ACCESS_TOKEN, ARTIST1_ID, ALBUMS_PAGE_OFFSET)).andReturn(ARTIST1_ALBUMS_PAGE2);
        expect(spotifyApi.getAlbums(ACCESS_TOKEN, ARTIST2_ID, null)).andReturn(ARTIST2_ALBUMS_PAGE1);
        replayAll();

        assertThat(getAlbumsFromFollowedArtists()).isEqualTo(ALBUMS);
        verifyAll();
    }

    @Test
    void givenAccessTokenValid_andArtistIdsInvalid_whenGetAlbumsFromFollowedArtists_thenReturnEmptyList()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.getFollowedArtists(ACCESS_TOKEN, null)).andReturn(ARTISTS_PAGE1);
        expect(spotifyApi.getFollowedArtists(ACCESS_TOKEN, ARTISTS_PAGE_CURSOR)).andReturn(ARTISTS_PAGE2);
        expect(spotifyApi.getAlbums(ACCESS_TOKEN, ARTIST1_ID, null)).andThrow(new SpotifyWebApiException());
        expect(spotifyApi.getAlbums(ACCESS_TOKEN, ARTIST2_ID, null)).andThrow(new SpotifyWebApiException());
        replayAll();

        assertThat(getAlbumsFromFollowedArtists()).isEmpty();
        verifyAll();
    }

    @Test
    void givenAccessTokenInvalid_whenGetAlbumsFromFollowedArtists_thenReturnEmptyList()
            throws IOException, ParseException, SpotifyWebApiException {
        expect(spotifyApi.getFollowedArtists(ACCESS_TOKEN, null)).andThrow(new SpotifyWebApiException());
        replayAll();

        assertThat(getAlbumsFromFollowedArtists()).isEmpty();
        verifyAll();
    }

    private List<AlbumSimplified> getAlbumsFromFollowedArtists() {
        List<AlbumSimplified> albums = new ArrayList<>();
        spotifyService.getAlbumsFromFollowedArtists(() -> ACCESS_TOKEN, albums::addAll);
        return albums;
    }
}
