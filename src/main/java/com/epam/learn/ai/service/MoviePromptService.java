package com.epam.learn.ai.service;

import com.epam.learn.ai.model.MovieFactsResponse;
import reactor.core.publisher.Mono;

public interface MoviePromptService {

    /**
     * In response generates facts about the movie as response.
     *
     * @param moviePrompt the prompt for the movie title.
     * @return returns {@link MovieFactsResponse} object containing prompt and movie facts (output)
     */
    Mono<MovieFactsResponse> getMoviePromptOpenAI(String moviePrompt);

    /**
     * In response generates facts about the movie as response.
     *
     * @param moviePrompt the prompt for the movie title.
     * @return returns {@link MovieFactsResponse} object containing prompt and movie facts (output)
     */
    Mono<MovieFactsResponse> getMoviePromptSemKernel(String moviePrompt);
}
