package greencity.filters;

import greencity.entity.ShoppingListItem;
import greencity.entity.ShoppingListItem_;
import greencity.entity.localization.ShoppingListItemTranslation;
import greencity.entity.localization.ShoppingListItemTranslation_;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemSpecificationTest {

    @Mock
    private Path<ShoppingListItem> pathShoppingListItemMock;

    @Mock
    private Path<Long> pathRootItemIdMock;
    @Mock
    private Path<Long> pathItemIdMock;

    @Mock
    private Root<ShoppingListItemTranslation> itemTranslationRootMock;

    @Mock
    private Predicate numericPredicate;

    @Mock
    private Predicate allPredicates;

    @Mock
    private CriteriaBuilder criteriaBuilderMock;

    @Mock
    private CriteriaQuery<?> criteriaQueryMock;

    @Mock
    private Root<ShoppingListItem> rootMock;

    private List<SearchCriteria> searchCriteriaList;

    @Test
    void toPredicateIdTest() {
        searchCriteriaList = new ArrayList<>();
        searchCriteriaList.add(SearchCriteria.builder()
            .key(ShoppingListItem_.ID)
            .type(ShoppingListItem_.ID)
            .value(12L)
            .build());

        ShoppingListItemSpecification shoppingListItemSpecification =
            new ShoppingListItemSpecification(searchCriteriaList);

        when(criteriaBuilderMock.conjunction()).thenReturn(allPredicates);
        when(
            shoppingListItemSpecification.getNumericPredicate(rootMock, criteriaBuilderMock, searchCriteriaList.get(0)))
                .thenReturn(numericPredicate);
        when(criteriaBuilderMock.and(allPredicates, numericPredicate)).thenReturn(numericPredicate);

        shoppingListItemSpecification.toPredicate(rootMock, criteriaQueryMock, criteriaBuilderMock);

        verify(criteriaBuilderMock).conjunction();
        verify(criteriaBuilderMock).and(allPredicates, numericPredicate);
    }

    @Test
    void toPredicateContentTest() {
        searchCriteriaList = new ArrayList<>();
        searchCriteriaList.add(SearchCriteria.builder()
            .key(ShoppingListItemTranslation_.CONTENT)
            .type(ShoppingListItemTranslation_.CONTENT)
            .value("content")
            .build());

        ShoppingListItemSpecification shoppingListItemSpecification =
            new ShoppingListItemSpecification(searchCriteriaList);

        when(criteriaBuilderMock.conjunction()).thenReturn(allPredicates);
        when(criteriaQueryMock.from(ShoppingListItemTranslation.class)).thenReturn(itemTranslationRootMock);
        when(itemTranslationRootMock.get(ShoppingListItemTranslation_.shoppingListItem))
            .thenReturn(pathShoppingListItemMock);
        when(pathShoppingListItemMock.get(ShoppingListItem_.id)).thenReturn(pathItemIdMock);
        when(rootMock.get(ShoppingListItem_.id)).thenReturn(pathRootItemIdMock);

        shoppingListItemSpecification.toPredicate(rootMock, criteriaQueryMock, criteriaBuilderMock);

        verify(criteriaBuilderMock).conjunction();
        verify(criteriaBuilderMock).like(any(), anyString());
        verify(criteriaBuilderMock).equal(pathItemIdMock, pathRootItemIdMock);
    }
}
