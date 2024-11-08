package me.khruslan.spotifyreleasenotifier.spotify.paging;

import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;

import java.util.Arrays;
import java.util.List;

public class CursorPagingAdapter<T> implements PagingAdapter<T, String> {
    private final PagingCursorbased<T> adaptee;

    public CursorPagingAdapter(PagingCursorbased<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public List<T> getItems() {
        return Arrays.asList(adaptee.getItems());
    }

    @Override
    public String getIdentifier() {
        var cursors = adaptee.getCursors();
        return cursors.length > 0 ? cursors[0].getAfter() : null;
    }

    @Override
    public boolean hasNext() {
        return adaptee.getNext() != null;
    }

    @Override
    public String toString() {
        return "CursorPagingAdapter{adaptee=" + adaptee + "}";
    }
}