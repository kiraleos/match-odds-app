package org.example.service;

import org.example.database.model.Match;
import org.example.database.model.Sport;
import org.example.database.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void testCreateMatchShouldSaveAndReturnMatch() {
        Match match = new Match();
        match.setTeamA("PAO");
        match.setTeamB("OSFP");
        match.setDescription("PAO vs OSFP");
        match.setMatchDate(LocalDate.of(2025, 1, 1));
        match.setMatchTime(LocalTime.of(12, 1, 1));
        match.setSport(Sport.FOOTBALL);

        when(matchRepository.save(any(Match.class))).thenAnswer(i -> i.getArguments()[0]);

        Match result = matchService.createMatch(match);

        verify(matchRepository).save(match);

        assertEquals("PAO", result.getTeamA());
        assertEquals("OSFP", result.getTeamB());
        assertEquals("PAO vs OSFP", result.getDescription());
        assertEquals(LocalDate.of(2025, 1, 1), result.getMatchDate());
        assertEquals(LocalTime.of(12, 1, 1), result.getMatchTime());
        assertEquals(Sport.FOOTBALL, result.getSport());
    }

    @Test
    void testGetMatchByIdShouldReturnMatchWhenExists() {
        Match match = new Match();
        match.setId(1L);
        match.setDescription("PAO vs OSFP");

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        Optional<Match> result = matchService.getMatchById(1L);

        assertTrue(result.isPresent());
        assertEquals("PAO vs OSFP", result.get().getDescription());
    }

    @Test
    void testGetMatchByIdShouldReturnEmptyWhenNotFound() {
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Match> result = matchService.getMatchById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllMatchesShouldReturnAllMatches() {
        Match match1 = new Match();
        Match match2 = new Match();
        List<Match> matches = Arrays.asList(match1, match2);

        when(matchRepository.findAll()).thenReturn(matches);

        List<Match> result = matchService.getAllMatches();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateMatchShouldUpdateWhenExists() {
        Match match = new Match();
        match.setDescription("PAO vs OSFP");

        when(matchRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.save(any(Match.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Match> result = matchService.updateMatch(1L, match);

        assertTrue(result.isPresent());
        assertEquals("PAO vs OSFP", result.get().getDescription());
        assertEquals(1L, result.get().getId());
    }


    @Test
    void testUpdateMatchShouldReturnEmptyWhenNotExists() {
        when(matchRepository.existsById(2L)).thenReturn(false);

        Optional<Match> result = matchService.updateMatch(2L, new Match());

        assertFalse(result.isPresent());
        verify(matchRepository, never()).save(any());
    }

    @Test
    void testDeleteMatchShouldDeleteWhenExists() {
        when(matchRepository.existsById(1L)).thenReturn(true);

        boolean result = matchService.deleteMatch(1L);

        assertTrue(result);
        verify(matchRepository).deleteById(1L);
    }

    @Test
    void testDeleteMatchShouldReturnFalseWhenNotExists() {
        when(matchRepository.existsById(2L)).thenReturn(false);

        boolean result = matchService.deleteMatch(2L);

        assertFalse(result);
        verify(matchRepository, never()).deleteById(any());
    }
}
