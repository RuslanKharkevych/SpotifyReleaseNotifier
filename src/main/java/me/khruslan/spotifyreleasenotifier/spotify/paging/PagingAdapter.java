package me.khruslan.spotifyreleasenotifier.spotify.paging;

import java.util.List;

public interface PagingAdapter<T, U> {
    List<T> getItems();
    U getIdentifier();
    boolean hasNext();
}
