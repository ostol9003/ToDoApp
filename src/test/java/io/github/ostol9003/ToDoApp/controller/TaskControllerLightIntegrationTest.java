package io.github.ostol9003.ToDoApp.controller;

import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() {

        //given
        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));

        //when + then
        try {
            mockMvc.perform(get("/tasks/1")).andDo(print())
                    .andExpect(content()
                            .string(containsString("\"description\":\"foo\"")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
