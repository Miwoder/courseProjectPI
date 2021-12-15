package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
}
