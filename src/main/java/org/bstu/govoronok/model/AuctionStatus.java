package org.bstu.govoronok.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AuctionStatus")
public class AuctionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany (mappedBy="auctionStatus", fetch=FetchType.EAGER)
    private Set<Auction> auctions;

    @OneToMany(mappedBy = "auctionStatus")
    private Set<StatusHistory> statusHistories;
}
