package org.example.database.repository;

import org.example.database.model.Odds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OddsRepository extends JpaRepository<Odds, Long> {
    List<Odds> findByMatchId(Long matchId);

    Boolean existsByIdAndMatchId(Long id, Long matchId);
}
