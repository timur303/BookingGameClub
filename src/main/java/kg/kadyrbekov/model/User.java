package kg.kadyrbekov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.kadyrbekov.model.entity.Booking;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.Image;
import kg.kadyrbekov.model.entity.OrderHistory;
import kg.kadyrbekov.model.enums.AuthenticationProvider;
import kg.kadyrbekov.model.enums.City;
import kg.kadyrbekov.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String phoneNumber;

    @Email
    private String email;

    private String password;

    @OneToOne(cascade = ALL, mappedBy = "user")
    private Image avatar;

    private boolean blocked;

    private String urlAvatar;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthenticationProvider authenticationProvider;

    @OneToMany(cascade = ALL, mappedBy = "user")
    @JsonIgnore
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private City city;

    @OneToMany(mappedBy = "user")
    private List<OrderHistory> orderHistoryList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = new LinkedList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));

        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getUserName1() {
        return username;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setRole(Role role) {
        this.role = role;
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

}