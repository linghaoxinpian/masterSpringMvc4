package com.example.demo.profile;


import com.example.demo.dates.PastLocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileForm {

    @Size(min = 2,message = "Please specify a valid twitter handle")
    private String twitterHandle;

    @Email
    @NotEmpty
    private String email;

    @NotNull
    @PastLocalDate
    private Date birthDate;

    private List<String> tastes=new ArrayList<>();


    //---------------属性---------------

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
