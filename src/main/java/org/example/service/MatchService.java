package org.example.service;

import org.example.database.model.Match;
import org.example.database.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public Match createMatch(Match match) {
        logger.info("Creating match {}", match.getDescription());
        return matchRepository.save(match);
    }

    public Optional<Match> getMatchById(Long id) {
        logger.info("Getting match with id {}", id);
        return matchRepository.findById(id);
    }

    public List<Match> getAllMatches() {
        logger.info("Getting all matches");
        return matchRepository.findAll();
    }

    public Optional<Match> updateMatch(Long id, Match match) {
        if (matchRepository.existsById(id)) {
            match.setId(id);
            return Optional.of(matchRepository.save(match));
        }
        return Optional.empty();
    }

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

