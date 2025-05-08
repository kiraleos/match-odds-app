package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.database.model.Odds;
import org.example.service.OddsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/matches/{matchId}/odds")
@Tag(name = "Match Odds API", description = "API for performing CRUD operations on match odds")
public class OddsController {

    private final OddsService oddsService;

    public OddsController(OddsService oddsService) {
        this.oddsService = oddsService;
    }

    @Operation(summary = "Create new odds for a match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<Odds> createMatchOdds(
            @RequestBody @Valid Odds odds,
            @PathVariable Long matchId
    ) {
        Odds createdOdds = oddsService.createMatchOdds(odds, matchId);
        return new ResponseEntity<>(createdOdds, HttpStatus.CREATED);
    }

    @Operation(summary = "Get odds for a specific match and odds ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Odds> getMatchOdds(
            @Parameter(description = "ID of the odds to retrieve")
            @PathVariable Long id,
            @PathVariable Long matchId
    ) {
        Optional<Odds> matchOdds = oddsService.getMatchOddsById(id, matchId);
        return matchOdds
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all odds for a specific match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    @GetMapping
    public ResponseEntity<List<Odds>> getOddsByMatch(@PathVariable Long matchId) {
        List<Odds> oddsList = oddsService.getMatchOddsByMatchId(matchId);

        if (oddsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(oddsList);
    }

    @Operation(summary = "Update odds for a specific match and odds ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Odds> updateMatchOdds(
            @PathVariable Long id,
            @RequestBody @Valid Odds odds,
            @PathVariable Long matchId
    ) {
        Optional<Odds> updatedOdds = oddsService.updateMatchOdds(id, odds, matchId);
        return updatedOdds.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete odds for a specific match and odds ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchOdds(@PathVariable Long id, @PathVariable Long matchId) {
        boolean deleted = oddsService.deleteMatchOdds(id, matchId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
