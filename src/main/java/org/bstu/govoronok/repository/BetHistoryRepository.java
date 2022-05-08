package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.BetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetHistoryRepository extends JpaRepository<BetHistory, Long> {

    List<BetHistory> getBetHistoriesByUserId(Long userId);
}
