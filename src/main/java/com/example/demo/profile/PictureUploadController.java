package com.example.demo.profile;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;

@Controller
public class PictureUploadController {
    public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");

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

    //储存图片到本地目录
    private void copyFileToPictures(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        System.out.println("原始文件名：" + filename);
        File tempFile = File.createTempFile("pic", filename.substring(filename.lastIndexOf(".")), PICTURES_DIR.getFile());

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
