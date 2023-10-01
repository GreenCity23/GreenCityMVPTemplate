package greencity.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notified_users")
@ToString(exclude = {"sender"})
@EqualsAndHashCode(exclude = {"sender"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotifiedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Boolean isRead;

}
