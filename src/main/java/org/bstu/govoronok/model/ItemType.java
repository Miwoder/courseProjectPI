package org.bstu.govoronok.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AuctionType")
public class ItemType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "itemType")
    private Item item;
}
