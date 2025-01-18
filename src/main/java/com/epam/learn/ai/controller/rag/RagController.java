package com.epam.learn.ai.controller.rag;

import com.epam.learn.ai.model.GeneralResponse;
import com.epam.learn.ai.service.rag.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping("/upload")
    public void upload(@RequestPart("file") Mono<FilePart> filePartMono) {
        filePartMono
                .doOnNext(ragService::savePDFContent)
                .subscribe();
    }

    @GetMapping("/prompt")
    public Flux<GeneralResponse> getRagAnswer(@RequestParam("query") String query) {
        return ragService.getAnswerResponse(query);
    }
}
