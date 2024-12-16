package com.epam.learn.ai.service.plugin;

import com.epam.learn.ai.model.movie.Actor;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.util.ArrayList;
import java.util.List;

public class ActorsByGenrePlugin {

    private final List<Actor> actors = new ArrayList<>();

    public ActorsByGenrePlugin() {

        var woodyHarrelson = Actor.builder()
                .firstName("Woody")
                .lastName("Harrelson")
                .genre(List.of("comedy", "thriller", "crime", "drama")).build();

        var emmaStone = Actor.builder()
                .firstName("Emma")
                .lastName("Stone")
                .genre(List.of("comedy", "fiction", "drama", "musical")).build();

        var christophWaltz = Actor.builder()
                .firstName("Christoph")
                .lastName("Christoph Waltz")
                .genre(List.of("comedy", "thriller", "crime", "drama")).build();

        var marionCotillard = Actor.builder()
                .firstName("Marion")
                .lastName("Cotillard")
                .genre(List.of("comedy", "thriller", "crime", "drama", "fiction", "action")).build();

        actors.add(woodyHarrelson);
        actors.add(emmaStone);
        actors.add(christophWaltz);
        actors.add(marionCotillard);
    }

    /**
     * Get list of actors by genre.
     *
     * @return list of actors by genre.
     */
    @DefineKernelFunction(name = "getactorsbygenre", description = "Get actors by Genre")
    public List<Actor> getActorsByGenre(@KernelFunctionParameter(name = "genre") String genre) {
        return actors.stream()
                .filter(actor -> actor.getGenre().contains(genre.toLowerCase()))
                .toList();

    }
}
