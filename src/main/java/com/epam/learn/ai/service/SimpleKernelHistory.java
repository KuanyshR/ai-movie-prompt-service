package com.epam.learn.ai.service;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import reactor.core.publisher.Mono;

public interface SimpleKernelHistory {

    Mono<String> processWithHistory(String prompt, ChatHistory chatHistory);
}
