package com.epam.learn.ai.service.movieprompt;

import com.epam.learn.ai.service.ChatHistoryInstance;
import com.epam.learn.ai.service.history.SimpleKernelHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoviePromptRunner implements ApplicationRunner {

    private final SimpleKernelHistory simpleKernelHistory;

    private final ChatHistoryInstance chatHistoryInstance;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Function for providing prompts through the console for interaction with OpenAI using
     * ChatHistory to provide context about previous messages .
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        final var chatHistory = chatHistoryInstance.getChatHistory();

        while (true) {
            var input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                log.info("Thank you! If you have any more questions, feel free to ask");
                break;
            }

            if(StringUtils.isEmpty(input) || StringUtils.isWhitespace(input)) {
                log.error("Prompt is empty, please add your question!");
            } else {
                simpleKernelHistory.processWithHistory(input, chatHistory)
                        .doOnNext(output -> log.info("AI ANSWER: {}", output))
                        .subscribe();
            }

        }
    }
}
