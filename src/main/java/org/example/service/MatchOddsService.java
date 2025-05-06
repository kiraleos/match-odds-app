package org.example.service;

import org.example.database.model.MatchOdds;
import org.example.database.repository.MatchOddsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchOddsService {

    private final MatchOddsRepository matchOddsRepository;

    public MatchOddsService(MatchOddsRepository matchOddsRepository) {
        this.matchOddsRepository = matchOddsRepository;
    }

    public MatchOdds createMatchOdds(MatchOdds matchOdds) {
        return matchOddsRepository.save(matchOdds);
    }

    public Optional<MatchOdds> getMatchOddsById(Long id) {
        return matchOddsRepository.findById(id);
    }

    public List<MatchOdds> getMatchOddsByMatchId(Long matchId) {
        return matchOddsRepository.findByMatchId(matchId);
    }

    public Optional<MatchOdds> updateMatchOdds(Long id, MatchOdds matchOdds) {
        if (matchOddsRepository.existsById(id)) {
            matchOdds.setId(id);
            return Optional.of(matchOddsRepository.save(matchOdds));
        }
        return Optional.empty();
    }

    public boolean deleteMatchOdds(Long id) {
        if (matchOddsRepository.existsById(id)) {
            matchOddsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
