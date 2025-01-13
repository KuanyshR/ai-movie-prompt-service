package com.epam.learn.ai.model.embedding;

import lombok.Builder;

@Builder
public record EmbeddingResponse(String id, Float score) {
}
