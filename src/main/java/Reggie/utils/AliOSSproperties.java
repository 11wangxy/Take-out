package Reggie.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSSproperties {
    String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;
}
