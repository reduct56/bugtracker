package edu.pet.controller;

import edu.pet.dto.BugRequest;
import edu.pet.dto.BugResponse;
import edu.pet.entity.Bug;
import edu.pet.service.BugService;
import edu.pet.utils.BugMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        return BugMapper.toResponse(newBug);
    }

    @GetMapping("")
    public List<BugResponse> getAll() {
        return bs.getAll().stream().map(
                BugMapper::toResponse
        ).toList();
    }

    @GetMapping("/{id}")
    public BugResponse getById(@PathVariable("id") Long id) {
        Bug bug = bs.getBugById(id);
        return BugMapper.toResponse(bug);
    }

    @PatchMapping("/{id}")
    public BugResponse markDone(@PathVariable("id") Long id) {
        Bug bug = bs.markDone(id);
        return BugMapper.toResponse(bug);
    }
}
