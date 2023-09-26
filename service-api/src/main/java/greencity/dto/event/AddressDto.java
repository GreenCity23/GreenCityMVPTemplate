package greencity.dto.event;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * DTO for {greencity.entity.Address}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class AddressDto {
    @NotNull
    String cityEn;
    @NotNull
    String cityUa;
    @NotNull
    String countryEn;
    @NotNull
    String countryUa;
    String formattedAddressEn;
    String formattedAddressUa;
    @NotNull
    String houseNumber;
    Double latitude;
    Double longitude;
    String regionEn;
    String regionUa;
    @NotNull
    String streetEn;
    @NotNull
    String streetUa;
}