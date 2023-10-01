package greencity.repository;

import greencity.entity.NotifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifiedUserRepo extends JpaRepository<NotifiedUser, Long>, JpaSpecificationExecutor<NotifiedUser> {



}