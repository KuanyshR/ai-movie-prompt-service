package com.epam.learn.ai.model;

import lombok.Builder;

@Builder
public record ErrorResponse(Integer code, String message) {
}
