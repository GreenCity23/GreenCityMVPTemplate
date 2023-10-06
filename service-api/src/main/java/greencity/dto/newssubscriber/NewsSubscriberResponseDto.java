package greencity.dto.newssubscriber;

import lombok.*;

import javax.validation.constraints.Email;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NewsSubscriberResponseDto implements Serializable {
    Long id;
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    private String unsubscribeToken;
    private String confirmationToken;
}
