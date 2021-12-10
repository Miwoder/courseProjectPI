package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.AuctionStatus;
import org.bstu.govoronok.model.ItemType;
import org.bstu.govoronok.repository.AuctionStatusRepository;
import org.bstu.govoronok.repository.ItemTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionStatusService {
    private final AuctionStatusRepository auctionStatusRepository;

    public AuctionStatus getAuctionStatusByName(String name){
        return auctionStatusRepository.getAuctionStatusByName(name);
    }
}
