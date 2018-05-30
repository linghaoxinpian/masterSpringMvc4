package com.example.demo.search.api;

import com.example.demo.profile.ProfileForm;
import com.example.demo.profile.UserProfileSession;
import com.example.demo.search.LightTweet;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {

    @RequestMapping(value = "/{searchType}",method = RequestMethod.GET)
    public List<LightTweet> search(@PathVariable String searchType, @MatrixVariable List<String> keywords){
        System.out.println("searchType值为："+searchType);
        System.out.println("keywords值为："+keywords);
       List<LightTweet> lightTweetList=new ArrayList<>();
       lightTweetList.add(new LightTweet("text1"));
       lightTweetList.add(new LightTweet("text2"));
        return lightTweetList;
    }
}

