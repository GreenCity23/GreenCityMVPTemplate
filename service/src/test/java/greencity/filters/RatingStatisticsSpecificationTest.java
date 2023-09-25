package greencity.filters;

import greencity.annotations.RatingCalculationEnum;
import greencity.dto.ratingstatistics.RatingStatisticsViewDto;
import greencity.entity.RatingStatistics;
import greencity.entity.RatingStatistics_;
import greencity.entity.User;
import greencity.entity.User_;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RatingStatisticsSpecificationTest {
    @Mock
    private Join<RatingStatistics, User> userJoinMock;
    @Mock
    private CriteriaBuilder criteriaBuilderMock;
    @Mock
    private CriteriaQuery criteriaQueryMock;
    @Mock
    private Root<RatingStatistics> ratingStatisticsRootMock;
    @Mock
    private Path<Object> pathUserIdMock;
    @Mock
    private Path<Object> pathIdMock;
    @Mock
    private Path<Object> pathRatingMock;
    @Mock
    private Path<Object> pathUserMailMock;
    @Mock
    private Path<Object> pathEventNameMock;
    @Mock
    private Path<Object> pathPointChangedMock;
    @Mock
    private Path<Object> pathCreateDateMock;
    @Mock
    private Predicate predicateMock;
    @Mock
    private Predicate andEventNamePredicate;
    @Mock
    private Predicate andIdPredicate;
    @Mock
    private Predicate andUserIdPredicate;
    @Mock
    private Predicate andUserMailPredicate;
    @Mock
    private Predicate andRatingPredicate;
    @Mock
    private Predicate andPointsChangedPredicate;
    @Mock
    private Predicate andCreateDatePredicate;
    private List<SearchCriteria> criteriaList;

    @Test
    void toPredicateForUserIdAndUserEmailAndPointsChangedTest() {
        RatingStatisticsViewDto ratingStatisticsViewDto = new RatingStatisticsViewDto(
            "", "", "23", "anyEmail",
            "", "", "15", "");
        criteriaList = new ArrayList<>();
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.USER)
            .type("userId")
            .value(ratingStatisticsViewDto.getUserId())
            .build());
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.USER)
            .type("userMail")
            .value(ratingStatisticsViewDto.getUserEmail())
            .build());
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.POINTS_CHANGED)
            .type("pointsChanged")
            .value(ratingStatisticsViewDto.getPointsChanged())
            .build());
        RatingStatisticsSpecification ratingStatisticsSpecification = new RatingStatisticsSpecification(criteriaList);

        when(criteriaBuilderMock.conjunction()).thenReturn(predicateMock);

        when(ratingStatisticsRootMock.join(RatingStatistics_.user)).thenReturn(userJoinMock);
        when(userJoinMock.get(User_.ID)).thenReturn(pathUserIdMock);
        when(criteriaBuilderMock.equal(userJoinMock.get(User_.ID).as(String.class),
            criteriaList.get(0).getValue())).thenReturn(andUserIdPredicate);

        when(criteriaBuilderMock.and(predicateMock, andUserIdPredicate)).thenReturn(andUserIdPredicate);

        when(userJoinMock.get(User_.EMAIL)).thenReturn(pathUserMailMock);
        when(criteriaBuilderMock.like(userJoinMock.get(User_.EMAIL).as(String.class),
            "%" + criteriaList.get(1).getValue() + "%")).thenReturn(andUserMailPredicate);

        when(criteriaBuilderMock.and(andUserIdPredicate, andUserMailPredicate)).thenReturn(andUserMailPredicate);

        when(ratingStatisticsRootMock.get(RatingStatistics_.POINTS_CHANGED)).thenReturn(pathPointChangedMock);
        when(criteriaBuilderMock.equal(
            ratingStatisticsRootMock.get(RatingStatistics_.POINTS_CHANGED),
            criteriaList.get(2).getValue())).thenReturn(andPointsChangedPredicate);

        when(criteriaBuilderMock.and(andUserMailPredicate, andPointsChangedPredicate))
            .thenReturn(andPointsChangedPredicate);

        ratingStatisticsSpecification.toPredicate(ratingStatisticsRootMock, criteriaQueryMock, criteriaBuilderMock);

        verify(ratingStatisticsRootMock, never()).get(RatingStatistics_.ID);
        verify(ratingStatisticsRootMock, never()).get(RatingStatistics_.RATING_CALCULATION_ENUM);
        verify(ratingStatisticsRootMock, never()).get(RatingStatistics_.CREATE_DATE);
        verify(ratingStatisticsRootMock, never()).get(RatingStatistics_.RATING);
        verify(criteriaBuilderMock).and(predicateMock, andUserIdPredicate);
        verify(criteriaBuilderMock).and(andUserIdPredicate, andUserMailPredicate);
        verify(criteriaBuilderMock).and(andUserMailPredicate, andPointsChangedPredicate);
    }

    @Test
    void toPredicateForIdAndEventNameAndDateRangeAndRatingTest() {
        RatingStatisticsViewDto ratingStatisticsViewDto = new RatingStatisticsViewDto(
            "12", "add_comment", "", "", LocalDate.now().toString(),
            LocalDate.now().plusDays(3).toString(), "", "1");
        criteriaList = new ArrayList<>();
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.ID)
            .type(RatingStatistics_.ID)
            .value(ratingStatisticsViewDto.getId())
            .build());
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.RATING_CALCULATION_ENUM)
            .type("enum")
            .value(ratingStatisticsViewDto.getEventName())
            .build());
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.CREATE_DATE)
            .type("dateRange")
            .value(new String[] {ratingStatisticsViewDto.getStartDate(), ratingStatisticsViewDto.getEndDate()})
            .build());
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.RATING)
            .type("currentRating")
            .value(ratingStatisticsViewDto.getPointsChanged())
            .build());
        RatingStatisticsSpecification ratingStatisticsSpecification = new RatingStatisticsSpecification(criteriaList);

        when(criteriaBuilderMock.conjunction()).thenReturn(predicateMock);

        when(ratingStatisticsRootMock.get(RatingStatistics_.ID)).thenReturn(pathIdMock);
        when(criteriaBuilderMock.equal(ratingStatisticsRootMock.get(RatingStatistics_.ID),
            criteriaList.get(0).getValue()))
                .thenReturn(andIdPredicate);

        when(criteriaBuilderMock.and(predicateMock, andIdPredicate)).thenReturn(andIdPredicate);

        when(ratingStatisticsRootMock.get(RatingStatistics_.RATING_CALCULATION_ENUM))
            .thenReturn(pathEventNameMock);
        when(criteriaBuilderMock.equal(ratingStatisticsRootMock.get(RatingStatistics_.RATING_CALCULATION_ENUM),
            RatingCalculationEnum.valueOf(criteriaList.get(1).getValue().toString().toUpperCase())))
                .thenReturn(andEventNamePredicate);
        when(criteriaBuilderMock.or(null, andEventNamePredicate)).thenReturn(andEventNamePredicate);

        when(criteriaBuilderMock.and(andIdPredicate, andEventNamePredicate)).thenReturn(andEventNamePredicate);

        when(ratingStatisticsRootMock.get(RatingStatistics_.CREATE_DATE)).thenReturn(pathCreateDateMock);
        ZonedDateTime zdt1 = LocalDate.parse(ratingStatisticsViewDto.getStartDate()).atStartOfDay(ZoneOffset.UTC);
        ZonedDateTime zdt2 = LocalDate.parse(ratingStatisticsViewDto.getEndDate()).atStartOfDay(ZoneOffset.UTC);
        when(criteriaBuilderMock.between(ratingStatisticsRootMock.get(RatingStatistics_.CREATE_DATE), zdt1, zdt2))
            .thenReturn(andCreateDatePredicate);

        when(criteriaBuilderMock.and(andEventNamePredicate, andCreateDatePredicate)).thenReturn(andCreateDatePredicate);

        when(ratingStatisticsRootMock.get(RatingStatistics_.RATING)).thenReturn(pathRatingMock);
        when(criteriaBuilderMock.equal(ratingStatisticsRootMock.get(RatingStatistics_.RATING),
            criteriaList.get(3).getValue()))
                .thenReturn(andRatingPredicate);

        ratingStatisticsSpecification.toPredicate(ratingStatisticsRootMock, criteriaQueryMock, criteriaBuilderMock);

        verify(ratingStatisticsRootMock, never()).get(RatingStatistics_.USER);
        verify(ratingStatisticsRootMock, never()).get(RatingStatistics_.POINTS_CHANGED);
        verify(criteriaBuilderMock).and(predicateMock, andIdPredicate);
        verify(criteriaBuilderMock).and(andIdPredicate, andEventNamePredicate);
        verify(criteriaBuilderMock).and(andEventNamePredicate, andCreateDatePredicate);
        verify(criteriaBuilderMock).and(andCreateDatePredicate, andRatingPredicate);
    }

    @Test
    void toPredicateThrowNumberFormatExceptionWhenUserIdIsWrongTest() {
        RatingStatisticsViewDto ratingStatisticsViewDto = new RatingStatisticsViewDto(
            "", "", "WRONG_FORMAT", "", "",
            "", "", "");
        criteriaList = new ArrayList<>();
        criteriaList.add(SearchCriteria.builder()
            .key(RatingStatistics_.USER)
            .type("userId")
            .value(ratingStatisticsViewDto.getUserId())
            .build());
        RatingStatisticsSpecification ratingStatisticsSpecification = new RatingStatisticsSpecification(criteriaList);

        when(criteriaBuilderMock.conjunction()).thenReturn(predicateMock);

        when(ratingStatisticsRootMock.join(RatingStatistics_.user)).thenReturn(userJoinMock);
        when(userJoinMock.get(User_.ID)).thenReturn(pathUserIdMock);

        when(criteriaBuilderMock.equal(userJoinMock.get(User_.ID).as(String.class),
            criteriaList.get(0).getValue())).thenThrow(NumberFormatException.class);
        ratingStatisticsSpecification.toPredicate(ratingStatisticsRootMock, criteriaQueryMock, criteriaBuilderMock);

        assertNotNull(predicateMock);
    }

    @Test
    void toPredicateNullPointerExceptionTest() {
        RatingStatisticsSpecification ratingStatisticsSpecification = new RatingStatisticsSpecification();
        when(criteriaBuilderMock.conjunction()).thenReturn(predicateMock);
        assertThrows(NullPointerException.class, () -> ratingStatisticsSpecification
            .toPredicate(ratingStatisticsRootMock, criteriaQueryMock, criteriaBuilderMock));
    }
}
