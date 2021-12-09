package org.bstu.govoronok.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Auction")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "startBet", nullable = false)
    private String startBet;

    @Column(name = "highBet", nullable = false)
    private String highBet;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="itemId")
    private Item item;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="userId")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="placeId")
    private Place place;

    @OneToMany (mappedBy="auction", fetch=FetchType.EAGER)
    private Set<BetHistory> betHistorySet;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="statusID")
    private AuctionStatus auctionStatus;

    @OneToMany(mappedBy = "auction")
    private Set<StatusHistory> statusHistories;



}
