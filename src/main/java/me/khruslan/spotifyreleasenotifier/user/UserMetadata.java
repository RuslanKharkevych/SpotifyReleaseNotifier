package me.khruslan.spotifyreleasenotifier.user;

public record UserMetadata(Long userId, Long chatId) {

    public static UserMetadata fromAuthState(String authState) {
        var params = authState.split("_");
        var userId = Long.valueOf(params[0]);
        var chatId = Long.valueOf(params[1]);
        return new UserMetadata(userId, chatId);
    }

    public String toAuthState() {
        return userId + "_" + chatId;
    }
}
