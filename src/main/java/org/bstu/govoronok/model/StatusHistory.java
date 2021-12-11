package org.bstu.govoronok.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "StatusHistory")
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "changeDate", nullable = false)
    private LocalDate changeDate;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="newStatusId")
    private StatusHistory statusHistory;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="auctionId")
    private Auction auction;
}
