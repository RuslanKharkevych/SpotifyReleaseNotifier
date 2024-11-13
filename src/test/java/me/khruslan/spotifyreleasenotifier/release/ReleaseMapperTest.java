package me.khruslan.spotifyreleasenotifier.release;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.khruslan.spotifyreleasenotifier.fixture.CoreFixtures.CLOCK;
import static me.khruslan.spotifyreleasenotifier.fixture.ReleaseFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseMapperTest {
    private ReleaseMapper releaseMapper;

    @BeforeEach
    public void init() {
        releaseMapper = new ReleaseMapper(CLOCK);
    }

    @Test
    public void givenReleaseHistory_whenMapToDto_thenReturnReleaseHistoryDto() {
        assertThat(releaseMapper.mapToDto(RELEASE_HISTORY)).isEqualTo(RELEASE_HISTORY_DTO);
    }

    @Test
    public void givenReleaseHistoryDtoWithValidDate_whenMapFromDto_thenReturnReleaseHistoryDtoWithMappedDate() {
        assertThat(releaseMapper.mapFromDto(RELEASE_HISTORY_DTO)).isEqualTo(RELEASE_HISTORY);
    }

    @Test
    public void givenReleaseHistoryDtoWithInvalidDate_whenMapFromDto_thenReturnReleaseHistoryDtoWithCurrentDate() {
        assertThat(releaseMapper.mapFromDto(RELEASE_HISTORY_DTO_WITH_INVALID_DATE))
                .isEqualTo(RELEASE_HISTORY_WITH_CURRENT_DATE);
    }
}
