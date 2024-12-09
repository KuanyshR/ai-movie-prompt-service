package com.epam.learn.ai.service.kernel;

import com.epam.learn.ai.model.GeneralResponse;
import com.epam.learn.ai.model.PromptExecSettingsProperties;
import reactor.core.publisher.Mono;

public interface KernelService {

    /**
     * In response generates answer for your question.
     *
     * @param prompt - ask everything .
     * @param execSettingsProperties - {@link PromptExecSettingsProperties} object containing deployment model name, temperature, resultsPerPrompt, maxTokens
     * @return returns {@link GeneralResponse} object containing prompt and output
     */
    Mono<GeneralResponse> processWithModel(String prompt, PromptExecSettingsProperties execSettingsProperties);
}
