package com.example.demo.controller;

import com.example.demo.profile.UserProfileSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private UserProfileSession userProfileSession;

    @Autowired
    public HomeController(UserProfileSession userProfileSession){
        this.userProfileSession=userProfileSession;
    }

    @RequestMapping("/")
    public String name(){
        List<String> tastes=userProfileSession.getTastes();
        if(tastes.isEmpty()){
            return "redirect:/search/mixed;keywords=apple,strawberry";
        }
        return "redirect:/search/mixed;keywords=apple,strawberry";
    }
}
