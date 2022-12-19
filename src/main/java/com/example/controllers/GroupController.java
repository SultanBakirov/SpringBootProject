package com.example.controllers;

import com.example.models.Group;
import com.example.models.Student;
import com.example.services.CourseService;
import com.example.services.GroupService;
import com.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class GroupController {
    private final GroupService groupService;
    private final StudentService studentService;

    private final CourseService courseService;

    @Autowired
    public GroupController(GroupService groupService, StudentService studentService, CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/groups/{id}")
    public String listGroups(@PathVariable Long id, @ModelAttribute("student") Student student,
                             Model model) {
        model.addAttribute("groups", groupService.getAllGroups(id));
        model.addAttribute("students", studentService.getAllListStudent());
        model.addAttribute("courseId", id);
        model.addAttribute("companyId", courseService.getCourseById(id).getCompany().getId());
        return "/group/groups";
    }

    @GetMapping("/groups/{id}/new")
    public String createGroupForm(@PathVariable Long id, Model model) {
        model.addAttribute("courseId", id);
        model.addAttribute("group", new Group());
        return "/group/create_group";
    }

    @PostMapping("/{id}/groups")
    public String saveGroup(@PathVariable Long id, @ModelAttribute("group") Group group) throws IOException {
        groupService.addGroup(id, group);
        return "redirect:/groups/" + id;
    }

    @GetMapping("/updateGroup/{id}")
    public String editGroupForm(@PathVariable("id") Long id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);
        model.addAttribute("companyId", id);
        model.addAttribute("courseId", group.getCompany().getId());
        groupService.updateGroup(group, id);
        return "/group/edit_group";
    }

    @PostMapping("/{courseId}/{id}/group")
    public String updateGroup(@PathVariable("courseId") Long courseId,
                              @PathVariable("id") Long id,
                              @ModelAttribute("group") Group group) throws IOException{
        groupService.updateGroup(group, id);
        return "redirect:/groups/" + courseId;
    }

    @GetMapping("/{courseId}/{id}/deleteGroup")
    public String deleteGroup(@PathVariable("id") Long id,
                              @PathVariable("courseId") Long courseId) {
        groupService.deleteGroupById(id);
        return "redirect:/groups/" +courseId;
    }

    @PostMapping("/{groupId}/assignStudent")
    private String assignStudent(@PathVariable("groupId") Long groupId,
                                 @ModelAttribute("student") Student student)
            throws IOException {
        System.out.println(student);
        studentService.assignStudent(groupId, student.getId());
        return "redirect:/students/"+groupId;
    }
}
