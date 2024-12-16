package com.epam.learn.ai.service.plugin;

import com.epam.learn.ai.exception.FactNotFoundException;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AlienMovieFactsPlugin {

    private final Map<String, String> facts = new HashMap<>();

    public AlienMovieFactsPlugin() {
        facts.put("fact#1", "The original title of \"Alien\" was \"Star Beast.\"\n");
        facts.put("fact#2", "The chestburster scene was filmed in one take, surprising the actors.\n");
        facts.put("fact#3", "Ellen Ripley, played by Sigourney Weaver, is a pioneering female action hero.\n");
        facts.put("fact#4", "The films explore themes like corporate greed and human survival.\n");
        facts.put("fact#5", "The franchise has significantly influenced the sci-fi and horror genres");
    }

    /**
     * Get fact about alien move.
     *
     * @return fact about alien move.
     */
    @DefineKernelFunction(name = "alien movie facts", description = "5 interesting facts about Alien movie and universe")
    public String getFacts(
            @KernelFunctionParameter(name = "fact") String fact) {
        return Optional.ofNullable(facts.get(fact))
                .orElseThrow(() -> {
                    var errorMessage = String.format("Sorry seems like I'm not aware about fact %s, or you provided wrong parameter", fact);
                    log.warn(errorMessage);
                    return new FactNotFoundException(errorMessage);
                });
    }
}
