package com.epam.learn.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.learn.ai.model.OpenAIProperties;
import com.epam.learn.ai.service.plugin.ActorsByGenrePlugin;
import com.epam.learn.ai.service.plugin.AlienMovieFactsPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up Semantic Kernel components.
 * <p>
 * This configuration provides several beans necessary for the interaction with
 * Azure OpenAI services and the creation of kernel plugins. It defines beans for
 * chat completion services, kernel plugins, kernel instance, invocation context,
 * and prompt execution settings.
 */
@Configuration
@RequiredArgsConstructor
public class SemanticKernelConfiguration {

    private final OpenAIProperties openAIProperties;

    /**
     * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
     *
     * @param openAIAsyncClient the {@link OpenAIAsyncClient} to communicate with Azure OpenAI
     * @return an instance of {@link ChatCompletionService}
     */
    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(openAIProperties.getDeploymentName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     *
     * @param chatCompletionService the {@link ChatCompletionService} for handling completions
     * @return an instance of {@link Kernel}
     */
    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     *
     * @param chatCompletionService the {@link ChatCompletionService} for handling completions
     * @return an instance of {@link Kernel}
     */
    @Bean
    public Kernel kernelWithPlugin(ChatCompletionService chatCompletionService,
                                   KernelPlugin actorsByGenrePlugin,
                                   KernelPlugin alienMovieFactsPlugin)
    {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(actorsByGenrePlugin)
                .withPlugin(alienMovieFactsPlugin)
                .build();
    }

    /**
     * Creates an {@link InvocationContext} bean with default prompt execution settings.
     *
     * @return an instance of {@link InvocationContext}
     */
    @Bean
    public InvocationContext invocationContext() {
        PromptExecutionSettings promptExecutionSettings = PromptExecutionSettings.builder()
                .withResultsPerPrompt(1)
                .withTemperature(1.0)
                .build();
        return InvocationContext.builder()
                .withPromptExecutionSettings(promptExecutionSettings)
                .build();
    }

    /**
     * Creates a map of {@link ChatCompletionService} for different models.     *
     * @return a map of model names to {@link ChatCompletionService}
     */
    @Bean
    public Map<String, ChatCompletionService> completionServiceMap(OpenAIAsyncClient openAIAsyncClient) {
        var completionServiceMap = new HashMap<String, ChatCompletionService>();
        openAIProperties.getModelList()
                .forEach(modelId -> {
                    completionServiceMap.put(modelId, OpenAIChatCompletion.builder()
                            .withModelId(modelId)
                            .withOpenAIAsyncClient(openAIAsyncClient).build());
                });
        return completionServiceMap;
    }

    /**
     * Creates a {@link KernelPlugin} bean for get actors by genre.
     *
     * @return an instance of {@link KernelPlugin}
     */
    @Bean
    public KernelPlugin actorsByGenrePlugin() {
        return KernelPluginFactory.createFromObject(new ActorsByGenrePlugin(), "ActorsByGenrePlugin");
    }

    /**
     * Creates a {@link KernelPlugin} bean for get alien movie facts.
     *
     * @return an instance of {@link KernelPlugin}
     */
    @Bean
    public KernelPlugin alienMovieFactsPlugin() {
        return KernelPluginFactory.createFromObject(new AlienMovieFactsPlugin(), "AlienMovieFactsPlugin");
    }
}
