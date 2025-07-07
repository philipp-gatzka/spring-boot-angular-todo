package ch.gatzka.rest.controller;

import ch.gatzka.data.entity.Task;
import ch.gatzka.data.repository.TaskRepository;
import ch.gatzka.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void givenValidTask_whenCreateTask_thenReturnsId() throws Exception {
        Task task = new Task("Test Task", "Test Description", false);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenTasks_whenGetAllTasks_thenReturnsTasks() throws Exception {
        Task task1 = new Task("Task 1", "Description 1", false);
        Task task2 = new Task("Task 2", "Description 2", false);
        taskService.save(task1);
        taskService.save(task2);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void givenTaskId_whenGetTaskById_thenReturnsTask() throws Exception {
        Task task = new Task("Task", "Description", false);
        Long id = taskService.save(task);

        mockMvc.perform(get("/api/v1/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task"));
    }

    @Test
    void givenInvalidId_whenGetTaskById_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/9999"))
                .andExpect(status().isNotFound()); // EntityNotFoundException currently returns 500
    }

    @Test
    void givenValidTask_whenUpdateTask_thenReturnsUpdatedTask() throws Exception {
        Task task = new Task("Task", "Description", false);
        Long id = taskService.save(task);

        task.setTitle("Updated Task");

        mockMvc.perform(put("/api/v1/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void givenTaskId_whenDeleteTask_thenTaskDeleted() throws Exception {
        Task task = new Task("Task", "Description", false);
        Long id = taskService.save(task);

        mockMvc.perform(delete("/api/v1/tasks/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/tasks/" + id))
                .andExpect(status().isNotFound()); // EntityNotFoundException currently returns 500
    }
}
