package greencity.repository;

import greencity.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class EventSearchRepo {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    /**
     * Initialization constructor.
     */
    public EventSearchRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Event> find(Pageable pageable, String searchQuery) {
        CriteriaQuery<Event> criteriaQuery =
                criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        Predicate predicate = getPredicate(criteriaQuery, searchQuery, root);

        criteriaQuery.select(root).distinct(true).where(predicate);
        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Event> resultList = typedQuery.getResultList();

        return new PageImpl<>(resultList, pageable, getEventCount(predicate));
    }

    private Predicate getPredicate(CriteriaQuery<Event> criteriaQuery, String searchingText, Root<Event> root) {
        List<Predicate> predicateList = formEventLikePredicate(searchingText, root);
        //TODO: mb, add tags search
        return criteriaBuilder.or(predicateList.toArray(new Predicate[0]));
    }

    private List<Predicate> formEventLikePredicate(String searcingText, Root<Event> root) {
        Expression<String> title = root.get("title").as(String.class);

        List<Predicate> predicateList = new ArrayList<>();
        Arrays.stream(searcingText.split(" ")).forEach(partOfSearchingText -> predicateList.add(
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(title), "%" + partOfSearchingText.toLowerCase() + "%"))));
        return predicateList;
    }

    private long getEventCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Event> countEventRoot = countQuery.from(Event.class);
        countQuery.select(criteriaBuilder.count(countEventRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
