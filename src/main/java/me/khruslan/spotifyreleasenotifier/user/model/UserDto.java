package me.khruslan.spotifyreleasenotifier.user.model;

import jakarta.persistence.*;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseDto;

import java.util.List;

@Entity(name = "User")
@Table(name = "Users")
public class UserDto {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "telegram_id")
    private long telegramId;

    @Column(name = "telegram_chat_id")
    private long telegramChatId;

    @Column(name = "spotify_access_token")
    private String spotifyAccessToken;

    @Column(name = "spotify_refresh_token")
    private String spotifyRefreshToken;

    @Column(name = "spotify_token_expiration_timestamp")
    private long spotifyTokenExpirationTimestamp;

    @Column(name = "release_history_date")
    private String releaseHistoryDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<ReleaseDto> releases;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(long telegramId) {
        this.telegramId = telegramId;
    }

    public long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getSpotifyAccessToken() {
        return spotifyAccessToken;
    }

    public void setSpotifyAccessToken(String spotifyAccessToken) {
        this.spotifyAccessToken = spotifyAccessToken;
    }

    public String getSpotifyRefreshToken() {
        return spotifyRefreshToken;
    }

    public void setSpotifyRefreshToken(String spotifyRefreshToken) {
        this.spotifyRefreshToken = spotifyRefreshToken;
    }

    public long getSpotifyTokenExpirationTimestamp() {
        return spotifyTokenExpirationTimestamp;
    }

    public void setSpotifyTokenExpirationTimestamp(long spotifyTokenExpirationTimestamp) {
        this.spotifyTokenExpirationTimestamp = spotifyTokenExpirationTimestamp;
    }

    public String getReleaseHistoryDate() {
        return releaseHistoryDate;
    }

    public void setReleaseHistoryDate(String releaseHistoryDate) {
        this.releaseHistoryDate = releaseHistoryDate;
    }

    public List<ReleaseDto> getReleases() {
        return releases;
    }

    public void setReleases(List<ReleaseDto> releases) {
        this.releases = releases;
    }
}
