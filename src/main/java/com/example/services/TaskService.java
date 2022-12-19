package com.example.services;

import com.example.models.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks(Long lessonId);

    void addTask(Long id, Task task);

    Task getTaskById(Long id);

    void updateTask(Task task, Long id);

    void deleteTaskById(Long id);

}
