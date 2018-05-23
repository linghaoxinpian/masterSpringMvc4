package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TweetController {

//    @Autowired
//    private Twitter twitter;

    @RequestMapping("/")
    public String home(){
        return "searchPage";
    }

    @RequestMapping(value = "/postSearch",method = RequestMethod.POST)
    public String postSearch(HttpServletRequest request, RedirectAttributes redirectAttributes){    //star:RedirectAttributes类用于redirect场景下传送值
        String search=request.getParameter("search");
        if(search.toLowerCase().contains("struts")){
            redirectAttributes.addFlashAttribute("error","Try using spring instead!");
            return "redirect:/";
        }
        redirectAttributes.addAttribute("search",search);
        return "redirect:result";
    }

    @RequestMapping("/result")
    public String hello(@RequestParam(defaultValue="masterSpringMVC4") String search, Model model){

        model.addAttribute("tweets",null);
        model.addAttribute("search",search);
        return "resultPage";
    }
}
