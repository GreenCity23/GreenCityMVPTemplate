package greencity.dto.event;

import greencity.enums.TagType;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {greencity.entity.Tag}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TagUaEnDto implements Serializable {

    Long id;
    TagType type;
    String nameUa;
    String nameEn;
}