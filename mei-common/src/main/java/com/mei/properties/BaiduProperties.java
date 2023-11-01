package com.mei.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-11-01 09:52
 **/

@Component
@ConfigurationProperties(prefix = "mei.baidu")
@Data
public class BaiduProperties {
    private String url;
    private String ak;
    private String shopAddress;
}
