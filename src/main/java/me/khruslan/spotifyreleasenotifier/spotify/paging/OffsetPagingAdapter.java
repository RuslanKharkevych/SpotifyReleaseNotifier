package me.khruslan.spotifyreleasenotifier.spotify.paging;

import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.Arrays;
import java.util.List;

public class OffsetPagingAdapter<T> implements PagingAdapter<T, Integer> {
    private final Paging<T> adaptee;

    public OffsetPagingAdapter(Paging<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public List<T> getItems() {
        return Arrays.asList(adaptee.getItems());
    }

    @Override
    public Integer getIdentifier() {
        var offset = adaptee.getOffset();
        return offset != null ? offset + adaptee.getItems().length : null;
    }

    @Override
    public boolean hasNext() {
        return adaptee.getNext() != null;
    }

    @Override
    public String toString() {
        return "OffsetPagingAdapter{adaptee=" + adaptee + "}";
    }
}
