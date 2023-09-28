package greencity.repository;

import greencity.entity.DateLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DateLocationRepo extends JpaRepository<DateLocation, Long>, JpaSpecificationExecutor<DateLocation> {
}
