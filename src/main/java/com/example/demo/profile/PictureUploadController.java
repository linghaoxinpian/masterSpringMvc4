package com.example.demo.profile;

import com.example.demo.config.PictureUploadProperties;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

@Controller
public class PictureUploadController {
    private final Resource picturesDir;
    private  final Resource anonymousPicture;

    public PictureUploadController(PictureUploadProperties uploadProperties){
        picturesDir=uploadProperties.getUploadPath();
        anonymousPicture=uploadProperties.getAnonymousPicture();
    }

    //★这里是一个模型属性，在这个类的所有请求方法参数中加入 @ModelAttribute("picturePath") Path picturePath 这样一个参数，那么其方法中就可以获取到这个模型属性的值了
    @ModelAttribute("picturePath")
    public Resource picturePath(){
        return anonymousPicture;
    }

    @RequestMapping("upload")
    public String uploadPage() {
        return "profile/uploadPage";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        if(file.isEmpty()||!isImage(file)){
            redirectAttributes.addFlashAttribute("error","Incorrect file. Please upload a picture.");
            return "redirect:/upload";
        }

        copyFileToPictures(file);

        return "profile/uploadPage";
    }

    @RequestMapping(value = "/uploadedPicture")
    public void getUPloadedPicture(HttpServletResponse response,@ModelAttribute("picturePath") Resource picturePath) throws IOException {
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(anonymousPicture.getFilename()));
        IOUtils.copy(anonymousPicture.getInputStream(),response.getOutputStream());
        System.out.println("模型picturePath的值："+picturePath);
    }

    //储存图片到本地目录
    private void copyFileToPictures(MultipartFile file) throws IOException {
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
    }

    private boolean isImage(MultipartFile file) {
        return  file.getContentType().startsWith("image");
    }

}
