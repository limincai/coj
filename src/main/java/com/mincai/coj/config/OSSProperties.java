package com.mincai.coj.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
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
