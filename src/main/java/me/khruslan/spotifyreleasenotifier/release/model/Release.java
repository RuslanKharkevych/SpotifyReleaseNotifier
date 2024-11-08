package me.khruslan.spotifyreleasenotifier.release.model;

public class Release {
    private Long id;
    private String albumId;
    private long userId;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Release{id=" + id + ", albumId='" + albumId + "', userId=" + userId + "}";
    }
}
