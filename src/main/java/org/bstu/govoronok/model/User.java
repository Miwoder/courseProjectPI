package org.bstu.govoronok.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @Column(name = "approved", nullable = false)
    private Boolean approved = Boolean.FALSE;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="payment_id")
    private Payment payment;

    @OneToMany (mappedBy="user", fetch=FetchType.EAGER)
    private Set<Item> items;

    @OneToMany(mappedBy = "user")
    private Set<Auction> auctions;

    @OneToOne(mappedBy = "user")
    private BetHistory betHistory;
}
