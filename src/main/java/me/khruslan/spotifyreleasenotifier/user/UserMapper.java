package me.khruslan.spotifyreleasenotifier.user;

import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import me.khruslan.spotifyreleasenotifier.release.ReleaseMapper;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistory;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistoryDto;
import me.khruslan.spotifyreleasenotifier.user.builder.UserBuilder;
import me.khruslan.spotifyreleasenotifier.user.builder.UserDtoBuilder;
import me.khruslan.spotifyreleasenotifier.user.model.User;
import me.khruslan.spotifyreleasenotifier.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    private final ReleaseMapper releaseMapper;

    @Autowired
    public UserMapper(ReleaseMapper releaseMapper) {
        this.releaseMapper = releaseMapper;
    }

    public UserDto mapToDto(User user) {
        var telegramCredentials = user.getTelegramCredentials();
        var spotifyCredentials = user.getSpotifyCredentials();
        var releaseHistoryDto = mapReleaseHistoryDto(user);

        return new UserDtoBuilder()
                .setId(user.getId())
                .setTelegramId(telegramCredentials.userId())
                .setTelegramChatId(telegramCredentials.chatId())
                .setSpotifyAccessToken(spotifyCredentials.accessToken())
                .setSpotifyRefreshToken(spotifyCredentials.refreshToken())
                .setSpotifyTokenExpirationTimestamp(spotifyCredentials.tokenExpirationTimestamp())
                .setReleaseHistoryDate(releaseHistoryDto.date())
                .setReleases(releaseHistoryDto.releases())
                .build();
    }

    public List<User> mapFromDto(List<UserDto> dto) {
        return dto.stream().map(this::mapFromDto).toList();
    }

    private User mapFromDto(UserDto dto) {
        var telegramCredentials = mapTelegramCredentials(dto);
        var spotifyCredentials = mapSpotifyCredentials(dto);
        var releaseHistory = mapReleaseHistory(dto);

        return new UserBuilder()
                .setId(dto.getId())
                .setTelegramCredentials(telegramCredentials)
                .setSpotifyCredentials(spotifyCredentials)
                .setReleaseHistory(releaseHistory)
                .build();
    }

    private TelegramCredentials mapTelegramCredentials(UserDto userDto) {
        var userId = userDto.getTelegramId();
        var chatId = userDto.getTelegramChatId();
        return new TelegramCredentials(userId, chatId);
    }

    private SpotifyCredentials mapSpotifyCredentials(UserDto userDto) {
        var accessToken = userDto.getSpotifyAccessToken();
        var refreshToken = userDto.getSpotifyRefreshToken();
        var tokenExpirationTimestamp = userDto.getSpotifyTokenExpirationTimestamp();
        return new SpotifyCredentials(accessToken, refreshToken, tokenExpirationTimestamp);
    }

    private ReleaseHistory mapReleaseHistory(UserDto userDto) {
        var historyDto = new ReleaseHistoryDto(userDto.getReleaseHistoryDate(), userDto.getReleases());
        return releaseMapper.mapFromDto(historyDto);
    }

    private ReleaseHistoryDto mapReleaseHistoryDto(User user) {
        var history = user.getReleaseHistory();
        return releaseMapper.mapToDto(history);
    }
}
