package sit.int221.projectintegrate.DTO;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import sit.int221.projectintegrate.Entities.Roles;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotEmpty
    @Size(min = 1, max = 50)
    @Length(max = 50,message = "Email up to 50 characters")
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Roles roles;
    @NotNull
    @NotEmpty
    @Size(min = 8, max = 14)
    @Length(max = 90,message = "Email up to 50 characters")
    private String userpassword;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
