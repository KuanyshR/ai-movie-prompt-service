package com.epam.learn.ai.service;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.stereotype.Component;

@Component
public class ChatHistoryInstance {

    private ChatHistory chatHistory;

    public ChatHistory getChatHistory() {
        if (chatHistory == null) {
            chatHistory = new ChatHistory();
        }
        return chatHistory;
    }
}
