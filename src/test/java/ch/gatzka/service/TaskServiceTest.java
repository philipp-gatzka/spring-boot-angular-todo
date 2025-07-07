package ch.gatzka.service;

import ch.gatzka.data.entity.Task;
import ch.gatzka.data.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void givenValidTask_whenSave_thenTaskSaved() {
        Task task = new Task("Test Task", "Test Description", false);
        taskService.save(task);
        List<Task> tasks = taskRepository.findAll();
        assertFalse(tasks.isEmpty());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    void givenInvalidTask_whenSave_thenTaskNotSaved() {
        Task task = new Task();
        assertThrows(TransactionSystemException.class, () -> {
            taskService.save(task);
        });
        List<Task> tasks = taskRepository.findAll();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void givenValidTaskId_whenGetById_thenTaskFound() {
        Task task = new Task("Test Task", "Test Description", false);
        Long id = taskService.save(task);
        Task foundTask = taskService.getById(id);
        assertEquals("Test Task", foundTask.getTitle());
    }

    @Test
    void givenInvalidTaskId_whenGetById_thenTaskNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            taskService.getById(1L);
        });
    }

    @Test
    void givenThreeTasks_whenRead_thenThreeTasksFound() {
        Task task1 = new Task("Test Task 1", "Test Description 1", false);
        Task task2 = new Task("Test Task 2", "Test Description 2", false);
        Task task3 = new Task("Test Task 3", "Test Description 3", false);
        taskService.save(task1);
        taskService.save(task2);
        taskService.save(task3);
        List<Task> tasks = taskService.read();
        assertEquals(3, tasks.size());
    }

    @Test
    void givenValidTaskId_whenDeleteById_thenTaskDeleted() {
        Task task = new Task("Test Task", "Test Description", false);
        Long id = taskService.save(task);
        taskService.deleteById(id);
        List<Task> tasks = taskRepository.findAll();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void givenInvalidTaskId_whenDeleteById_thenThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            taskService.deleteById(1L);
        });
    }

    @Test
    void givenValidTask_whenUpdate_thenTaskUpdated() {
        Task task = new Task("Test Task", "Test Description", false);
        Long id = taskService.save(task);
        task.setId(id);
        task.setTitle("Updated Test Task");
        taskService.update(task);
        Task updatedTask = taskService.getById(id);
        assertEquals("Updated Test Task", updatedTask.getTitle());
    }
}
