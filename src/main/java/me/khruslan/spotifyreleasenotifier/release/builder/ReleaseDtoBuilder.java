package me.khruslan.spotifyreleasenotifier.release.builder;

import me.khruslan.spotifyreleasenotifier.release.model.ReleaseDto;

public class ReleaseDtoBuilder {
    private final ReleaseDto releaseDto;

    public ReleaseDtoBuilder() {
        releaseDto = new ReleaseDto();
    }

    public ReleaseDtoBuilder setId(Long id) {
        releaseDto.setId(id);
        return this;
    }

    public ReleaseDtoBuilder setAlbumId(String albumId) {
        releaseDto.setAlbumId(albumId);
        return this;
    }

    public ReleaseDtoBuilder setUserId(long userId) {
        releaseDto.setUserId(userId);
        return this;
    }

    public ReleaseDto build() {
        return releaseDto;
    }
}