package me.khruslan.spotifyreleasenotifier.release;

import me.khruslan.spotifyreleasenotifier.release.builder.ReleaseBuilder;
import me.khruslan.spotifyreleasenotifier.release.builder.ReleaseDtoBuilder;
import me.khruslan.spotifyreleasenotifier.release.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class ReleaseMapper {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseMapper.class);

    public ReleaseHistoryDto mapToDto(ReleaseHistory history) {
        var date = history.date().toString();
        var releases = mapReleasesDto(history.releases());
        return new ReleaseHistoryDto(date, releases);
    }

    public ReleaseHistory mapFromDto(ReleaseHistoryDto dto) {
        var date = parseReleaseHistoryDate(dto.date());
        var releases = mapReleases(dto.releases());
        return new ReleaseHistory(date, releases);
    }

    private List<Release> mapReleases(List<ReleaseDto> dto) {
        return dto.stream().map(this::mapRelease).toList();
    }

    private List<ReleaseDto> mapReleasesDto(List<Release> releases) {
        return releases.stream().map(this::mapReleaseDto).toList();
    }

    private Release mapRelease(ReleaseDto dto) {
        return new ReleaseBuilder()
                .setId(dto.getId())
                .setAlbumId(dto.getAlbumId())
                .setUserId(dto.getUserId())
                .build();
    }

    private ReleaseDto mapReleaseDto(Release release) {
        return new ReleaseDtoBuilder()
                .setId(release.getId())
                .setAlbumId(release.getAlbumId())
                .setUserId(release.getUserId())
                .build();
    }

    private LocalDate parseReleaseHistoryDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse release history date", e);
            return LocalDate.now();
        }
    }
}
