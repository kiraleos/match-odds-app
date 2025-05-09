package org.example.service;

import org.example.database.model.Odds;
import org.example.database.repository.OddsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OddsServiceTest {

    @Mock
    private OddsRepository oddsRepository;

    @InjectMocks
    private OddsService oddsService;

    @Test
    void testCreateMatchOddsShouldSetMatchIdAndSave() {
        Odds odds = new Odds();
        odds.setOdd(1.5);
        odds.setMatchId(10L);
        odds.setSpecifier("X");

        when(oddsRepository.save(any(Odds.class))).thenAnswer(i -> i.getArguments()[0]);

        Odds result = oddsService.createMatchOdds(odds, 10L);

        assertEquals(10L, result.getMatchId());
        assertEquals(1.5, result.getOdd());
        assertEquals("X", result.getSpecifier());
        verify(oddsRepository).save(odds);
    }

    @Test
    void testGetMatchOddsByIdShouldReturnWhenMatchIdMatches() {
        Odds odds = new Odds();
        odds.setId(1L);
        odds.setMatchId(10L);
        odds.setOdd(1.5);

        when(oddsRepository.findById(1L)).thenReturn(Optional.of(odds));

        Optional<Odds> result = oddsService.getMatchOddsById(1L, 10L);

        assertTrue(result.isPresent());
        assertEquals(1.5, result.get().getOdd());
    }

    @Test
    void testGetMatchOddsByIdShouldReturnEmptyWhenMatchIdMismatch() {
        Odds odds = new Odds();
        odds.setId(1L);
        odds.setMatchId(5L);

        when(oddsRepository.findById(1L)).thenReturn(Optional.of(odds));

        Optional<Odds> result = oddsService.getMatchOddsById(1L, 10L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMatchOddsByIdShouldReturnEmptyWhenNotFound() {
        when(oddsRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Odds> result = oddsService.getMatchOddsById(999L, 10L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetMatchOddsByMatchIdShouldReturnList() {
        Odds odds1 = new Odds();
        Odds odds2 = new Odds();
        List<Odds> oddsList = Arrays.asList(odds1, odds2);

        when(oddsRepository.findByMatchId(10L)).thenReturn(oddsList);

        List<Odds> result = oddsService.getMatchOddsByMatchId(10L);

        assertEquals(2, result.size());
        verify(oddsRepository).findByMatchId(10L);
    }

    @Test
    void testUpdateMatchOddsShouldUpdateWhenExists() {
        Odds odds = new Odds();
        odds.setOdd(3.0);

        when(oddsRepository.existsByIdAndMatchId(1L, 10L)).thenReturn(true);
        when(oddsRepository.save(any(Odds.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Odds> result = oddsService.updateMatchOdds(1L, odds, 10L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(3.0, result.get().getOdd());
    }

    @Test
    void testUpdateMatchOddsShouldReturnEmptyWhenNotExists() {
        when(oddsRepository.existsByIdAndMatchId(1L, 10L)).thenReturn(false);

        Optional<Odds> result = oddsService.updateMatchOdds(1L, new Odds(), 10L);

        assertTrue(result.isEmpty());
        verify(oddsRepository, never()).save(any());
    }

    @Test
    void testDeleteMatchOddsShouldDeleteWhenExists() {
        when(oddsRepository.existsByIdAndMatchId(1L, 10L)).thenReturn(true);

        boolean result = oddsService.deleteMatchOdds(1L, 10L);

        assertTrue(result);
        verify(oddsRepository).deleteById(1L);
    }

    @Test
    void testDeleteMatchOddsShouldReturnFalseWhenNotExists() {
        when(oddsRepository.existsByIdAndMatchId(1L, 10L)).thenReturn(false);

        boolean result = oddsService.deleteMatchOdds(1L, 10L);

        assertFalse(result);
        verify(oddsRepository, never()).deleteById(any());
    }
}
