package greencity.repository.options;

import greencity.entity.NewsSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface NewsSubscriberRepo extends JpaRepository<NewsSubscriber, Long> {

    NewsSubscriber findByEmail(String email);

    /**
     * Method to delete subscriber. This method use native SQL query.
     *
     * @param email subscriber email
     * @author Arthur Mkrtchian
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM news_subscribers WHERE email = :email")
    void deleteSubscriberByToken(@Param("unsubscribeToken") String email);

    /**
     * Method to confirm subscribtion. This method use native SQL query.
     *
     * @param email subscriber email
     * @author Arthur Mkrtchian
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE news_subscribers SET is_verified = true WHERE email = :email")
    void confirmSubscriber(@Param("email") String email);


}
