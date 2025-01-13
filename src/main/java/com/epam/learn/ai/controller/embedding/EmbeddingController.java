package com.epam.learn.ai.controller.embedding;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.learn.ai.model.embedding.EmbeddingResponse;
import com.epam.learn.ai.service.embedding.EmbeddingModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/embedding")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingModelService embeddingService;

    @PostMapping("/get")
    public Mono<List<EmbeddingItem>> getEmbedding(@RequestParam String text) {
        return embeddingService.getEmbeddings(text);
    }


    @PostMapping("/save")
    public void save(@RequestParam String text) {
        embeddingService.processAndSaveText(text);
    }


    @GetMapping("/search")
    public Flux<EmbeddingResponse> search(@RequestParam String text) {
        return embeddingService.search(text);
    }
}
