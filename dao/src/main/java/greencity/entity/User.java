package greencity.entity;

import greencity.dto.friends.UserFriendDto;
import greencity.dto.user.RegistrationStatisticsDtoResponse;
import greencity.enums.EmailNotification;
import greencity.enums.Role;
import greencity.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "monthsStatisticsMapping",
        classes = {
            @ConstructorResult(
                targetClass = RegistrationStatisticsDtoResponse.class,
                columns = {
                    @ColumnResult(name = "month", type = Integer.class),
                    @ColumnResult(name = "count", type = Long.class)
                })
        }),
    @SqlResultSetMapping(
        name = "userFriendDtoMapping",
        classes = {
            @ConstructorResult(
                targetClass = UserFriendDto.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "city", type = String.class),
                    @ColumnResult(name = "rating", type = Double.class),
                    @ColumnResult(name = "mutualFriends", type = Long.class),
                    @ColumnResult(name = "profilePicturePath", type = String.class),
                })
        })
})
@NamedNativeQueries(value = {
    @NamedNativeQuery(name = "User.findAllRegistrationMonths",
        query = "SELECT EXTRACT(MONTH FROM date_of_registration) - 1 as month, count(date_of_registration) FROM users "
            + "WHERE EXTRACT(YEAR from date_of_registration) = EXTRACT(YEAR FROM CURRENT_DATE) "
            + "GROUP BY month",
        resultSetMapping = "monthsStatisticsMapping"),
    @NamedNativeQuery(name = "User.getAllUsersFriends",
        query = "SELECT *, (SELECT count(*) "
            + "       FROM users_friends uf1 "
            + "       WHERE uf1.user_id in :friends "
            + "       and uf1.friend_id = u.id "
            + "       and uf1.status = 'FRIEND' "
            + "       or "
            + "       uf1.friend_id in :friends "
            + "       and uf1.user_id = u.id "
            + "       and uf1.status = 'FRIEND') as mutualFriends, "
            + "       u.profile_picture as profilePicturePath "
            + "FROM users u "
            + "WHERE u.id != :userId "
            + "       AND u.id IN :friends "
            + "       AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) "
            + "ORDER BY rating DESC, mutualFriends DESC",
        resultSetMapping = "userFriendDtoMapping"),
    @NamedNativeQuery(name = "User.getAllUsersExceptMainUserAndFriends",
        query = "SELECT *, (SELECT count(*) "
            + "        FROM users_friends uf1 "
            + "        WHERE uf1.user_id not in :notFriends "
            + "          and uf1.friend_id = u.id "
            + "          and uf1.status = 'FRIEND' "
            + "           or "
            + "         uf1.friend_id not in :notFriends "
            + "          and uf1.user_id = u.id "
            + "          and uf1.status = 'FRIEND') as mutualFriends, "
            + "       u.profile_picture           as profilePicturePath "
            + "FROM users u "
            + "WHERE u.id != :userId "
            + "AND u.id IN :notFriends AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))"
            + " OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :name, '%')))",
        resultSetMapping = "userFriendDtoMapping")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
@EqualsAndHashCode(
    exclude = {"verifyEmail", "ownSecurity", "ecoNewsLiked", "ecoNewsCommentsLiked",
        "refreshTokenKey", "verifyEmail", "estimates", "restorePasswordEmail", "customShoppingListItems",
        "eventOrganizerRating, events"})
@ToString(
    exclude = {"verifyEmail", "ownSecurity", "refreshTokenKey", "ecoNewsLiked", "ecoNewsCommentsLiked",
        "verifyEmail", "estimates", "restorePasswordEmail", "customShoppingListItems", "eventOrganizerRating, events"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(value = EnumType.ORDINAL)
    private UserStatus userStatus;

    @Column(nullable = false, name = "date_of_registration")
    private LocalDateTime dateOfRegistration;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private OwnSecurity ownSecurity;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private VerifyEmail verifyEmail;

    @OneToOne(mappedBy = "user")
    private RestorePasswordEmail restorePasswordEmail;

    @Enumerated(value = EnumType.ORDINAL)
    private EmailNotification emailNotification;

    @Column(name = "refresh_token_key", nullable = false)
    private String refreshTokenKey;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CustomShoppingListItem> customShoppingListItems = new ArrayList<>();

    @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Event> events;

    @Column(name = "profile_picture")
    private String profilePicturePath;

    @ManyToMany(mappedBy = "usersLikedNews")
    private Set<EcoNews> ecoNewsLiked;

    @ManyToMany(mappedBy = "usersLiked")
    private Set<EcoNewsComment> ecoNewsCommentsLiked;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "city")
    private String city;

    @Column(name = "user_credo")
    private String userCredo;

    @Column(name = "show_location")
    private Boolean showLocation;

    @Column(name = "show_eco_place")
    private Boolean showEcoPlace;

    @Column(name = "show_shopping_list")
    private Boolean showShoppingList;

    @Column(name = "last_activity_time")
    private LocalDateTime lastActivityTime;

    @Column(name = "event_organizer_rating")
    private Double eventOrganizerRating;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Filter> filters = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "users_friends", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    private List<User> userFriends = new ArrayList<>();
}
