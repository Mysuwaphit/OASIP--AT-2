package sit.int221.projectintegrate.DTO;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class SimpleUserDTO {
    private Integer id;
    @NotNull
    @Length(max = 100,message = "Username up to 100 characters")
    private String username;
    @NotNull(message = "Email should not be null or empty.")
    @Email(message = "Email should be a valid email format.")
    @Length(max = 50,message = "Email up to 50 characters")
    private String email;
    @NotNull
    private String roles;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
