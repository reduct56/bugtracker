package edu.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import edu.pet.entity.Bug;
import org.springframework.web.server.ResponseStatusException;

@Repository
public interface BugRepository extends JpaRepository<Bug, Long> {
    default Bug findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug not found"));
    }
}
