package io.github.ostol9003.ToDoApp.controller;

import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() {
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        //when + then
        try {
            mockMvc.perform(get("/tasks/" + id)).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
