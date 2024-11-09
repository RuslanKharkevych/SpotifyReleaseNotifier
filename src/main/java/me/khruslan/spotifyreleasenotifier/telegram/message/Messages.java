package me.khruslan.spotifyreleasenotifier.telegram.message;

public class Messages {
    public static final String WELCOME = """
            👋 Welcome to Spotify Release Notifier!

            I'm here to make sure you never miss new releases from the artists you follow \u2060🔔
            
            Here’s what I can do:
            ➣ /login - Connect Spotify account \u2060🔗
            ➣ /logout - Logout and stop service \u2060❌
            ➣ /status - Check current status \u2060ℹ️
            ➣ /help - Get a list of commands \u2060📋

            🚀 Let’s get started!""";

    public static final String LOGIN_URL = """
            👨‍💻 Ready to connect your Spotify account?

            Click the link below to log in and start receiving updates about new releases \u2060⬇️

            %s""";

    public static final String LOGIN_SUCCESS = """
            🎉 You’re all set!

            You will now receive Telegram notifications when new music is released \u2060📣""";

    public static final String LOGIN_ERROR = """
            ⚠️ It seems access wasn’t granted or an error occurred.

            Please try again with /login command \u2060🔄""";

    public static final String LOGIN_BAD_STATE = """
            ✅ You are already logged in!

            If you want to logout or use a different account, use /logout command \u2060🚫""";

    public static final String LOGOUT_SUCCESS = """
            🫡 You're now logged out!

            You will no longer receive new messages \u2060🔕

            If you change your mind, you can log in again with /login command \u2060⏱️""";

    public static final String LOGOUT_BAD_STATE = """
            👌 You are already logged out!

            If you want to login, use /login command \u2060📲""";

    public static final String UNRECOGNIZED_COMMAND = """
            ❓ Couldn't recognize command %s.

            Please type /help to see available commands \u2060📃""";

    public static final String INTERNAL_ERROR = """
            🚨 Oops! Something went wrong.

            Please try again \u2060🤞""";
}
