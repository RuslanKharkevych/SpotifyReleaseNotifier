package me.khruslan.spotifyreleasenotifier.user;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "spotify_auth_token")
    private String spotifyAuthToken;

    @Column(name = "spotify_refresh_token")
    private String spotifyRefreshToken;

    public User() {
    }

    public User(Long telegramId, String spotifyAuthToken, String spotifyRefreshToken) {
        this.telegramId = telegramId;
        this.spotifyAuthToken = spotifyAuthToken;
        this.spotifyRefreshToken = spotifyRefreshToken;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public String getSpotifyAuthToken() {
        return spotifyAuthToken;
    }

    public String getSpotifyRefreshToken() {
        return spotifyRefreshToken;
    }
}
