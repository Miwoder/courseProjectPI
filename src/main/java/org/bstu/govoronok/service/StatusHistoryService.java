package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.repository.StatusHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusHistoryService {
    private final StatusHistoryRepository statusHistoryRepository;

    public void save(StatusHistory statusHistory) {
        statusHistoryRepository.save(statusHistory);
    }
}
