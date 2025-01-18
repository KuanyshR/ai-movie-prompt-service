package com.epam.learn.ai.service.rag;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.learn.ai.model.GeneralResponse;
import com.epam.learn.ai.service.embedding.EmbeddingModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagServiceImpl implements RagService {

    @Value("${client.openai.deployment_name}")
    private String deploymentModelName;

    private final OpenAIAsyncClient openAIAsyncClient;

    private final EmbeddingModelService embeddingModelService;

    @Override
    public void savePDFContent(FilePart file) {
        var subscribe = file.content()
                .map(dataBuffer -> {
                    try (PDDocument pdfDocument = PDDocument.load(dataBuffer.asInputStream())) {
                        var pdfStripper = new PDFTextStripper();
                        return pdfStripper.getText(pdfDocument);
                    } catch (IOException e) {
                        log.error("Error occurred during PDF parsing: {}", e.getMessage());
                        return "";
                    }
                })
                .doOnNext(embeddingModelService::processAndSaveText).subscribe();
        log.info("Document saved with result: {}", subscribe.isDisposed());
    }

    @Override
    public Flux<GeneralResponse> getAnswerResponse(String prompt) {
        return embeddingModelService.searchText(prompt)
                .flatMap(this::getAnswer);
    }

    private Mono<GeneralResponse> getAnswer(String query) {
         return openAIAsyncClient.getChatCompletions(
                        deploymentModelName,
                        new ChatCompletionsOptions((List.of(new ChatRequestUserMessage(query)))))
                .map(this::getChatCompletions)
                .map(output -> GeneralResponse.builder()
                        .promptInput(query)
                        .output(output).build());
    }

    private String getChatCompletions(ChatCompletions chatCompletions) {
        return chatCompletions.getChoices()
                .stream()
                .map(chatChoice -> chatChoice.getMessage().getContent())
                .collect(Collectors.joining(" "));
    }
}
