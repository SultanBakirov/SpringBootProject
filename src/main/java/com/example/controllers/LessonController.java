package com.example.controllers;

import com.example.models.Lesson;
import com.example.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/lessons/{id}")
    public String listLessons(@PathVariable Long id, Model model) {
        model.addAttribute("lessons", lessonService.getAllLessons(id));
        model.addAttribute("courseId", id);
        return "/lesson/lessons";
    }

    @GetMapping("/getLessonById/{id}")
    public String getLessonById(@PathVariable("id") Long id, Model model){
        model.addAttribute("lesson", lessonService.getLessonById(id));
        return "/lesson/lessons";
    }

    @GetMapping("/lessons/{id}/new")
    public String createLessonForm(@PathVariable Long id, Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("courseId", id);
        return "/lesson/create_lesson";
    }

    @PostMapping("/{id}/lessons")
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson, @PathVariable Long id) {
        lessonService.addLesson(id, lesson);
        return "redirect:/lessons/" + id;
    }

    @GetMapping("/updateLesson/{id}")
    public String editLessonForm(@PathVariable("id") Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", lesson.getCourse().getId());
        return "/lesson/edit_lesson";
    }

    @PostMapping("/{courseId}/{id}/lesson")
    public String updateLesson(@PathVariable("courseId") Long courseId,
                               @PathVariable Long id,
                               @ModelAttribute("lesson") Lesson lesson) throws IOException {
        lessonService.updateLesson(lesson, id);
        return "redirect:/lessons/" + courseId;
    }

    @GetMapping("/{courseId}/{id}/deleteLesson")
    public String deleteInstructor(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        lessonService.deleteLessonById(id);
        return "redirect:/lessons/" + courseId;
    }
}
