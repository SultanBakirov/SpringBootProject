package com.example.controllers;

import com.example.models.Instructor;
import com.example.services.CourseService;
import com.example.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class InstructorController {

    private final InstructorService instructorService;
    private final CourseService courseService;

    @Autowired
    public InstructorController(InstructorService instructorService, CourseService courseService) {
        this.instructorService = instructorService;
        this.courseService = courseService;
    }


    @GetMapping("/instructors/{id}")
    public String listInstructors(@PathVariable Long id, Model model) {
        System.out.println("getAllInstructorController");
        model.addAttribute("instructorsList", instructorService.getAllInstructorsByCourseId(id));
        model.addAttribute("courseId",id);
        model.addAttribute("companyId",courseService.getCourseById(id).getCompany().getId());
        return "/instructor/instructors";
    }

    @GetMapping("/instructors/{id}/new")
    public String createInstructorForm(@PathVariable Long id, Model model) {
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("courseId", id);
        return "/instructor/create_instructor";
    }

    @PostMapping("/{id}/instructors")
    public String saveInstructor(@ModelAttribute("instructor") Instructor instructor, @PathVariable Long id) throws IOException {
        instructorService.addInstructor(id, instructor);
        return "redirect:/instructors/" + id;
    }

    @GetMapping("/update/{id}")
    public String editInstructorForm(@PathVariable("id") Long id, Model model) {
        Instructor instructor = instructorService.getInstructorById(id);
        model.addAttribute("instructor", instructor);
        model.addAttribute("courseId", instructor.getCourse().getId());
        return "/instructor/edit_instructor";
    }

    @PostMapping("/{courseId}/{id}/instructor")
    public String updateInstructor(@PathVariable("courseId") Long courseId,
                                   @PathVariable("id") Long id,
                                   @ModelAttribute("instructor") Instructor instructor) throws IOException {
        instructorService.updateInstructor(instructor, id);
        return "redirect:/instructors/" + courseId;
    }

    @GetMapping("/{courseId}/{id}/deleteInstructor")
    public String deleteInstructor(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        instructorService.deleteInstructorById(id);
        return "redirect:/instructors/" + courseId;
    }
}