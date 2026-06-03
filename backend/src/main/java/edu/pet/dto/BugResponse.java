package edu.pet.dto;

import edu.pet.enums.Priority;
import edu.pet.enums.State;

public record BugResponse (
        Long id, // на сервере генерится айди
        String title,
        String info,
        Priority priority,
        State state
) {}
