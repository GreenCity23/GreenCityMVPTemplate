package greencity.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "organizer")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Pattern(
            message = "The title should be at least 5 and not more than 70 characters long.",
            regexp = "^.{5,69}$"
    )
    private String title;

    @Column
    @Pattern(
            message = "The description should be at least 20 and not more than 63 206 characters long.",
            regexp = "^.{20,63206}$"
    )
    private String description;

    @Column(name = "creation_date")
    @CreationTimestamp
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<DateLocation> dateLocations;

    @ManyToOne
    private User organizer;

    @Pattern(
            message = "The link must start with http(s)://",
            regexp = "^(https://|http://).*"
    )
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
    private List<@Pattern(
            message = "The link must start with http(s)://",
            regexp = "^(https://|http://).*"
    ) String> additionalImages;

    @ManyToMany
    @JoinTable(
            name = "events_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "events_attenders",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> attenders;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventComment> eventComments;
}
