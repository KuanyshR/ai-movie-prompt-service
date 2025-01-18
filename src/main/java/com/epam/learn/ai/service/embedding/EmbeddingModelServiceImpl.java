package com.epam.learn.ai.service.embedding;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.epam.learn.ai.exception.StoredPointSearchException;
import com.epam.learn.ai.exception.VectorSavingException;
import com.epam.learn.ai.model.embedding.EmbeddingResponse;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.VectorsFactory.vectors;

/**
 * Service manages vector operations like obtaining embeddings, storing vectors, and performing searches.
 * <p>
 *  And integrates with OpenAI to generate embeddings and uses Qdrant for vector database tasks.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingModelServiceImpl implements EmbeddingModelService {

    private final OpenAIAsyncClient openAIAsyncClient;
    private final QdrantClient qdrantClient;

    @Value("${embedding.model}")
    private String embeddingModelName;

    @Value("${embedding.collection-name}")
    private String collectionName;

    /**
     * Retrieve embeddings with text params,
     * @param text the input text to be processed into embeddings
     */
    @Override
    public Mono<List<EmbeddingItem>> getEmbeddings(String text) {
        return openAIAsyncClient.getEmbeddings(embeddingModelName,
                        new EmbeddingsOptions(List.of(text)))
                .map(Embeddings::getData);
    }

    /**
     * Search embeddings in the Qdrant DB collection by text.
     *
     * @param text the input text for searching embeddings
     */
    @Override
    public void processAndSaveText(String text) {
        getEmbeddings(text)
                .map(this::pointStructs)
                .doOnNext(pointStructs -> {
                    try {
                        saveVector(pointStructs);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).subscribe();
    }

    /**
     * This process takes input text, creates embeddings,
     * converts them into vector points, and stores them in the Qdrant collection.
     *
     * @param text the input text to be processed into embeddings
     * @return text that stored in DB
     */
    @Override
    public Flux<EmbeddingResponse> search(String text) {
        return getEmbeddings(text)
                .map(this::toFloatList)
                .map(this::searchStoredPoints)
                .map(this::toEmbeddingResponse)
                .flatMapIterable(embeddingResponses -> embeddingResponses);
    }

    /**
     * This process takes input text, creates embeddings,
     * converts them into vector points, and search them in the Qdrant collection.
     *
     * @param text the input text to be processed into embeddings
     * @return text that stored in DB
     */
    @Override
    public Flux<String> searchText(String text) {
        return getEmbeddings(text)
                .map(this::toFloatList)
                .map(this::searchStoredPoints)
                .map(scoredPoints -> scoredPoints.stream().toList())
                .flatMapIterable(scoredPoints -> scoredPoints)
                .filter(scoredPoint -> scoredPoint.containsPayload("input"))
                .map(scoredPoint -> scoredPoint.getPayloadMap()
                        .get("input").getStringValue());
    }

    private ArrayList<Points.PointStruct> pointStructs(List<EmbeddingItem> items) {
        var points = new ArrayList<List<Float>>();
        items.forEach(embeddingItem -> {
                    var values = new ArrayList<>(embeddingItem.getEmbedding());
                    points.add(values);
                });

        var pointStructs = new ArrayList<Points.PointStruct>();
        points.forEach(point -> {
            var pointStruct = toPointStruct(point);
            pointStructs.add(pointStruct);
        });
        return pointStructs;
    }


    private void saveVector(ArrayList<Points.PointStruct> pointStructs) throws InterruptedException, ExecutionException {
        try {
            var collectionInfo = qdrantClient.getCollectionInfoAsync(collectionName).get();

            if(Objects.nonNull(collectionInfo.getStatus())){
                qdrantClient.getCollectionInfoAsync(collectionName).get();
            } else {
                log.warn("Collection '{}' not found. Creating a new collection...", collectionName);
                createCollection();
            }
        } catch (Exception ex) {
            throw new VectorSavingException(String.format("Problem occurred during saving vector values: %s", collectionName));
        }
        var updateResult = qdrantClient.upsertAsync(collectionName, pointStructs).get();
        log.info("Upsert status: {}", updateResult.getStatus().name());
    }

    private Points.PointStruct toPointStruct(List<Float> point) {
        return Points.PointStruct.newBuilder()
                .setId(id(UUID.randomUUID()))
                .setVectors(vectors(point))
                .build();
    }

    private List<EmbeddingResponse> toEmbeddingResponse(List<Points.ScoredPoint> scoredPoints) {
        return scoredPoints.stream()
                .map(scoredPoint -> EmbeddingResponse.builder()
                        .id(scoredPoint.getId().getUuid())
                        .score(scoredPoint.getScore())
                        .build())
                .toList();
    }

    private List<Points.ScoredPoint> searchStoredPoints(List<Float> floats) {
        try {
            return qdrantClient.searchAsync(
                    Points.SearchPoints.newBuilder()
                            .setCollectionName(collectionName)
                            .addAllVector(floats)
                            .setLimit(10)
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new StoredPointSearchException(
                    String.format("Error occurred during search of stored point: %s", e.getMessage()));
        }
    }

    private List<Float> toFloatList(List<EmbeddingItem> items) {
        return items.stream()
                .flatMap(item -> item.getEmbedding().stream())
                .toList();
    }

    private void createCollection() throws ExecutionException, InterruptedException {
        var result = qdrantClient.createCollectionAsync(collectionName,
                        Collections.VectorParams.newBuilder()
                                .setDistance(Collections.Distance.Cosine)
                                .setSize(1536)
                                .build())
                .get();
        log.info("Collection was created: [{}]", result.getResult());
    }
}
