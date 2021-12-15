package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.repository.BetHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BetHistoryService {
    private final BetHistoryRepository betHistoryRepository;

    public void save(BetHistory betHistory) {
        betHistoryRepository.save(betHistory);
    }
}
