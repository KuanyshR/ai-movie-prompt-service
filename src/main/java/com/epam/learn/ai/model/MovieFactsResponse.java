package com.epam.learn.ai.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MovieFactsResponse {

    private String promptInput;
    private String output;
}
