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

    @Column(name = "password", nullable = false)
    @NotBlank(message = "{password.error2}")
    @Min(value = 6, message = "{password.error1}")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @Column(name = "approved", nullable = false)
    private Boolean approved = Boolean.FALSE;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "{firstName.error}")
    private String name;

    @Column(name = "surname", nullable = false)
    @NotBlank(message = "{lastName.error}")
    private String surname;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "{email.erorr}")
    @Email(message = "{email.erorr}")
    private String email;

    @Column(name = "phone", nullable = false)
    @NotBlank(message = "{phone.error}")
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
