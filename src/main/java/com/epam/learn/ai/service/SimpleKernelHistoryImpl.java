package com.epam.learn.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.semanticfunctions.*;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service class for interacting with the AI kernel, maintaining chat history.
 * <p>
 * This service provides a method to process user prompts while preserving chat history.
 * It uses the {@link Kernel} to invoke AI responses based on the user's input and the
 * previous chat context. The conversation history is updated after each interaction.
 */

@Slf4j
@Service
@AllArgsConstructor
public class SimpleKernelHistoryImpl implements SimpleKernelHistory {

    private final Kernel kernel;

    private final InvocationContext invocationContext;

    /**
     * Creates interaction function with arguments with the user prompt and chat history.
     *
     * @param prompt the user's input
     * @param chatHistory the current chat history
     */
    public Mono<String> processWithHistory(String prompt, ChatHistory chatHistory) {
        KernelFunctionArguments arguments = getKernelFunctionArguments(prompt, chatHistory);
        return kernel.invokeAsync(getChat())
                .withPromptExecutionSettings(invocationContext.getPromptExecutionSettings())
                .withArguments(arguments)
                .doOnNext(stringFunctionResult -> {
                    ChatMessageContent<String> messageContent = new ChatMessageContent<>(AuthorRole.SYSTEM, prompt);
                    chatHistory.addMessage(messageContent);
                })
                .map(FunctionResult::getResult);
    }

    /**
     * Creates a kernel function for generating a chat response using a predefined prompt template.
     * <p>
     * The template includes the chat history and the user's message as variables.
     *
     * @return a {@link KernelFunction} for handling chat-based AI interactions
     */
    private KernelFunction<String> getChat() {
        return KernelFunction.<String>createFromPrompt("""
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
                .build();
    }


    /**
     * Creates the kernel function arguments with the user prompt and chat history.
     *
     * @param prompt the user's input
     * @param chatHistory the current chat history
     * @return a {@link KernelFunctionArguments} instance containing the variables for the AI model
     */
    private KernelFunctionArguments getKernelFunctionArguments(String prompt, ChatHistory chatHistory) {
        return KernelFunctionArguments.builder()
                .withVariable("request", prompt)
                .withVariable("chatHistory", chatHistory)
                .build();
    }
}
