package com.ds.freeboard.web;

import com.ds.freeboard.domain.posts.YouTubeSearchCriteria;
import com.ds.freeboard.domain.posts.YouTubeVideo;
import com.ds.freeboard.service.posts.YouTubeSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class YouTubeDemoController {

    @Autowired
    private YouTubeSearchService youtubeService;


    //starting page for YouTube api demo

    @RequestMapping(value = "/youtubeDemo", method = RequestMethod.GET)
    //@ResponseBody
    //@PostMapping(value = "/youtubeDemo/{searchKeyword}")
    //public String youtubeDemo(Model model, @PathVariable(value = "searchKeyword") String searchKeyword) {
    public String youtubeDemo(Model model) {
        //System.out.println("=================================");
        //System.out.println("[/youtubeDemo] YouTubeSearchController start ! ---> " + searchKeyword);

        //instantiate an empty address object
        YouTubeSearchCriteria youtubeSearchCriteria = new YouTubeSearchCriteria();

        //put the object in the model
        model.addAttribute("youtubeSearchCriteria", youtubeSearchCriteria);

        //get out
        //return "youtubeDemo";
        return "show-yt-results";
        //return "index";
    }


    @RequestMapping(value = "/youtubeDemo", method = RequestMethod.POST)
    public String formSubmit(@Valid YouTubeSearchCriteria youtubeSearchCriteria, BindingResult bindingResult, Model model) {

        //check for errors
        if (bindingResult.hasErrors()) {
            //return "youtubeDemo";
            return "index";
        }

        //get the list of YouTube videos that match the search term
        List<YouTubeVideo> videos = youtubeService.fetchVideosByQuery(youtubeSearchCriteria.getQueryTerm());

        if (videos != null && videos.size() > 0) {
            model.addAttribute("numberOfVideos", videos.size());
        } else {
            model.addAttribute("numberOfVideos", 0);
        }

        //put it in the model
        model.addAttribute("videos", videos);

        //add the criteria to the model as well
        model.addAttribute("youtubeSearchCriteria", youtubeSearchCriteria);

        //get out
        return "show-yt-results";
        //return model.toString();
    }


    //redirect to demo if user hits the root
    @RequestMapping("/")
    public String home(Model model) {
        //return "redirect:youtube";
        return "index";
    }
}
