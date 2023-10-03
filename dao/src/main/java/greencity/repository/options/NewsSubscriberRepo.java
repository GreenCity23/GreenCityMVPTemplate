package greencity.repository.options;

import greencity.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsSubscriberRepo extends JpaRepository<Subscriber, Long> {

}
