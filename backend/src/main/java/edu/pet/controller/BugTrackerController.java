package edu.pet.controller;

import edu.pet.dto.BugRequest;
import edu.pet.dto.BugResponse;
import edu.pet.entity.Bug;
import edu.pet.sevice.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bugs")
public class BugTrackerController {
    private final BugService bs;

    @Autowired
    public BugTrackerController(BugService bs) {
        this.bs = bs;
    }

    @PostMapping("")
    public BugResponse createBug(@RequestBody BugRequest request) {
        Bug newBug = bs.addBug(request);

        return new BugResponse(
                newBug.getId(),
                newBug.getTitle(),
                newBug.getInfo(),
                newBug.getPriority(),
                newBug.getState()
        );
    }

    @GetMapping("")
    public List<BugResponse> getAll() {
        return bs.getAll().stream().map(bug -> new BugResponse(
                bug.getId(),
                bug.getTitle(),
                bug.getInfo(),
                bug.getPriority(),
                bug.getState()
        )).toList();
    }

    @GetMapping("/{id}") // используем PathVariable а не RequestParam - RESTful
    public BugResponse getById(@PathVariable("id") Long id) {
        Bug bug = bs.getBugById(id);
        return new BugResponse(
                bug.getId(),
                bug.getTitle(),
                bug.getInfo(),
                bug.getPriority(),
                bug.getState()
        );
    }

    @PatchMapping("/{id}")
    public BugResponse markDone(@PathVariable("id") Long id) {
        Bug bug = bs.markDone(id);
        return new BugResponse(
                bug.getId(),
                bug.getTitle(),
                bug.getInfo(),
                bug.getPriority(),
                bug.getState()
        );
    }
}
