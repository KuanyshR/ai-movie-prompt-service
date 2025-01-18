package com.epam.learn.ai.service.rag;

import com.epam.learn.ai.model.GeneralResponse;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

public interface RagService {

    /**
     * Uploads the file to the vector data base
     *
     * @param file the file to upload
     */
    void savePDFContent(FilePart file);

    /**
     * Generates an answer for the given prompt
     *
     * @param prompt the query to generate an answer
     * @return the generated answer
     */
    Flux<GeneralResponse> getAnswerResponse(String prompt);
}
