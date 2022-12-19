package com.example.controllers;

import com.example.models.Task;
import com.example.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks/{id}")
    public String listTasks(@PathVariable Long id, Model model) {
        model.addAttribute("tasks", taskService.getAllTasks(id));
        model.addAttribute("lessonId", id);
        return "/task/tasks";
    }

    @GetMapping("/tasks/{id}/new")
    public String createTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("lessonId", id);
        return "/task/create_task";
    }

    @PostMapping("/{id}/tasks")
    public String saveTask(@ModelAttribute("task") Task task, @PathVariable Long id) {
        taskService.addTask(id, task);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/updateTask/{id}")
    public String editTaskForm(@PathVariable("id") Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("lessonId", task.getLesson().getId());
        return "/task/edit_task";
    }

    @PostMapping("/{lessonId}/{id}/task")
    public String updateTask(@PathVariable("lessonId") Long lessonId,
                             @PathVariable("id") Long id,
                             @ModelAttribute("task") Task task) throws IOException {
        taskService.updateTask(task, id);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{lessonId}/{id}/deleteTask")
    public String deleteTask(@PathVariable("id") Long id, @PathVariable("lessonId") Long lessonId) {
        taskService.deleteTaskById(id);
        return "redirect:/tasks/" + lessonId;
    }
}
