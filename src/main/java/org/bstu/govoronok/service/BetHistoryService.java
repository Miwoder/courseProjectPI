package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.repository.BetHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetHistoryService {
    private final BetHistoryRepository betHistoryRepository;

    public void save(BetHistory betHistory) {
        betHistoryRepository.save(betHistory);
    }

    public long getMaxBetByUserId(Long id) {
        List<BetHistory> allUserBets = betHistoryRepository.getBetHistoriesByUserId(id);
        return allUserBets.stream().mapToLong(betHistory -> Long.parseLong(betHistory.getBet())).max().orElse(0);
    }
}
