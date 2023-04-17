package Reggie.controller;


import Reggie.common.Result;
import Reggie.utils.AliOSSUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;

@Slf4j
@RestController
@RequestMapping("/common")
public class uploadController {
    @Autowired
    private AliOSSUtils aliOSSUtils;

    //存储云服务
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传{}", file.getOriginalFilename());
        //调用阿里云oss工具类
        String s = aliOSSUtils.upload(file);
        log.info("文件已经上传完毕");
        return Result.success(s);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse) throws IOException {
        log.info("文件下载{}", name);
       try{
           FileInputStream fileInputStream = new FileInputStream(new File(name));
           ServletOutputStream outputStream = httpServletResponse.getOutputStream();

           httpServletResponse.setContentType("image/jpg");
           int len =0;
           byte[] bytes = new byte[1024];
           while ((fileInputStream.read(bytes))!=-1){
               outputStream.write(bytes, 0 ,len);
               outputStream.flush();
           }
           outputStream.close();
           fileInputStream.close();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}