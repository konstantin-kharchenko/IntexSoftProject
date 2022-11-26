package by.kharchenko.intexsoftproject.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "photo_name")
    private String photoName;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Order> orders;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private LoyaltyCard loyaltyCard;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private CardNumber cardNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "baskets", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @ToString.Exclude
    private Set<Product> products;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private Set<Role> roles;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private AllPrice allPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
