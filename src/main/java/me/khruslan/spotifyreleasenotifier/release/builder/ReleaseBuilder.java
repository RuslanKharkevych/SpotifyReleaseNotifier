package me.khruslan.spotifyreleasenotifier.release.builder;

import me.khruslan.spotifyreleasenotifier.release.model.Release;

public class ReleaseBuilder {
    private final Release release;

    public ReleaseBuilder() {
        release = new Release();
    }

    public ReleaseBuilder setId(Long id) {
        release.setId(id);
        return this;
    }

    public ReleaseBuilder setAlbumId(String albumId) {
        release.setAlbumId(albumId);
        return this;
    }

    public Release build() {
        return release;
    }
}
