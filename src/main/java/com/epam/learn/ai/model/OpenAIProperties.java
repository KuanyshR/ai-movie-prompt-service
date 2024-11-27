package com.epam.learn.ai.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "client.openai")
@Data
public class OpenAIProperties {

    private String key;
    private String endpoint;
    private String deploymentName;
}
