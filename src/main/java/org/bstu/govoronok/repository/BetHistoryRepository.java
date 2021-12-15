package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetHistoryRepository extends JpaRepository<BetHistory, Long> {
}
