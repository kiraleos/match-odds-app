package org.example.database.repository;

import org.example.database.model.MatchOdds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchOddsRepository extends JpaRepository<MatchOdds, Long> {
    List<MatchOdds> findByMatchId(Long matchId);
}
