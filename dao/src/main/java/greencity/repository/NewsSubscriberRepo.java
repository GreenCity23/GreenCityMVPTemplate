package greencity.repository;

import greencity.entity.NewsSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Provides an interface to manage {@link NewsSubscriber} entity.
 * @author Arthur Mkrtchian
 */
@Repository
public interface NewsSubscriberRepo extends JpaRepository<NewsSubscriber, Long> {

    /**
     * Method to get subscriber by email.
     *
     * @author Arthur Mkrtchian
     */
    NewsSubscriber findByEmail(String email);

    /**
     * Method to get all subscribers with confirmed emails. This method use native SQL query.
     *
     * @author Arthur Mkrtchian
     */
    @Query(nativeQuery = true, value = "SELECT * FROM news_subscribers WHERE is_confirmed = true")
    List<NewsSubscriber> getAllConfirmed();

    /**
     * Method to delete subscriber. This method use native SQL query.
     *
     * @param email subscriber email
     * @author Arthur Mkrtchian
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM news_subscribers WHERE email = :email AND unsubscribe_token = :unsubscribeToken")
    void deleteSubscriberByEmailAndToken(@Param("email") String email, @Param("unsubscribeToken") String unsubscribeToken);


    /**
     * Method to confirm subscribtion. This method use native SQL query.
     *
     * @param email subscriber email
     * @author Arthur Mkrtchian
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE news_subscribers SET is_confirmed=true WHERE email = :email")
    void confirmSubscriber(@Param("email") String email);


}
