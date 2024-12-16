package com.epam.learn.ai.model.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Actor {

    private String firstName;
    private String lastName;
    private List<String> genre;
}
