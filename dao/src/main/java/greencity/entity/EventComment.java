package greencity.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events_comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "parentComment, replies")
public class EventComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 8000)
    private String text;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne
    private EventComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.ALL})
    private List<EventComment> replies = new ArrayList<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    @Column
    private boolean deleted;

    @Transient
    private boolean currentUserLiked;

    @ManyToMany
    @JoinTable(
        name = "event_comment_users_liked",
        joinColumns = @JoinColumn(name = "event_comment_id"),
        inverseJoinColumns = @JoinColumn(name = "users_liked_id"))
    private Set<User> usersLiked;
}
