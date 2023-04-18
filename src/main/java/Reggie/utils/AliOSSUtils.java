package Reggie.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.ClientException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 */
@Component
//使用注解把对象交给IOC管理，取代new生成对象
public class AliOSSUtils {

    //    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
//    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//    private String accessKeyId = "LTAI5t9XJ494wcipCfhm9ukC";
//    private String accessKeySecret = "rKLkFj9KCciMrKy9tS8L4JwYqfemur";
//    // 填写Bucket名称，例如examplebucket。
//    private String bucketName = "spring-talis";
    @Autowired
    private AliOSSproperties aliOSSproperties;

    /**
     * 实现上传图片到OSS
     */

    public String upload(MultipartFile file) throws IOException {
        //获取阿里云oss参数
        String endpoint = aliOSSproperties.getEndpoint();
        String bucketName = aliOSSproperties.getBucketName();
        String accessKeySecret = aliOSSproperties.getAccessKeySecret();
        String accessKeyId = aliOSSproperties.getAccessKeyId();
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }


    public String download(String objectName) throws Exception { // 获取阿里云oss参数
        String endpoint = aliOSSproperties.getEndpoint();
        String bucketName = aliOSSproperties.getBucketName();
        String accessKeySecret = aliOSSproperties.getAccessKeySecret();
        String accessKeyId = aliOSSproperties.getAccessKeyId(); // 要下载的文件本地保存路径
        String filename = objectName.substring(objectName.lastIndexOf('/')+1);
        String pathName = "D:\\" + filename;

        // 下载文件到本地
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.getObject(new GetObjectRequest(bucketName, filename), new File(pathName));

       // 返回下载链接
        return pathName;
    }
}


