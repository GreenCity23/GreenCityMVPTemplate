package greencity.entity;

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
@ToString(exclude = {"sentNotifications"})
@EqualsAndHashCode(exclude = {"sentNotifications"})
public class NotificationSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationSourceType source;

    @Column(name = "en_name", nullable = false)
    private String enName;

    @Column(name = "ua_name", nullable = false)
    private String uaName;

    @OneToMany(mappedBy = "source", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> sentNotifications = new ArrayList<>();
}
