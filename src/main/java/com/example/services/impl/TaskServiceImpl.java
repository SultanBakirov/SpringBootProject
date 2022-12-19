package com.example.services.impl;

import com.example.models.Lesson;
import com.example.models.Task;
import com.example.repositories.LessonRepository;
import com.example.repositories.TaskRepository;
import com.example.services.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(LessonRepository lessonRepository, TaskRepository taskRepository) {
        this.lessonRepository = lessonRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public List<Task> getAllTasks(Long lessonId) {
        return taskRepository.getAllTasks(lessonId);
    }

    @Override
    public void addTask(Long id, Task task) {
        Lesson lesson = lessonRepository.findById(id).get();
        lesson.addTask(task);
        task.setLesson(lesson);
        taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).get();
    }

    @Override
    public void updateTask(Task task, Long id) {
        Task task1 = taskRepository.findById(id).get();
        task1.setTaskName(task.getTaskName());
        task1.setTaskText(task.getTaskText());
        task1.setDeadline(task.getDeadline());
        taskRepository.save(task1);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
