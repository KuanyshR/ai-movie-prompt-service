package com.epam.learn.ai.controller.movieprompt;

import com.epam.learn.ai.model.MovieFactsResponse;
import com.epam.learn.ai.service.movieprompt.MovieFactsService;
import com.epam.learn.ai.service.movieprompt.MovieFactsServiceImpl;
import com.epam.learn.ai.service.movieprompt.MoviePromptService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The movie controller responsible for generating a response based on the prompt of the movie title.
 * The Semantic Kernel service is used.
 *
 */
@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class MoviePromptServiceV3Controller {

    public final MovieFactsService movieFactsService;

    /**
     * In response generates facts about the movie as response.
     *
     * @param prompt the prompt for the movie title.
     * @return returns {@link MovieFactsResponse} object containing prompt and movie facts (output)
     */
    @GetMapping("/ai/movieprompt")
    public Mono<MovieFactsResponse> getMoviePrompt(@RequestParam("prompt") String prompt) {
        return movieFactsService.getMoviePromptSemKernel(prompt);
    }
}
