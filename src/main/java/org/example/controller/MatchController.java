package org.example.controller;

import org.example.database.model.MatchEntity;
import org.example.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<MatchEntity> createMatch(@RequestBody MatchEntity match) {
        MatchEntity createdMatch = matchService.createMatch(match);
        return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchEntity> getMatch(@PathVariable Long id) {
        Optional<MatchEntity> match = matchService.getMatchById(id);
        return match
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<MatchEntity> getAllMatches() {
        return matchService.getAllMatches();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchEntity> updateMatch(@PathVariable Long id, @RequestBody MatchEntity match) {
        Optional<MatchEntity> updatedMatch = matchService.updateMatch(id, match);
        return updatedMatch
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        Boolean deleted = matchService.deleteMatch(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
