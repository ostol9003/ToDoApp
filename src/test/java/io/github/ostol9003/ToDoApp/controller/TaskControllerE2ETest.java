package io.github.ostol9003.ToDoApp.controller;

import io.github.ostol9003.ToDoApp.model.Task;
import io.github.ostol9003.ToDoApp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TaskRepository repo;


    @Test
    void httpGet_returnsAllTasks() {
        //given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));
        //when
        Task[] tasks = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //then
        assertThat(tasks).hasSize(initial + 2);
    }


}