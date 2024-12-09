package com.epam.learn.ai.model;

import lombok.Builder;

@Builder
public record PromptExecSettingsProperties (
        String modelId,
        Double temperature,
        Integer resultsPerPrompt,
        Integer maxTokens) {
}
