package com.ds.freeboard.web;

import com.ds.freeboard.domain.posts.YouTubeRepository;
import com.ds.freeboard.domain.posts.YouTubeSearchCriteria;
import com.ds.freeboard.web.dto.YouTubeSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class YouTubeSearchController {

    private final YouTubeRepository youTubeProvider;

    @Autowired
    public YouTubeSearchController(
            final YouTubeRepository youTubeProvider
    ) {
        this.youTubeProvider = youTubeProvider;
    }

    @GetMapping("/youtube")
    public List<YouTubeSearchDto> Index() {
        YouTubeSearchCriteria youtubeSearchCriteria = null;
        return youTubeProvider.get(youtubeSearchCriteria.getQueryTerm());
    }

}

