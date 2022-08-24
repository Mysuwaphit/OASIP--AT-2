package sit.int221.projectintegrate.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
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

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Lob
    @Column(name = "roles", nullable = false)
    private String roles;

    @Column(name = "createdOn", nullable = false , insertable = false , updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updatedOn", nullable = false , insertable = false , updatable = false)
    private LocalDateTime updatedOn;


}