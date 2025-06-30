package Development.Rodrigues.Almeidas_Cortes.users.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tab_users")
@Entity(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_Login")
    private LocalDateTime firstLogin;

    @Column(name = "last_Login")
    private LocalDateTime lastLogin;

    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "funct")
    private String function;
    
    @Column(name = "photo")
    private String photo;
    
    @Column(name = "sex")
    private String sex;
    
    public User(String name, String user, String password, boolean active, String fullName, String function, String photo, String sex) {
        this.name = name;
        this.user = user;
        this.password = password;
        this.active = active;
        this.fullName = fullName;
        this.function = function;
        this.photo = photo;
        this.sex = sex;
    }

    public void updateUser(
        String name, 
        String user, 
        String password, 
        LocalDateTime firstLogin, 
        LocalDateTime lastLogin, 
        boolean active,
        String fullName, 
        String function, 
        String photo, 
        String sex
    ) {
        this.name = name;
        this.user = user;
        this.password = password;
        if (firstLogin != null) this.firstLogin = firstLogin;
        if (lastLogin != null) this.lastLogin = lastLogin;
        this.active = active;
        this.fullName = fullName;
        this.function = function;
        this.photo = photo;
        this.sex = sex;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //controle de acesso por perfis
        return List.of(new SimpleGrantedAuthority(("ROLE_USER")));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.user;
    }
}
