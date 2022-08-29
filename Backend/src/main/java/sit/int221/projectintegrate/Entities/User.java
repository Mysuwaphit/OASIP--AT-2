package sit.int221.projectintegrate.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @NotNull
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @NotNull
    @Column(name = "userpassword", nullable = false, length = 90)
    private String userpassword;

    @Lob
    @Column(name = "roles", nullable = false)
    private String roles;

    @Column(name = "createdOn", nullable = false , insertable = false , updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updatedOn", nullable = false , insertable = false , updatable = false)
    private LocalDateTime updatedOn;

}