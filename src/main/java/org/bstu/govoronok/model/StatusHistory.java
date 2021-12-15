package org.bstu.govoronok.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "StatusHistory")
public class StatusHistory {

    public StatusHistory(LocalDate changeDate, AuctionStatus auctionStatus, Auction auction) {
        this.changeDate = changeDate;
        this.auctionStatus = auctionStatus;
        this.auction = auction;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "changeDate", nullable = false)
    private LocalDate changeDate;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "newStatusId")
    private AuctionStatus auctionStatus;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "auctionId")
    private Auction auction;
}
