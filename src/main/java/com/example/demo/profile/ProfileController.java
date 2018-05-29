package com.example.demo.profile;

import com.example.demo.dates.Zh_LocalDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
public class ProfileController {

    private UserProfileSession userProfileSession;
    @Autowired    //不知道构造函数这里的注释有什么作用，先注释了，错了再说...知道了，简单说下，这里是构造函数注入，等价于属性上使用@Autowired的域注入
    public ProfileController(UserProfileSession userProfileSession){
        this.userProfileSession=userProfileSession;
    }

    @ModelAttribute
    public ProfileForm getProfileForm(){
        return userProfileSession.toForm();
    }

    @ModelAttribute("dateFormat")   //ModelAttribute注解类似于model.addAttribute()方法，html页面只需添加一个占位符 th:placeholder="${dateFormat}"
    public String localeFormat(Locale locale){
        return Zh_LocalDateFormatter.getPattern(locale);
    }

    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm){
        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile",params = {"save"} ,method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "profile/profilePage";
        }
        userProfileSession.saveForm(profileForm);   //借助Spring mvc将数据存储到会话中
        System.out.println("save ok "+ profileForm);
        return "redirect:/profile";
    }

    @RequestMapping(value = "/profile",params = {"addTaste"})
    public String addRow(ProfileForm profileForm){
        profileForm.getTastes().add(null);
        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile",params = {"removeTaste"})
    public String removeRow(ProfileForm profileForm, HttpServletRequest request){
        Integer rowId=Integer.valueOf(request.getParameter("removeTaste"));
        profileForm.getTastes().remove(rowId.intValue());
        return "profile/profilePage";
    }
}
