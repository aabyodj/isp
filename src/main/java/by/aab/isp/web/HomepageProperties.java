package by.aab.isp.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "homepage")
@Data
public class HomepageProperties {

    private int promotionsCount = 3;
    private int tariffsCount = 3;
}
