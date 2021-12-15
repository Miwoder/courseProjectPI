package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.model.Payment;
import org.bstu.govoronok.repository.BetHistoryRepository;
import org.bstu.govoronok.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BetHistoryService {
    private final BetHistoryRepository betHistoryRepository;

    public void save(BetHistory betHistory){
        betHistoryRepository.save(betHistory);
    }
}
