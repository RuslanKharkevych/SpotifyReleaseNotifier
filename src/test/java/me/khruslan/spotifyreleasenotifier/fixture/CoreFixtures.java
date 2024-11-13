package me.khruslan.spotifyreleasenotifier.fixture;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

public class CoreFixtures {
    private static final Instant INSTANT = Instant.parse("2024-11-09T16:11:56Z");

    public static final Clock CLOCK = Clock.fixed(INSTANT, ZoneOffset.UTC);
    public static final Clock CLOCK_BEFORE_TOKEN_EXPIRATION = CLOCK;
    public static final Clock CLOCK_AFTER_TOKEN_EXPIRATION = Clock.offset(CLOCK, Duration.ofDays(1));
}