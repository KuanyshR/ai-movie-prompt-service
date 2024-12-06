package com.epam.learn.ai.service.kernel;

import com.epam.learn.ai.exception.ModelNotFoundException;
import com.epam.learn.ai.model.GeneralResponse;
import com.epam.learn.ai.model.PromptExecSettingsProperties;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KernelServiceImpl implements KernelService{

    private final Map<String, ChatCompletionService> completionServiceMap;

    @Override
    public Mono<GeneralResponse> processWithModel(String prompt, PromptExecSettingsProperties execSettingsProperties){

        var modelId = execSettingsProperties.modelId();

        var chatCompletionService = Optional.ofNullable(completionServiceMap.get(modelId))
                .orElseThrow(() -> {
                    var errorMessage = "ChatCompletionService not found by model id: %s";
                    String error = String.format(errorMessage, modelId);
                    return new ModelNotFoundException(error);
                });

        var promptExecutionSettings = promptExecutionSettings(execSettingsProperties);

        return getKernel(chatCompletionService).invokePromptAsync(prompt)
                .withPromptExecutionSettings(promptExecutionSettings)
                .map(result -> getFunctionResult(prompt, result));

    }

    private Kernel getKernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    private PromptExecutionSettings promptExecutionSettings(PromptExecSettingsProperties execSettingsProperties) {

        var modelId = Optional.of(execSettingsProperties.modelId()).orElse("gpt-35-turbo");
        var temperature = Optional.of(execSettingsProperties.temperature()).orElse(1.0);

        log.info("Current deployment model is: {}", modelId);

        return PromptExecutionSettings.builder()
                .withModelId(modelId)
                .withTemperature(temperature)
                .build();
    }

    private GeneralResponse getFunctionResult(String prompt, FunctionResult<Object> result) {
        var output = Optional.ofNullable(result.getResult())
                .map(Object::toString)
                .orElseThrow(() -> new RuntimeException("Not found with following prompt"));

        return GeneralResponse.builder()
                .promptInput(prompt)
                .output(output)
                .build();
    }
}
