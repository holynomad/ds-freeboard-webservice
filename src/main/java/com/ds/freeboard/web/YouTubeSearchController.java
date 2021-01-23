package com.ds.freeboard.web;

import com.ds.freeboard.domain.posts.YouTubeRepository;
import com.ds.freeboard.domain.posts.YouTubeSearchCriteria;
import com.ds.freeboard.web.dto.YouTubeSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class YouTubeSearchController {

    private final YouTubeRepository youTubeProvider;
    private YouTubeSearchCriteria youtubeSearchCriteria;

    @Autowired
    public YouTubeSearchController(
            final YouTubeRepository youTubeProvider
    ) {
        this.youTubeProvider = youTubeProvider;
    }

    //starting page for YouTube api demo
/*
    @GetMapping(value = "/youtube")
    public String Index (Model model){
        //instantiate an empty address object
        YouTubeSearchCriteria youtubeSearchCriteria = new YouTubeSearchCriteria();

        //put the object in the model
        model.addAttribute("youtubeSearchCriteria", youtubeSearchCriteria);

        //get out
        // error point !!!
        return "index";
    }
*/

    @ResponseBody
    @PostMapping(value = "/youtube/{searchKeyword}")
    //@RequestMapping(value = "/youtube", method = RequestMethod.GET)
    public List<YouTubeSearchDto> resultSubmit(@Valid YouTubeSearchCriteria youTubeSearchCriteria, @PathVariable(value = "searchKeyword") String searchKeyword, Model model) {
    //public String resultSubmit(@Valid YouTubeSearchCriteria youTubeSearchCriteria, @PathVariable(value = "searchKeyword") String searchKeyword, Model model) {
    //public List<YouTubeSearchDto> resultSubmit(@Valid YouTubeSearchCriteria youTubeSearchCriteria, String searchKeyword, Model model) {
    //public String resultSubmit(@PathVariable String searchKeyword, Model model) {
    //public String demo(Model model) {

        System.out.println("=================================");
        System.out.println("YouTubeSearchController start ! ---> " + searchKeyword);

        //instantiate an empty address object
        YouTubeSearchCriteria youtubeSearchCriteria = new YouTubeSearchCriteria();

        youtubeSearchCriteria.setQueryTerm(searchKeyword);

        //get the list of YouTube videos that match the search term
        List<YouTubeSearchDto> videos = youTubeProvider.get(youtubeSearchCriteria.getQueryTerm());

        //put it in the model
        model.addAttribute("videos", videos);

        // logging
        //System.out.println(model.toString());

        //return youTubeProvider.get(youtubeSearchCriteria.getQueryTerm());
        return videos;
        //return searchKeyword;
        //return "show-yt-results";
    }

}

