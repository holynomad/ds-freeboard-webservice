package com.ds.freeboard.domain.posts;

import com.ds.freeboard.web.dto.YouTubeSearchDto;

import java.util.List;

public interface YouTubeRepository {

    //List<YouTubeSearchDto> get();

    //public YouTubeSearchDto get() {
    // add Input param "queryTerm" @ 2021.01.14.
    List<YouTubeSearchDto> get(String queryTerm);
}
