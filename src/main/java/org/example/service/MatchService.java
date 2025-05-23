package org.example.service;

import org.example.database.model.Match;
import org.example.database.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @CacheEvict(value = "allMatches", allEntries = true)
    public Match createMatch(Match match) {
        logger.info("Creating match {}", match.getDescription());
        return matchRepository.save(match);
    }

    @Cacheable(value = "matches", key = "#id")
    public Optional<Match> getMatchById(Long id) {
        logger.info("Getting match with id {}", id);
        return matchRepository.findById(id);
    }

    @Cacheable("allMatches")
    public List<Match> getAllMatches() {
        logger.info("Getting all matches");
        return matchRepository.findAll();
    }

    @CacheEvict(value = "matches", key = "#id")
    public Optional<Match> updateMatch(Long id, Match match) {
        if (matchRepository.existsById(id)) {
            match.setId(id); // In order for JPA to not generate its own id due to the @Id annotation on the entity
            return Optional.of(matchRepository.save(match));
        }
        return Optional.empty();
    }

    @CacheEvict(value = {"matches", "allMatches"}, key = "#id", allEntries = true)
    public Boolean deleteMatch(Long id) {
        logger.info("Deleting match with id {}", id);
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }

        logger.info("No odds for match id {}", id);
        return false;
    }
}

