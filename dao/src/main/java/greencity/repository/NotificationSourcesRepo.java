package greencity.repository;

import greencity.entity.NotificationSource;
import greencity.enums.NotificationSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSourcesRepo extends JpaRepository<NotificationSource, Long> {

    /**
     * Find a {@link NotificationSource} entity by its source.
     *
     * @param source The source to search for.
     * @return The found {@link NotificationSource} entity or null if not found.
     */
    NotificationSource findBySource(NotificationSourceType source);

    /**
     * Count the number of notifications with a specific source ('NEWS_COMMENTED')
     * associated with a given EcoNewsComment ID.
     *
     * @param commentId The ID of the EcoNewsComment to filter by.
     * @return The count of notifications matching the criteria.
     */
    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM notifications n " +
                    "JOIN notification_sources ns ON n.source_id = ns.id " +
                    "JOIN notification_econews_comment nec ON nec.notification_id = n.id " +
                    "WHERE ns.source = 'NEWS_COMMENTED' AND nec.econews_comment_id = :commentId",
            nativeQuery = true
    )
    long countBySourceAndEcoNewsCommentId(@Param("commentId") Long commentId);
}
