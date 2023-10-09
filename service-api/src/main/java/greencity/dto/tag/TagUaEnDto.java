package greencity.dto.tag;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TagUaEnDto implements Serializable {
    private long id;
    private String nameUa;
    private String nameEn;
}
