package com.epam.learn.ai.service.embedding;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.learn.ai.model.embedding.EmbeddingResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EmbeddingModelService {

    Mono<List<EmbeddingItem>> getEmbeddings(String text);

    Flux<EmbeddingResponse> search(String text);

    void processAndSaveText(String text);

    Flux<String> searchText(String text);
}
