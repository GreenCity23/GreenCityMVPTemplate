package greencity.repository;

import greencity.entity.EventComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventCommentRepo extends JpaRepository<EventComment, Long> {
    @Query("SELECT count(ec) from EventComment ec where ec.parentComment.id = ?1 AND ec.deleted = FALSE")
    Integer countByParentCommentId(Long parentCommentId);

    Page<EventComment> findAllByParentCommentIdAndDeletedFalseOrderByCreatedDateDesc(Pageable pageable,
                                                                                     Long parentCommentId);

    Page<EventComment> findAllByParentCommentIsNullAndDeletedFalseAndEventIdOrderByCreatedDateDesc(Pageable pageable
    , Long eventId);
}
