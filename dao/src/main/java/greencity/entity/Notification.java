package greencity.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "notifications")
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
    private NotificationSource source;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotifiedUser> notifiedUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "notifications")
    private List<EcoNewsComment> ecoNewsComments = new ArrayList<>();
}
