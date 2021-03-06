package org.bstu.govoronok.model;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
    @NotBlank(message = "{item.name.error}")
    private String name;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "{item.description.error}")
    private String description;

    @Column(name = "image")
    private Byte[] image;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "type_id")
    private ItemType itemType;

    @OneToOne(mappedBy = "item")
    private Auction auction;
}
