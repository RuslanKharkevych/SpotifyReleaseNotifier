package me.khruslan.spotifyreleasenotifier.release.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "Release")
@Table(name = "Releases")
public class ReleaseDto {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "album_id")
    private String albumId;

    @Column(name = "user_id")
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
        if (!(o instanceof ReleaseDto that)) return false;
        return userId == that.userId && Objects.equals(id, that.id) && Objects.equals(albumId, that.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, albumId, userId);
    }

    @Override
    public String toString() {
        return "ReleaseDto{" + "id=" + id + ", albumId='" + albumId + "', userId=" + userId + "'";
    }
}
