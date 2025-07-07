package ch.gatzka.service;

import ch.gatzka.data.entity.Task;
import ch.gatzka.data.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Long save(Task task) {
        log.info("Saving task: {}", task);
        return taskRepository.save(task).getId();
    }

    public Task getById(Long id) {
        log.info("Getting task by id: {}", id);
        Optional<Task> optional = taskRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    public List<Task> read() {
        log.info("Reading all tasks");
        return taskRepository.findAll();
    }

    public void deleteById(Long id) {
        log.info("Deleting task by id: {}", id);
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
    }

    public Task update(Long id, Task updatedTask) {
        log.info("Updating task: {}", updatedTask);
        Task task = getById(id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDone(updatedTask.getDone());
        return taskRepository.save(task);
    }

}
