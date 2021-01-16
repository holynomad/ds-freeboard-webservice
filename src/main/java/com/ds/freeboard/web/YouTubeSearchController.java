package com.ds.freeboard.web;

import com.ds.freeboard.domain.posts.YouTubeRepository;
import com.ds.freeboard.domain.posts.YouTubeSearchCriteria;
import com.ds.freeboard.web.dto.YouTubeSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
    public List<YouTubeSearchDto> Index(Model model) {
    //public String demo(Model model) {

        System.out.println("YouTubeSearchController start !");



        //instantiate an empty address object
        YouTubeSearchCriteria youtubeSearchCriteria = new YouTubeSearchCriteria();

        //put the object in the model
        model.addAttribute("youtubeSearchCriteria", youtubeSearchCriteria);

        youtubeSearchCriteria.setQueryTerm("IT+Designer");

        //get the list of YouTube videos that match the search term
        List<YouTubeSearchDto> videos = youTubeProvider.get(youtubeSearchCriteria.getQueryTerm());

        //put it in the model
        model.addAttribute("videos", videos);

        //return youTubeProvider.get(youtubeSearchCriteria.getQueryTerm());
        return videos;
        //return "index";
    }

}

