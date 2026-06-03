package edu.pet.dto;

import edu.pet.enums.Priority;
import edu.pet.enums.State;

// с рекордом удобнее, можно не писать геттеры и сеттеры для дто
public record BugRequest(
        String title,
        String info,
        Priority priority,
        State state
) {}
