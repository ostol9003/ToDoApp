package io.github.ostol9003.ToDoApp.logic;

import io.github.ostol9003.ToDoApp.TaskConfigurationProperties;
import io.github.ostol9003.ToDoApp.model.*;
import io.github.ostol9003.ToDoApp.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    private static TaskConfigurationProperties configurationReturning(boolean value) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(value);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private static TaskGroupRepository groupRepositoryReturning(boolean value) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(value);
        return mockGroupRepository;
    }


    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exist")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        // given
        var mockGroupRepository = groupRepositoryReturning(true);
        //and
        var mockConfig = configurationReturning(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository,null, mockConfig );
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configuration_ok_and_no_projects_throwsIllegalArgumentException() {
        // given
        var mockRepository = getProjectRepository(Optional.empty());
        //and
        var mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null,null, mockConfig );
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects for a given id")
    void createGroup_noMultipleGroupsConfig_and_noUndoneGroupExists_no_projects_throwsIllegalArgumentException() {
        // given
        var mockRepository = getProjectRepository(Optional.empty());
        //and
        var mockGroupRepository = groupRepositoryReturning(false);
        //and
        var mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository,null, mockConfig );
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");

    }

    private static ProjectRepository getProjectRepository(Optional<Project> empty) {
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(empty);
        return mockRepository;
    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup_configurationOk_existingProject_createAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar", Set.of(-1, -2, -3));
        var mockRepository = getProjectRepository(Optional.of(project));
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGriupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        var mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo,serviceWithInMemRepo, mockConfig );
        //when
        GroupReadModel result = toTest.createGroup(today, 1);
        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private static TaskGroupService dummyGriupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream().map(days -> {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private final Map<Integer, TaskGroup> map = new HashMap<>();
        private int index = 0;

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                   var field = TaskGroup.class.getDeclaredField("id");
                   field.setAccessible(true);
                   field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);

            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream().filter(group -> !group.isDone()).anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

}
