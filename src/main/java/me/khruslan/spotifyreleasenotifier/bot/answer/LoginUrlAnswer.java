package me.khruslan.spotifyreleasenotifier.bot.answer;

public class LoginUrlAnswer extends Answer {

    private final String url;

    public LoginUrlAnswer(String url) {
        this.url = url;
    }

    @Override
    public String getMessage() {
        return url;
    }

    @Override
    public String toString() {
        return "LoginUrlAnswer{url='" + url + "'}";
    }
}
