package com.example.demo.profile;

import com.example.demo.config.PictureUploadProperties;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.Locale;

@Controller
public class PictureUploadController {
    private final Resource picturesDir;
    private  final Resource anonymousPicture;
    private final MessageSource messageSource;
    private final UserProfileSession userProfileSession;

    @Autowired    //第4章有这个，暂时不加，弄清原因或报错再加...知道了，简单说下，这里是构造函数注入，等价于属性上使用@Autowired的域注入
    public PictureUploadController(PictureUploadProperties uploadProperties,MessageSource messageSource,UserProfileSession userProfileSession){
        picturesDir=uploadProperties.getUploadPath();
        anonymousPicture=uploadProperties.getAnonymousPicture();
        this.messageSource=messageSource;
        this.userProfileSession=userProfileSession;
    }

    @ModelAttribute("userProfileSession")
    public UserProfileSession userProfileSession(){
        return userProfileSession;
    }

    //处理上传的文件并跳转
    @RequestMapping(value = "/profile",params = {"upload"},method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        if(file.isEmpty()||!isImage(file)){
            redirectAttributes.addFlashAttribute("error","Incorrect file. Please upload a picture.");
            return "redirect:/profile";
        }

        Resource picturePath = copyFileToPictures(file);
        userProfileSession.setPicturePath(picturePath);

        return "redirect:profile";
    }

    @RequestMapping(value = "/uploadedPicture")
    public void getUPloadedPicture(HttpServletResponse response/*,@ModelAttribute("picturePath") Resource picturePath*/) throws IOException {
        Resource picturePath=userProfileSession.getPicturePath();
        if(picturePath==null){
            picturePath=anonymousPicture;
        }
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.toString()));
        IOUtils.copy(picturePath.getInputStream(),response.getOutputStream());
        System.out.println("模型picturePath的值："+picturePath);
    }

    //MultipartException异常处理
    @RequestMapping("uploadError")
    public ModelAndView onUploadError(Locale locale){
        ModelAndView modelAndView=new ModelAndView("profile/uploadPage");
        modelAndView.addObject("error",messageSource.getMessage("upload.file.too.big",null,locale));
        return modelAndView;
    }

    //储存图片到本地目录
    private Resource copyFileToPictures(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileExtension=filename.substring(filename.lastIndexOf("."));
        System.out.println("原始文件名：" + filename);
        File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());

        //try...with代码块将会自动关闭流，即便出现异常也是,从而移除了finally代码块
        try (
                InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream((tempFile))
        ) {
            IOUtils.copy(in, out);
        }
        return new FileSystemResource(tempFile);
    }

    private boolean isImage(MultipartFile file) {
        return  file.getContentType().startsWith("image");
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(Locale locale){
        ModelAndView modelAndView=new ModelAndView(("profile/profilePage"));
        modelAndView.addObject("error",messageSource.getMessage("upload.io.exception",null,locale));    //这里的locale是：lang=en,会去message_en.properties文件里去查找
        return modelAndView;
    }
}
