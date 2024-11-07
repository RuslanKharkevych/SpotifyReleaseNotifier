package me.khruslan.spotifyreleasenotifier.notifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

// TODO: Map in UserService
public class NotifiedReleasesRecord {
    private static final String TOKEN_DELIMITER = "/";

    private String value;

    public NotifiedReleasesRecord(String value) {
        this.value = value;
    }

    public NotifiedReleasesRecord(LocalDate date) {
        value = date.toString();
    }

    public NotifiedReleasesRecord(LocalDate date, List<String> albumIds) {
        value = date.toString();
        appendAlbumIds(albumIds);
    }

    public String getValue() {
        return value;
    }

    // TODO: Catch parsing error, handle index out of bounds error
    public LocalDate getDate() {
        return LocalDate.parse(getTokens()[0]);
    }

    public String[] getAlbumIds() {
        var tokens = getTokens();

        if (tokens.length > 1) {
            return Arrays.copyOfRange(tokens, 1, tokens.length);
        } else {
            return new String[]{};
        }
    }

    public void appendAlbumIds(List<String> albumIds) {
        if (!albumIds.isEmpty()) {
            value += TOKEN_DELIMITER + String.join(TOKEN_DELIMITER, albumIds);
        }
    }

    private String[] getTokens() {
        return value.split(TOKEN_DELIMITER);
    }
}
