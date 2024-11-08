package me.khruslan.spotifyreleasenotifier.spotify.paging;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PagingUtil {
    public static final int PAGE_SIZE = 50;

    private static final Logger logger = LoggerFactory.getLogger(PagingUtil.class);

    public static <T, U> List<T> getAllItems(PageLoader<T, U> pageLoader) {
        List<T> items = new ArrayList<>();
        U identifier = null;
        PagingAdapter<T, U> paging;

        do {
            paging = loadPage(pageLoader, identifier);
            if (paging == null) break;
            identifier = paging.getIdentifier();
            items.addAll(paging.getItems());
        } while (paging.hasNext());

        return items;
    }

    private static <T, U> PagingAdapter<T, U> loadPage(PageLoader<T, U> pageLoader, U identifier) {
        try {
            var page = pageLoader.loadPage(identifier);
            logger.debug("Loaded page: {}", page);
            return page;
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            logger.error("Failed to load page", e);
            return null;
        }
    }
}
