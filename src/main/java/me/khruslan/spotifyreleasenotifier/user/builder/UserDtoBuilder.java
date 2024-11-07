package me.khruslan.spotifyreleasenotifier.user.builder;

import me.khruslan.spotifyreleasenotifier.release.model.ReleaseDto;
import me.khruslan.spotifyreleasenotifier.user.model.UserDto;

import java.util.List;

public class UserDtoBuilder {
    private final UserDto userDto;

    public UserDtoBuilder() {
        userDto = new UserDto();
    }

    public UserDtoBuilder setId(Long id) {
        userDto.setId(id);
        return this;
    }

    public UserDtoBuilder setTelegramId(Long telegramId) {
        userDto.setTelegramId(telegramId);
        return this;
    }

    public UserDtoBuilder setTelegramChatId(Long telegramChatId) {
        userDto.setTelegramChatId(telegramChatId);
        return this;
    }

    public UserDtoBuilder setSpotifyAccessToken(String spotifyAccessToken) {
        userDto.setSpotifyAccessToken(spotifyAccessToken);
        return this;
    }

    public UserDtoBuilder setSpotifyRefreshToken(String spotifyRefreshToken) {
        userDto.setSpotifyRefreshToken(spotifyRefreshToken);
        return this;
    }

    public UserDtoBuilder setSpotifyTokenExpirationTimestamp(Long spotifyTokenExpirationTimestamp) {
        userDto.setSpotifyTokenExpirationTimestamp(spotifyTokenExpirationTimestamp);
        return this;
    }

    public UserDtoBuilder setReleaseHistoryDate(String releaseHistoryDate) {
        userDto.setReleaseHistoryDate(releaseHistoryDate);
        return this;
    }

    public UserDtoBuilder setReleases(List<ReleaseDto> releases) {
        userDto.setReleases(releases);
        return this;
    }

    public UserDto build() {
        return userDto;
    }
}