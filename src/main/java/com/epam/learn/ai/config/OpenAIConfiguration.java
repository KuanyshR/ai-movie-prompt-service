package com.epam.learn.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.learn.ai.model.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenAIConfiguration {

    private final OpenAIProperties openAIProperties;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openAIProperties.getKey()))
                .endpoint(openAIProperties.getEndpoint())
                .buildAsyncClient();
    }
}
