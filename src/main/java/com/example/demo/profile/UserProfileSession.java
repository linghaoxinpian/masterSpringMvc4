package com.example.demo.profile;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)  //target_class使用cglib代理，interfaces使用jdk代理 no不使用任何代理
public class UserProfileSession implements Serializable {   //实现Serializable不是必须的，因为HTTP会话能够将任意对象存储在内存中，不过让存储在会话中的对象支持序列化是一种好的实践

    private String twitterHandle;
    private String email;
    private Date birthDate;
    private List<String> tastes=new ArrayList<>();
    private URL picturePath;


    public void saveForm(ProfileForm profileForm){
        this.twitterHandle=profileForm.getTwitterHandle();
        this.email= profileForm.getEmail();
        this.birthDate=profileForm.getBirthDate();
        this.tastes=profileForm.getTastes();
    }

    public ProfileForm toForm(){
        ProfileForm profileForm=new ProfileForm();
        profileForm.setTwitterHandle(this.twitterHandle);
        profileForm.setEmail(email);
        profileForm.setBirthDate(birthDate);
        profileForm.setTastes(tastes);

        return profileForm;
    }

//------------------属性--------------------
    public Resource getPicturePath() {
        return picturePath==null?null:new UrlResource(picturePath);
    }

    public void setPicturePath(Resource picturePath) throws IOException {
        this.picturePath = picturePath.getURL();
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getTastes() {
        return tastes;
    }

    public void setTastes(List<String> tastes) {
        this.tastes = tastes;
    }
}
