package me.khruslan.spotifyreleasenotifier.spotify.paging;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

@FunctionalInterface
public interface PageLoader<T, U> {
    PagingAdapter<T, U> loadPage(U identifier) throws IOException, ParseException, SpotifyWebApiException;
}
