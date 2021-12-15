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
@Table(name = "BetHistory")
public class BetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bet", nullable = false)
    private String bet;

    @Column(name = "winDate", nullable = false)
    private LocalDate winDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "auctionId")
    private Auction auction;

    public BetHistory(String bet, LocalDate winDate, User user, Auction auction) {
        this.bet = bet;
        this.winDate = winDate;
        this.user = user;
        this.auction = auction;
    }
}
