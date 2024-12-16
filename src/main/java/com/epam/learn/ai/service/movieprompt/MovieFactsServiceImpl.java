package com.epam.learn.ai.service.movieprompt;

import com.epam.learn.ai.model.MovieFactsResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieFactsServiceImpl implements MovieFactsService {

    private final Kernel kernelWithPlugin;

    private final InvocationContext invocationContext;

    private final ChatCompletionService chatCompletionService;

    @Override
    public Mono<MovieFactsResponse> getMoviePromptSemKernel(String moviePrompt) {
        return chatCompletionService.getChatMessageContentsAsync(moviePrompt, kernelWithPlugin, invocationContext)
                .map(chatMessageContents ->
                        extractResponse(moviePrompt, chatMessageContents));
    }

    private MovieFactsResponse extractResponse(String prompt, List<ChatMessageContent<?>> responseContent) {
        var output = responseContent.stream()
                .map(ChatMessageContent::getContent)
                .collect(Collectors.joining());

        return MovieFactsResponse.builder()
                .promptInput(prompt)
                .output(output).build();
    }
}
