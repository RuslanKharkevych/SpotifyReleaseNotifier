package me.khruslan.spotifyreleasenotifier.release.model;

public class Release {
    private Long id;
    private String albumId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
