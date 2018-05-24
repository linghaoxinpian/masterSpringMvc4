package com.example.demo.profile;

import com.example.demo.dates.Zh_LocalDateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class ProfileController {

    @ModelAttribute("dateFormat")   //ModelAttribute注解类似于model.addAttribute()方法，html页面只需添加一个占位符 th:placeholder="${dateFormat}"
    public String localeFormat(Locale locale){
        return Zh_LocalDateFormatter.getPattern(locale);
    }

    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm){
        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile",method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm){
        System.out.println("save ok "+ profileForm);
        return "redirect:/profile";
    }
}
