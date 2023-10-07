package greencity.repository;

import greencity.entity.Event;

import greencity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    /**
     * Method returns all {@link Event} by page.
     *
     * @param page page of event.
     * @return all {@link Event} by page.
     */
    Page<Event> findAllByOrderByCreationDateDesc(Pageable page);

    /**
     * Method returns all users {@link Event} by page.
     *
     * @param user author of event.
     * @param page page of event.
     * @return all {@link Event} by page.
     */
    Page<Event> findAllByOrganizerOrderByCreationDateDesc(User user, Pageable page);

    /**
     * Method for get total Events count.
     *
     * @return {@link int} total count of Events
     */
    @Query(nativeQuery = true,
            value = "select count(id) from events")
    int totalCountOfCreationEvents();

    /**
     * Method returns {@link Event} by attender id and page.
     *
     * @param attenderId {@link Long} attender id.
     * @return all {@link Event} by page.
     * @author Maksym Fartushok
     */
    @Query(nativeQuery = true,
            value = "SELECT e.* " +
                    "FROM events AS e " +
                    "INNER JOIN events_attenders AS ea ON e.id = ea.event_id " +
                    "WHERE ea.user_id = (:attenderId) " +
                    "ORDER BY e.creation_date DESC "
    )
    Page<Event> findAllByAttenderIdOrderByCreationDateDesc(Long attenderId, Pageable page);

    /**
     * Method returns {@link Event} by page, where user is organizer or attender.
     *
     * @param userId {@link Long} user id.
     * @return all {@link Event} by page.
     * @author Maksym Fartushok
     */
    @Query(nativeQuery = true,
            value = "SELECT e.* " +
                    "FROM events AS e " +
                    "WHERE e.organizer_id = (:userId) " +
                    "OR e.id in " +
                    "(SELECT ea.event_id " +
                    "FROM events_attenders AS ea " +
                    "WHERE ea.user_id = (:userId)) " +
                    "ORDER BY e.creation_date DESC "
    )
    Page<Event> findAllRelatedToUserOrderByCreationDateDesc(Long userId, Pageable page);
}
