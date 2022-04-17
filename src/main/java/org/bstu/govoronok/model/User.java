package org.bstu.govoronok.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

//    @Column(name = "password", nullable = false)
//    @NotBlank(message = "Password can't be empty")
//    @Min(value = 6, message = "Password length can't be less then 6 symbols")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @Column(name = "approved", nullable = false)
    private Boolean approved = Boolean.FALSE;

//    @Column(name = "name", nullable = false)
//    @NotBlank(message = "Name can't be empty")
//    @Min(value = 2, message = "Name length can't be less then 2 symbols")
    private String name;

//    @Column(name = "surname", nullable = false)
//    @NotBlank(message = "Surname can't be empty")
//    @Min(value = 2, message = "Surname length can't be less then 2 symbols")
    private String surname;

    @Column(name = "email", nullable = false)
    @Email(message = "Incorrect email")
    private String email;

    @Column(name = "phone", nullable = false)
    @NotBlank(message = "Phone can't be empty")
    @Min(value = 7, message = "Phone length can't be less then 7 symbols")
    private String phone;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.valueOf(0);

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Item> items;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<Auction> auctions;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<BetHistory> betHistorySet;
}
