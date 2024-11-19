package com.mincai.coj.config;

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

    private String bucket;

    private String accessKey;

    private String secretAccessKey;

}
