package org.bstu.govoronok.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
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
    @NotBlank(message = "{auction.startBet.error}")
    private String startBet;

    @Column(name = "highBet", nullable = false)
    private String highBet;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "placeId")
    private Place place;

    @OneToMany(mappedBy = "auction", fetch = FetchType.EAGER)
    private Set<BetHistory> betHistorySet;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "statusID")
    private AuctionStatus auctionStatus;

    @OneToMany(mappedBy = "auction")
    private Set<StatusHistory> statusHistories;

    public static Optional<String> getImgData(Optional<byte[]> bytes) {
        return Optional.of(Base64.getMimeEncoder().encodeToString(bytes.get()));
    }
}
