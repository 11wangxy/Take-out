package Reggie;

import Reggie.common.Result;
import Reggie.utils.AliOSSUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@SpringBootTest
@RequestMapping("/common")
public class aliyunOSSTest {
    @Autowired
    private AliOSSUtils aliOSSUtils;


    //存储云服务
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //调用阿里云oss工具类
        String s = aliOSSUtils.upload(file);
        return Result.success(s);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception{
        log.info("文件下载  {}", name);
        String download = aliOSSUtils.download(name);
        log.info("文件路径{}",download);
        FileInputStream fileInputStream = new FileInputStream(new File(download));
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpg");
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }

    @DeleteMapping("delete")
    public void delete(String name) throws Exception {
        log.info("文件{}删除操作",name);
        Result<String> delete = aliOSSUtils.delete(name);
        log.info("文件删除成功");
    }
}
