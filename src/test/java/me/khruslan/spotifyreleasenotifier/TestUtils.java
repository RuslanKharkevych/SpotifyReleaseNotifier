package me.khruslan.spotifyreleasenotifier;

import static org.easymock.EasyMock.expectLastCall;

public class TestUtils {
    public static void expectRunnable(Runnable call) {
        call.run();
        expectLastCall();
    }
}
