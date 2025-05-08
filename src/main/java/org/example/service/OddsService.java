package org.example.service;

import org.example.database.model.Odds;
import org.example.database.repository.OddsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OddsService {

    private final OddsRepository oddsRepository;

    public OddsService(OddsRepository oddsRepository) {
        this.oddsRepository = oddsRepository;
    }

    public Odds createMatchOdds(Odds odds, Long matchId) {
        if (odds.getMatchId() == null) {
            odds.setMatchId(matchId);
        }
        return oddsRepository.save(odds);
    }

    public Optional<Odds> getMatchOddsById(Long id, Long matchId) {
        return oddsRepository
                .findById(id)
                .filter(match -> match.getMatchId().equals(matchId));
    }

    public List<Odds> getMatchOddsByMatchId(Long matchId) {
        return oddsRepository.findByMatchId(matchId);
    }

    public Optional<Odds> updateMatchOdds(Long id, Odds odds, Long matchId) {
        if (oddsRepository.existsByIdAndMatchId(id, matchId)) {
            odds.setId(id); // In order for JPA to not generate its own id due to the @Id annotation on the entity
            return Optional.of(oddsRepository.save(odds));
        }
        return Optional.empty();
    }

    public boolean deleteMatchOdds(Long id, Long matchId) {
        if (oddsRepository.existsByIdAndMatchId(id, matchId)) {
            oddsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
