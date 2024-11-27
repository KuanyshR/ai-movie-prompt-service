package com.epam.learn.ai.controller;

import com.epam.learn.ai.model.MovieFactsResponse;
import com.epam.learn.ai.service.MoviePromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The movie controller responsible for generating a response based on the prompt of the movie title.
 * The OpenAI service is used.
 *
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MoviePromptServiceV1Controller {

    public final MoviePromptService moviePromptService;

    /**
     * In response generates facts about the movie as response.
     *
     * @param prompt the prompt for the movie title.
     * @return returns {@link MovieFactsResponse} object containing prompt and movie facts (output)
     */
    @GetMapping("/ai/movieprompt")
    public Mono<MovieFactsResponse> getMoviePrompt(@RequestParam("prompt") String prompt) {
        return moviePromptService.getMoviePromptOpenAI(prompt);
    }
}
