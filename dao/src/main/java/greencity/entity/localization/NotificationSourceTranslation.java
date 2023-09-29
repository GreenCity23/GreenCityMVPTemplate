package greencity.entity.localization;

import greencity.entity.Language;
import greencity.entity.NotificationSources;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notification_source_translations")
//@EqualsAndHashCode//(exclude = {"language","sources"})
//@ToString//(exclude = {"language","sources"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationSourceTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "notification_source_id", referencedColumnName = "id")
    private NotificationSources source;

}
