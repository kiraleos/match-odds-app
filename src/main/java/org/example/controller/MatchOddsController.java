package org.example.controller;

import org.example.database.model.MatchOdds;
import org.example.service.MatchOddsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/match-odds")
public class MatchOddsController {

    private final MatchOddsService matchOddsService;

    public MatchOddsController(MatchOddsService matchOddsService) {
        this.matchOddsService = matchOddsService;
    }

    @PostMapping
    public ResponseEntity<MatchOdds> createMatchOdds(@RequestBody MatchOdds matchOdds) {
        MatchOdds createdOdds = matchOddsService.createMatchOdds(matchOdds);
        return new ResponseEntity<>(createdOdds, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchOdds> getMatchOdds(@PathVariable Long id) {
        Optional<MatchOdds> matchOdds = matchOddsService.getMatchOddsById(id);
        return matchOdds.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/match/{matchId}")
    public List<MatchOdds> getMatchOddsByMatch(@PathVariable Long matchId) {
        return matchOddsService.getMatchOddsByMatchId(matchId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchOdds> updateMatchOdds(@PathVariable Long id, @RequestBody MatchOdds matchOdds) {
        Optional<MatchOdds> updatedOdds = matchOddsService.updateMatchOdds(id, matchOdds);
        return updatedOdds.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchOdds(@PathVariable Long id) {
        boolean deleted = matchOddsService.deleteMatchOdds(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
