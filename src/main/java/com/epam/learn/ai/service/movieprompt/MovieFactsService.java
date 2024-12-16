package com.epam.learn.ai.service.movieprompt;

import com.epam.learn.ai.model.MovieFactsResponse;
import reactor.core.publisher.Mono;

public interface MovieFactsService {

    Mono<MovieFactsResponse> getMoviePromptSemKernel(String moviePrompt);
}
