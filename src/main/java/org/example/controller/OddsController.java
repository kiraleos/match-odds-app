package org.example.controller;

import org.example.database.model.Odds;
import org.example.service.OddsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches/{matchId}/odds")
public class OddsController {

    private final OddsService oddsService;

    public OddsController(OddsService oddsService) {
        this.oddsService = oddsService;
    }

    @PostMapping
    public ResponseEntity<Odds> createMatchOdds(@RequestBody Odds odds, @PathVariable Long matchId) {
        Odds createdOdds = oddsService.createMatchOdds(odds, matchId);
        return new ResponseEntity<>(createdOdds, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Odds> getMatchOdds(@PathVariable Long id, @PathVariable Long matchId) {
        Optional<Odds> matchOdds = oddsService.getMatchOddsById(id, matchId);
        return matchOdds
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Odds>> getOddsByMatch(@PathVariable Long matchId) {
        List<Odds> oddsList = oddsService.getMatchOddsByMatchId(matchId);

        if (oddsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(oddsList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Odds> updateMatchOdds(@PathVariable Long id, @RequestBody Odds odds, @PathVariable Long matchId) {
        Optional<Odds> updatedOdds = oddsService.updateMatchOdds(id, odds, matchId);
        return updatedOdds.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchOdds(@PathVariable Long id, @PathVariable Long matchId) {
        boolean deleted = oddsService.deleteMatchOdds(id, matchId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
