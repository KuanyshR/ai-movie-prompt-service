package com.epam.learn.ai.controller.general;

import com.epam.learn.ai.model.GeneralResponse;
import com.epam.learn.ai.model.PromptExecSettingsProperties;
import com.epam.learn.ai.service.kernel.KernelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The general controller responsible for generating a response answer based on the prompt.
 * The Semantic Kernel service is used.
 *
 */
@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class GeneralPromptServiceController {

    private final KernelService kernelService;

    /**
     * In response generates answer for your question.
     *
     * @param prompt - ask everything .
     * @return returns {@link GeneralResponse} object containing prompt and output
     */
    @GetMapping("/ai/general")
    public Mono<GeneralResponse> chat(
            @RequestParam("prompt") String prompt,
            @RequestParam("model") String model,
            @RequestParam("temperature") String temperature) {

        var execSettingsProperties = PromptExecSettingsProperties.builder()
                .modelId(model)
                .temperature(Double.valueOf(temperature))
                .build();

        return kernelService.processWithModel(prompt, execSettingsProperties);
    }
}
