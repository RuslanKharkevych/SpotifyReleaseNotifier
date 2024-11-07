package me.khruslan.spotifyreleasenotifier.auth;

public record TelegramCredentials(long userId, long chatId) {
    public static TelegramCredentials fromAuthState(String authState) {
        var params = authState.split("_");
        var userId = Long.parseLong(params[0]);
        var chatId = Long.parseLong(params[1]);
        return new TelegramCredentials(userId, chatId);
    }

    public String toAuthState() {
        return userId + "_" + chatId;
    }
}
