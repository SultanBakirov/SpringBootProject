package com.example.services.impl;

import com.example.models.*;
import com.example.services.GroupService;
import com.example.spring.models.*;
import com.example.repositories.CompanyRepository;
import com.example.repositories.CourseRepository;
import com.example.repositories.GroupRepository;
import com.example.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final CompanyRepository companyRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public GroupServiceImpl(CompanyRepository companyRepository, CourseRepository courseRepository,
                            GroupRepository groupRepository, StudentRepository studentRepository) {
        this.companyRepository = companyRepository;
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public List<Group> getAllGroups(Long companyId) {
        return groupRepository.getAllGroups(companyId);
    }

    @Override
    public void addGroup(Long id, Group group) {
        Company company = companyRepository.findById(id).get();
        company.addGroup(group);
        group.setCompany(company);
        groupRepository.save(group);
    }

    @Override
    public Group getGroupById(Long id) {
        return groupRepository.findById(id).get();
    }

    @Override
    public void updateGroup(Group group, Long id) {
        Group group1 = groupRepository.findById(id).get();
        group1.setGroupName(group.getGroupName());
        group1.setDateOfStart(group.getDateOfStart());
        group1.setImage(group.getImage());
        groupRepository.save(group1);
    }

    @Override
    public void deleteGroupById(Long id) {
        Group group = groupRepository.getById(id);
        for (Student s: group.getStudents()) {
            group.getCompany().minusStudent();
        }

        for (Course c: group.getCourses()) {
            for (Student student: group.getStudents()) {
                for (Instructor i: c.getInstructors()) {
                    i.minus();
                }
            }
        }

        for (Course c : group.getCourses()) {
            c.getGroups().remove(group);
            group.minusCount();
        }
        studentRepository.deleteAll(group.getStudents());
        group.setCourses(null);
        groupRepository.delete(group);
    }

    @Override
    public void assignGroup(Long courseId, Long groupId) throws IOException {
        Group group = groupRepository.findById(groupId).get();
        Course course = courseRepository.findById(courseId).get();
        if (course.getGroups()!=null){
            for (Group g : course.getGroups()) {
                if (g.getId() == groupId) {
                    throw new IOException("This group already exists!");
                }
            }
        }

        if (course.getInstructors() != null) {
            for (Instructor i: course.getInstructors()) {
                for (Student s: group.getStudents()) {
                    i.plus();
                }
            }
        }

        group.addCourse(course);
        course.addGroup(group);
        courseRepository.save(course);
        groupRepository.save(group);
    }
}
