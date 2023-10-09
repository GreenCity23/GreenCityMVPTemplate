package greencity.filters;

import greencity.ModelUtils;
import greencity.dto.habitfact.HabitFactViewDto;
import greencity.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitFactSpecificationTest {
    @Mock
    private Join<HabitFact, Habit> habitFactHabitJoin;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private Root<HabitFact> habitFactRoot;
    @Mock
    private Root<HabitFactTranslation> habitFactTranslationRoot;
    @Mock
    private CriteriaQuery<Habit> criteriaQuery;
    @Mock
    private Predicate expected;
    @Mock
    private Path<Object> objectPath;
    @Mock
    private Path<Long> longPath;
    @Mock
    private Path<String> stringPath;
    @Mock
    private Path<HabitFact> pathHabitFact;
    @Mock
    private SingularAttribute<HabitFact, Habit> habit;
    @Mock
    private SingularAttribute<Habit, Long> id;
    @Mock
    private SingularAttribute<HabitFactTranslation, HabitFact> habitFact;
    @Mock
    private SingularAttribute<HabitFact, Long> habitId;
    @Mock
    private SingularAttribute<Translation, String> content;
    private HabitFactSpecification habitFactSpecification;
    private List<SearchCriteria> searchCriteriaList;

    @BeforeEach
    void init() {
        HabitFactTranslation_.content = content;
        HabitFactTranslation_.habitFact = habitFact;

        HabitFact_.id = habitId;
        HabitFact_.habit = habit;

        Habit_.id = id;

        MockitoAnnotations.openMocks(this);

        HabitFactViewDto habitFactViewDto = ModelUtils.getHabitFactViewDto();

        searchCriteriaList = new ArrayList<>();
        searchCriteriaList.add(SearchCriteria.builder()
                .key("id")
                .type("id")
                .value(habitFactViewDto.getId())
                .build());
        searchCriteriaList.add(SearchCriteria.builder()
                .key("habitId")
                .type("habitId")
                .value(habitFactViewDto.getHabitId())
                .build());
        searchCriteriaList.add(SearchCriteria.builder()
                .key("content")
                .type("content")
                .value(habitFactViewDto.getContent())
                .build());
        habitFactSpecification = new HabitFactSpecification(searchCriteriaList);
    }

    @Test
    void toPredicate() {
        when(criteriaBuilder.conjunction()).thenReturn(expected);
        when(habitFactRoot.get(searchCriteriaList.get(0).getKey())).thenReturn(objectPath);
        when(criteriaBuilder.equal(objectPath, searchCriteriaList.get(0).getValue())).thenReturn(expected);
        when(criteriaBuilder.conjunction()).thenReturn(expected);
        when(habitFactRoot.join(HabitFact_.habit)).thenReturn(habitFactHabitJoin);
        when(habitFactHabitJoin.get(Habit_.id)).thenReturn(longPath);
        when(criteriaBuilder.equal(longPath, searchCriteriaList.get(1).getValue())).thenReturn(expected);
        when(criteriaBuilder.and(expected, expected)).thenReturn(expected);
        when(criteriaQuery.from(HabitFactTranslation.class)).thenReturn(habitFactTranslationRoot);
        when(criteriaBuilder.conjunction()).thenReturn(expected);
        when(habitFactTranslationRoot.get(HabitFactTranslation_.content)).thenReturn(stringPath);
        when(criteriaBuilder.like(stringPath, "%" + searchCriteriaList.get(2).getValue() + "%")).thenReturn(expected);
        when(habitFactTranslationRoot.get(HabitFactTranslation_.habitFact)).thenReturn(pathHabitFact);
        when(pathHabitFact.get(HabitFact_.id)).thenReturn(longPath);
        when(habitFactRoot.get(HabitFact_.id)).thenReturn(longPath);
        when(criteriaBuilder.equal(longPath, longPath)).thenReturn(expected);
        Predicate actual = habitFactSpecification.toPredicate(habitFactRoot, criteriaQuery, criteriaBuilder);
        assertEquals(expected, actual);
    }
}