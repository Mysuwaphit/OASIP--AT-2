package sit.int221.projectintegrate.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import sit.int221.projectintegrate.Entities.Events;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class SimpleEventCategoryDTO {
    private Integer id;
    @Length(max = 100,message = "The character length cannot exceed 100")
    @NotNull
    private String eventCategoryName;
    @Length(max = 500,message = "The character length cannot exceed 500")
    private String eventCategoryDescription;
    @NotNull
    private Integer duration;
    @JsonIgnore
    @OneToMany(mappedBy = "eventCategory")
    private Set<Events> events = new LinkedHashSet<>();
}
