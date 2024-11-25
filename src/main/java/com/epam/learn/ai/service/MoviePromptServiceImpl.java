package com.epam.learn.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.learn.ai.model.MovieFactsResponse;
import com.epam.learn.ai.model.OpenAIProperties;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoviePromptServiceImpl implements MoviePromptService {

    private final OpenAIProperties openAIProperties;

    private final OpenAIAsyncClient openAIAsyncClient;

    private final Kernel kernel;

    private final InvocationContext invocationContext;

    @Override
    public Mono<MovieFactsResponse> getMoviePromptOpenAI(String moviePrompt) {

        var movieFactsPrompt = addMovieFactsString(moviePrompt);

        var chatCompletionsOptions = new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(movieFactsPrompt)));

        return openAIAsyncClient.getChatCompletions(openAIProperties.getDeploymentName(), chatCompletionsOptions)
                .map(this::getChatCompletions)
                .map(outputs -> MovieFactsResponse.builder()
                        .promptInput(moviePrompt)
                        .output(outputs)
                        .build());
    }

    @Override
    public Mono<MovieFactsResponse> getMoviePromptSemKernel(String moviePrompt) {
        return kernel.invokePromptAsync(moviePrompt)
                .withPromptExecutionSettings(invocationContext.getPromptExecutionSettings())
                .map(this::getFunctionResult)
                .map(output -> MovieFactsResponse.builder()
                        .promptInput(moviePrompt)
                        .output(output)
                        .build());
    }

    private String getChatCompletions(ChatCompletions chatCompletions) {
        return chatCompletions.getChoices()
                .stream()
                .map(chatChoice -> chatChoice.getMessage().getContent())
                .collect(Collectors.joining(" "));
    }

    private String addMovieFactsString(String prompt) {
        return String.format("Please provide interesting facts about the film titled: %s", "/" +prompt + "/");
    }

    private String getFunctionResult(FunctionResult<Object> result) {
        return Optional.ofNullable(result.getResult())
                .map(Object::toString)
                .orElseThrow(() -> new RuntimeException("Not found with following prompt"));
    }
}
