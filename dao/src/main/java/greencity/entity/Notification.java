package greencity.entity;

import greencity.enums.NotificationSourceType;
import greencity.enums.NotificationSourceType;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "notification")
@ToString(exclude = {"sender"})
@EqualsAndHashCode(exclude = {"sender"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime creationDate;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private NotificationSources source;

    @Column(nullable = false)
    private Long source_object_id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotifiedUsers> notifiedUsers = new ArrayList<>();

}
