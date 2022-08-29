package sit.int221.projectintegrate.DTO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SimpleLoginDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Integer id;
    @NotNull
    @NotEmpty
    @Email(message = "Email should be a valid email format.")
    @Size(min = 1, max = 50)
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 14)
    private String userpassword;
}