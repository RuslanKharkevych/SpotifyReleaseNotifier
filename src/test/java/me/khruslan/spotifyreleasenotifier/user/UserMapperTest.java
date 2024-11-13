package me.khruslan.spotifyreleasenotifier.user;

import me.khruslan.spotifyreleasenotifier.release.ReleaseMapper;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK;
import static me.khruslan.spotifyreleasenotifier.fixture.ReleaseFixtures.RELEASE_HISTORY;
import static me.khruslan.spotifyreleasenotifier.fixture.ReleaseFixtures.RELEASE_HISTORY_DTO;
import static me.khruslan.spotifyreleasenotifier.fixture.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

@ExtendWith(EasyMockExtension.class)
public class UserMapperTest extends EasyMockSupport {
    @Mock
    private ReleaseMapper releaseMapper;

    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        userMapper = new UserMapper(CLOCK, releaseMapper);
    }

    @Test
    public void givenUser_whenMapToUserDto_thenReturnUserDto() {
        expect(releaseMapper.mapToDto(RELEASE_HISTORY)).andReturn(RELEASE_HISTORY_DTO);
        replayAll();

        assertThat(userMapper.mapToDto(mockUserWithReleaseHistory())).isEqualTo(USER_DTO);
        verifyAll();
    }

    @Test
    public void givenUserDto_whenMapFromUserDto_thenReturnUser() {
        expect(releaseMapper.mapFromDto(RELEASE_HISTORY_DTO)).andReturn(RELEASE_HISTORY);
        replayAll();

        assertThat(userMapper.mapFromDto(List.of(USER_DTO))).isEqualTo(List.of(mockUserWithReleaseHistory()));
        verifyAll();
    }
}
