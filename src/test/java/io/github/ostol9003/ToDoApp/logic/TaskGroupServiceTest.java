package io.github.ostol9003.ToDoApp.logic;

import io.github.ostol9003.ToDoApp.model.TaskGroup;
import io.github.ostol9003.ToDoApp.model.TaskGroupRepository;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Should throw when undone tasks")
    void toggleGroup_undoneTasks_throws_IllegalStateException() {
        //given
        var mockTaskRepository = getTaskRepository(true);
        //system under test
        var toTest = new TaskGroupService(null,mockTaskRepository);
        //when
        var exception = catchThrowable(()->toTest.toggleGroup(1));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("Should throw when id not found")
    void toggleGroup_idNotFound_throws_IllegalArgumentException() {
        //given
        var mockTaskRepository = getTaskRepository(false);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository,mockTaskRepository);
        //when
        var exception = catchThrowable(()->toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id not found");
    }

    @Test
    @DisplayName("Should toggle group")
    void toggleGroup_workAsExpected() {
        //given
        var mockTaskRepository = getTaskRepository(false);
        //and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository,mockTaskRepository);
        //when
        toTest.toggleGroup(0);
        //then
        assertThat(group.isDone()).isNotEqualTo(beforeToggle);
    }

    private static TaskRepository getTaskRepository(boolean value) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt())).thenReturn(value);
        return mockTaskRepository;
    }
}