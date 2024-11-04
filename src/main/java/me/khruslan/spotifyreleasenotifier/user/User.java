package me.khruslan.spotifyreleasenotifier.user;

public record User(Long telegramId, String spotifyAuthToken, String spotifyRefreshToken) {
}
