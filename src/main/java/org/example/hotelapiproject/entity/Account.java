package org.example.hotelapiproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.hotelapiproject.entity.enums.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString(exclude = "hotels")
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String name;

    private String surname;

    @NotBlank(message = "The password cannot be blank")
    @Size(min = 5, message = "The password must contain at least 5 characters")
    @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Hotel> hotels = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Review> reviews;

    @OneToMany(mappedBy = "account")
    private List<FavoriteHotel> favoriteHotels;

    @OneToMany(mappedBy = "account")
    private List<FavoriteRoom> favoriteRooms;

    @JsonIgnore
    @Override
    @Enumerated(EnumType.STRING)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
