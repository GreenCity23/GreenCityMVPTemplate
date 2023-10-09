package greencity.repository;

import greencity.entity.EcoNewsComment;
import greencity.entity.EventComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventCommentRepo extends JpaRepository<EventComment, Long> {
    /**
     * Method returns count of replies to comment, specified by parentCommentId.
     *
     * @param parentCommentId id of comment, count of replies to which we get.
     * @return count of replies to comment, specified by parentCommentId.
     */
    @Query("SELECT count(ec) from EventComment ec where ec.parentComment.id = ?1 AND ec.deleted = FALSE")
    Integer countByParentCommentId(Long parentCommentId);

    /**
     * Method returns all replies to comment, specified by parentCommentId and by
     * page.
     *
     * @param pageable        page of events.
     * @param parentCommentId id of comment, replies to which we get.
     * @return all replies to comment, specified by parentCommentId and page.
     */
    Page<EventComment> findAllByParentCommentIdAndDeletedFalseOrderByCreatedDateDesc(Pageable pageable,
        Long parentCommentId);

    /**
     * Method returns all {@link EventComment} by page.
     *
     * @param pageable page of events.
     * @param eventId  id of {@link greencity.entity.Event} for which comments we
     *                 search.
     * @return all {@link EventComment} by page.
     */
    Page<EventComment> findAllByParentCommentIsNullAndDeletedFalseAndEventIdOrderByCreatedDateDesc(Pageable pageable,
        Long eventId);
}
