package greencity.dto.event;

import lombok.*;

/**
 * DTO for {greencity.entity.Address}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class AddressDto {
    String cityEn;
    String cityUa;
    String countryEn;
    String countryUa;
    String formattedAddressEn;
    String formattedAddressUa;
    String houseNumber;
    Double latitude;
    Double longitude;
    String regionEn;
    String regionUa;
    String streetEn;
    String streetUa;
}