package com.example.controllers;

import com.example.models.Course;
import com.example.models.Group;
import com.example.models.Instructor;
import com.example.models.Student;
import com.example.services.CourseService;
import com.example.services.GroupService;
import com.example.services.InstructorService;
import com.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

public class CourseController {

    private final CourseService courseService;
    private final GroupService groupService;
    private final InstructorService instructorService;
    private final StudentService studentService;


    @Autowired
    public CourseController(CourseService courseService, GroupService groupService, InstructorService instructorService, StudentService studentService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.instructorService = instructorService;
        this.studentService = studentService;
    }

    @GetMapping("/courses/{id}")
    public String listCourses(@PathVariable Long id, Model model,
                              @ModelAttribute("group") Group group,
                              @ModelAttribute("instructor") Instructor instructor,
                              @ModelAttribute("student") Student student) {
        model.addAttribute("courses", courseService.getAllCourses(id));
        model.addAttribute("groups", groupService.getAllGroups(id));
        model.addAttribute("instructors", instructorService.getAllInstructors());
        model.addAttribute("students", studentService.getAllListStudent());
        model.addAttribute("companyId", id);
        return "/course/courses";
    }

    @GetMapping("/courses/{id}/new")
    public String createCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("companyId", id);
        return "/course/create_course";
    }

    @PostMapping("/{id}/courses")
    public String saveCourse(@PathVariable Long id, @ModelAttribute("course") Course course) throws IOException {
        courseService.addCourse(id, course);
        return "redirect:/courses/" + id;
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("companyId", course.getCompany().getId());
        return "/course/edit_course";
    }

    @PostMapping("/{id}/{companyId}/update")
    public String updateCourse(@PathVariable("id") Long id,
                               @PathVariable("companyId") Long companyId,
                               @ModelAttribute("course") Course course) throws IOException {
        courseService.updateCourse(course, id);
        return "redirect:/courses/" + companyId;
    }

    @GetMapping("/{id}/{companyId}/delete")
    public String deleteCourse(@PathVariable("id") Long id,
                               @PathVariable("companyId") Long companyId) {
        courseService.deleteCourseById(id);
        return "redirect:/courses/" + companyId;
    }

    @PostMapping("{companyId}/{courseId}/assignGroup")
    private String assignGroup(@PathVariable("companyId") Long comId,
                               @PathVariable("courseId") Long courseId,
                               @ModelAttribute("group") Group group)
            throws IOException {
        System.out.println(group);
        groupService.assignGroup(courseId, group.getId());
        return "redirect:/groups/" + comId+"/"+courseId;
    }

    @PostMapping("/{courseId}/assignInstructor")
    private String assignInstructor(@PathVariable("courseId") Long courseId,
                                    @ModelAttribute("instructor") Instructor instructor)
            throws IOException {
        System.out.println(instructor);
        instructorService.assignInstructor(courseId, instructor.getId());
        return "redirect:/instructors/"+courseId;
    }
}
