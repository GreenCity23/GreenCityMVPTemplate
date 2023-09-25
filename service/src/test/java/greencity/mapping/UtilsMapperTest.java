package greencity.mapping;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arthur Mkrtchian
 */
class UtilsMapperTest {

    @Test
    @DisplayName("Mapping of individual objects test")
    void testMap() {
        Source source = new Source(1, "Test");
        Target target = UtilsMapper.map(source, Target.class);

        assertEquals(source.getId(), target.getId());
        assertEquals(source.getName(), target.getName());
    }

    @Test
    @DisplayName("Mapping of list of objects test")
    void testMapAllToList() {
        List<Source> sourceList = Arrays.asList(
            new Source(1, "Test1"),
            new Source(2, "Test2"));

        List<Target> targetList = UtilsMapper.mapAllToList(sourceList, Target.class);

        assertEquals(sourceList.size(), targetList.size());

        for (int i = 0; i < sourceList.size(); i++) {
            assertEquals(sourceList.get(i).getId(), targetList.get(i).getId());
            assertEquals(sourceList.get(i).getName(), targetList.get(i).getName());
        }
    }

    @Test
    @DisplayName("Mapping of set of objects test")
    void testMapAllToSet() {
        Set<Source> sourceSet = Set.of(
            new Source(1, "Test1"),
            new Source(2, "Test2"));

        Set<Target> targetSet = UtilsMapper.mapAllToSet(sourceSet, Target.class);

        assertEquals(sourceSet.size(), targetSet.size());

        for (Target target : targetSet) {
            assertTrue(sourceSet.stream().anyMatch(
                source -> source.getId().equals(target.getId()) && source.getName().equals(target.getName())));
        }
    }

    @Setter
    @Getter
    static class Source {
        private Integer id;
        private String name;

        public Source(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Setter
    @Getter
    static class Target {
        private Integer id;
        private String name;
    }
}