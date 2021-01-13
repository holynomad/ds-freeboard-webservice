package com.ds.freeboard.domain.posts;

import com.ds.freeboard.web.dto.YouTubeSearchDto;

import java.util.List;

public interface YouTubeRepository {
    List<YouTubeSearchDto> get();
}
