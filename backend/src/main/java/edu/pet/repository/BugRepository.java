package edu.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.pet.entity.Bug;

@Repository // базовый JPA проще чем казался
public interface BugRepository extends JpaRepository<Bug, Long> {

}
