package greencity.entity;


import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "dates_locations")
public class DateLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Event event;
    @Column
    private ZonedDateTime startDate;
    @Column
    private ZonedDateTime finishDate;
    @Column(name = "online_link")
    private String onlineLink;
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    // other fields
}
