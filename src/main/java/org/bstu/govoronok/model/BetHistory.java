package org.bstu.govoronok.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="auctionId")
    private Auction auction;
}
