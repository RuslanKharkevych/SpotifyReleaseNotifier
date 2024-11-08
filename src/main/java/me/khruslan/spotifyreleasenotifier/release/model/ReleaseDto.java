package me.khruslan.spotifyreleasenotifier.release.model;

import jakarta.persistence.*;

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
}
