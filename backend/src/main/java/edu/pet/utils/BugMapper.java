package edu.pet.utils;

import edu.pet.dto.BugResponse;
import edu.pet.entity.Bug;

public class BugMapper {
    public static BugResponse toResponse(Bug bug) {
        return new BugResponse(
                bug.getId(),
                bug.getTitle(),
                bug.getInfo(),
                bug.getPriority(),
                bug.getState()
        );
    }
}
