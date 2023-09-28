package greencity.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "city_en")
    private String cityEn;
    @Column(name = "city_ua")
    private String cityUa;
    @Column(name = "country_en")
    private String countryEn;
    @Column(name = "country_ua")
    private String countryUa;
    @Column(name = "formatted_address_en")
    private String formattedAddressEn;
    @Column(name = "formatted_address_ua")
    private String formattedAddressUa;
    @Column(name = "house_number")
    private String houseNumber;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column(name = "region_en")
    private String regionEn;
    @Column(name = "region_ua")
    private String regionUa;
    @Column(name = "street_en")
    private String streetEn;
    @Column(name = "street_ua")
    private String streetUa;
    @OneToOne
    @JoinColumn(name = "date_location_id", referencedColumnName = "id")
    private DateLocation dateLocation;
}
