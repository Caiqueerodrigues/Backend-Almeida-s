package Development.Rodrigues.Almeidas_Cortes.users.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "SendUser")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SendUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "first_Login")
    private LocalDateTime firstLogin;

    @Column(name = "last_Login")
    private LocalDateTime lastLogin;

    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "funct")
    private String funct;
    
    @Column(name = "photo")
    private String photo;
    
    @Column(name = "sex")
    private String sex;

    public SendUser(
        String name, 
        String user, 
        boolean active, 
        String fullName, 
        String function, 
        String photo, 
        String sex,
        LocalDateTime firstLogin,
        LocalDateTime lastLogin
    ) {
        this.name = name;
        this.user = user;
        this.active = active;
        this.fullName = fullName;
        this.funct = function;
        this.photo = photo;
        this.sex = sex;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
    }
}
