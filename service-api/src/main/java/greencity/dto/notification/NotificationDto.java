package greencity.dto.notification;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class NotificationDto {

    @NotBlank
    private String title;

    private Long sourceId;

    private Long senderId;
}
