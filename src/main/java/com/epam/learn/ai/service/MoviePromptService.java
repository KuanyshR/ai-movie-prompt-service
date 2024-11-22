package com.epam.learn.ai.service;

import com.epam.learn.ai.model.MovieFactsResponse;
import reactor.core.publisher.Mono;

public interface MoviePromptService {

    Mono<MovieFactsResponse> getMoviePromptOpenAI(String moviePrompt);

    Mono<MovieFactsResponse> getMoviePromptSemKernel(String moviePrompt);
}
