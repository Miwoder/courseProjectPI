package org.bstu.govoronok.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne (optional=false, cascade=CascadeType.MERGE)
    @JoinColumn (name="user_id")
    private User user;

    @ManyToOne (optional=false, cascade=CascadeType.MERGE)
    @JoinColumn (name="type_id")
    private ItemType itemType;

    @OneToOne(mappedBy = "item")
    private Auction auction;
}
