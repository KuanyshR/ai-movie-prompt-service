package com.epam.learn.ai.model;

import lombok.Builder;

@Builder
public record GeneralResponse(String promptInput, String output) {
}
