package greencity.dto.user;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendersEmailsDto implements Serializable {
    Long id;
    String name;
    @NotNull
    String email;
}
