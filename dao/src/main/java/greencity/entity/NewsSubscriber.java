package greencity.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "news_subscribers")
public class NewsSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(name = "confirmation_token", unique = true)
    private String confirmationToken;

    @NotBlank
    @Column(name = "unsubscribe_token", unique = true)
    private String unsubscribeToken;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}