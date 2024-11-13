package me.khruslan.spotifyreleasenotifier.release.model;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Release release)) return false;
        return userId == release.userId && Objects.equals(id, release.id) && Objects.equals(albumId, release.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, albumId, userId);
    }

    @Override
    public String toString() {
        return "Release{id=" + id + ", albumId='" + albumId + "', userId=" + userId + "}";
    }
}
