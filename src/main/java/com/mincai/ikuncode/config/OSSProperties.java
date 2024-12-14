package com.mincai.ikuncode.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 oss 属性配置
 *
 * @author limincai
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OSSProperties {

    private String endpoint;

    private String prefix;

    private String accessKey;

    private String secretAccessKey;

    private String depositoryName;

    private String bucketName;

}
