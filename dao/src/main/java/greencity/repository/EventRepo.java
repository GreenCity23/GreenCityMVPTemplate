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
}
