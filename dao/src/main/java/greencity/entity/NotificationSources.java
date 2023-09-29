package greencity.entity;

import greencity.entity.localization.NotificationSourceTranslation;
import greencity.enums.NotificationSourceType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "notification_sources")
@ToString(exclude = {"source"})
@EqualsAndHashCode(exclude = {"source"})
public class NotificationSources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationSourceType source;

    @OneToMany(mappedBy = "source", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> sentNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "source", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NotificationSourceTranslation> notificationSourceTranslations = new ArrayList<>();
}
