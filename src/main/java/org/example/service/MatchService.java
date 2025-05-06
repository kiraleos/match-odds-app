package org.example.service;

import org.example.database.model.MatchEntity;
import org.example.database.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public MatchEntity createMatch(MatchEntity match) {
        return matchRepository.save(match);
    }

    public Optional<MatchEntity> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public List<MatchEntity> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<MatchEntity> updateMatch(Long id, MatchEntity match) {
        if (matchRepository.existsById(id)) {
            match.setId(id);
            return Optional.of(matchRepository.save(match));
        }
        return Optional.empty();
    }

    public Boolean deleteMatch(Long id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

