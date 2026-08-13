package edu.pet.configuration;

import edu.pet.repository.BugRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ApplicationWarmup {
    private final BugRepository bugRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void warmupDatabase() {
        log.info("Database warmup started");
        long start = System.currentTimeMillis();
        long count = bugRepository.count();
        long end = System.currentTimeMillis();
        log.info("Database warmup completed in {}ms. Total entries={}", end-start, count);
    }
}
