package greencity.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<DateLocation> dateLocations;

    @ManyToOne
    private User organizer;

    @Column(name = "title_image")
    private String titleImage;

    @Column(name = "is_closed")
    private boolean eventClosed;

    @Column(name = "is_subscribed")
    private boolean isSubscribed;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @ElementCollection
    @CollectionTable(name = "events_additional_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "additional_title_image")
    private List<String> additionalImages;

    @ManyToMany
    @JoinTable(
            name = "events_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "events_attenders",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> attenders;
}
