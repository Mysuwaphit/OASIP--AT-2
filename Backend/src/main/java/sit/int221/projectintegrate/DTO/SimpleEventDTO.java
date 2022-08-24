package sit.int221.projectintegrate.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import sit.int221.projectintegrate.Entities.EventCategory;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class SimpleEventDTO {
    private Integer id;
    @Length(max = 100,message = "The character length cannot exceed 100")
    private String bookingName;
    @Email(message = "Email should be a valid email format.")
    @NotNull(message = "Email should not be null or empty.")
    private String bookingEmail;
    private String category;
    @Future(message = "Dates must not be present and past.")
    @NotNull(message = "Date time is not null or empty.")
    private LocalDateTime startTime;
    private Integer duration;
    private String eventNotes;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventCategoryId")
    private EventCategory eventCategory;


}