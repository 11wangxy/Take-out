package Reggie.controller;


import Reggie.common.Result;
import Reggie.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@RestController
@RequestMapping("/common")
public class uploadController {
    @Autowired
    private AliOSSUtils aliOSSUtils;

    //存储云服务
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //调用阿里云oss工具类
        String s = aliOSSUtils.upload(file);
        return Result.success(s);
    }

//    @GetMapping("/download")
//    public void download(String name, HttpServletResponse response) throws Exception{
//        log.info("文件下载  {}", name);
//        String download = aliOSSUtils.download(name);
//        log.info("文件路径{}",download);
//        FileInputStream fileInputStream = new FileInputStream(new File(download));
//        ServletOutputStream outputStream = response.getOutputStream();
//        response.setContentType("image/jpg");
//        int len = 0;
//        byte[] bytes = new byte[1024];
//        while ((fileInputStream.read(bytes)) != -1) {
//            outputStream.write(bytes, 0, len);
//            outputStream.flush();
//        }
//        outputStream.close();
//        fileInputStream.close();
//    }
}
