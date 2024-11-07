package me.khruslan.spotifyreleasenotifier.user;

import jakarta.persistence.*;
import me.khruslan.spotifyreleasenotifier.notifier.NotifiedReleasesRecord;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Column(name = "spotify_access_token")
    private String spotifyAccessToken;

    @Column(name = "spotify_refresh_token")
    private String spotifyRefreshToken;

    @Column(name = "spotify_token_expiration_timestamp")
    private Long spotifyTokenExpirationTimestamp;

    @Column(name = "releases_last_checked_date")
    private String releasesLastCheckedDate;

    @Column(name = "releases_notified_record")
    private String releasesNotifiedRecord;

    @SuppressWarnings("unused")
    public User() {
        // Used internally by Hibernate
    }

    public User(UserMetadata telegramInfo, AuthorizationCodeCredentials spotifyInfo) {
        telegramId = telegramInfo.userId();
        telegramChatId = telegramInfo.chatId();
        setCredentials(spotifyInfo);
        releasesLastCheckedDate = LocalDate.now().toString();
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public String getSpotifyAccessToken() {
        return spotifyAccessToken;
    }

    public String getSpotifyRefreshToken() {
        return spotifyRefreshToken;
    }

    public Long getSpotifyTokenExpirationTimestamp() {
        return spotifyTokenExpirationTimestamp;
    }

    public String getReleasesLastCheckedDate() {
        return releasesLastCheckedDate;
    }

    public void setReleasesLastCheckedDate(String releasesLastCheckedDate) {
        this.releasesLastCheckedDate = releasesLastCheckedDate;
    }

    public NotifiedReleasesRecord getNotifiedReleasesRecord() {
        return releasesNotifiedRecord != null ? new NotifiedReleasesRecord(releasesNotifiedRecord) : null;
    }

    public void setNotifiedReleasesRecord(NotifiedReleasesRecord record) {
        releasesNotifiedRecord = record.getValue();
    }

    public void setCredentials(AuthorizationCodeCredentials credentials) {
        spotifyAccessToken = credentials.getAccessToken();
        spotifyRefreshToken = credentials.getRefreshToken();
        spotifyTokenExpirationTimestamp = System.currentTimeMillis() + credentials.getExpiresIn() * 1000;
    }
}
