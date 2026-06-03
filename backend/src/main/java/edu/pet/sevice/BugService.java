package edu.pet.sevice;

import edu.pet.entity.Bug;
import edu.pet.dto.BugRequest;
import edu.pet.enums.State;
import edu.pet.repository.BugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // как обычный конструктор с @Autowired
@Service
public class BugService {
    private final BugRepository bugRepository;

    public Bug addBug(BugRequest request) {
        Bug bug = Bug.builder()
                .title(request.title())
                .info(request.info())
                .priority(request.priority())
                .state(request.state())
                .build();
        bugRepository.save(bug);
        return bug;
    }

    public Bug markDone(Long id) {
        Bug bug = bugRepository.getReferenceById(id);
        bug.setState(State.CLOSED);
        bugRepository.save(bug); // главное не забыть сохранить
        return bug;
    }

    public Bug getBugById(long id) {
        return bugRepository.getReferenceById(id); // есть еще deprecated getById и getOne :D
    }

    public List<Bug> getAll() {
        return bugRepository.findAll();
    }
}
