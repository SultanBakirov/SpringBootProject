package com.example.services.impl;

import com.example.models.Course;
import com.example.models.Group;
import com.example.models.Instructor;
import com.example.models.Student;
import com.example.repositories.GroupRepository;
import com.example.repositories.StudentRepository;
import com.example.services.StudentService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;


    public StudentServiceImpl(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public List<Student> getAllListStudent() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudents(Long groupId) {
        return studentRepository.getAllStudents(groupId);
    }

    @Override
    public void addStudent(Long id, Student student) throws IOException {
        List<Student> students = studentRepository.findAll();
        for (Student i : students) {
            if (i.getEmail().equals(student.getEmail())) {
                throw new IOException("Student with email already exists!");
            }
        }

        Group group = groupRepository.getById( id);
        group.addStudent(student);
        student.setGroup(group);
        for (Course c:student.getGroup().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }

        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public void updateStudent(Student student, Long id) throws IOException {
        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        Student student1 = studentRepository.getById(id);
        student1.setFirstName(student.getFirstName());
        student1.setLastName(student.getLastName());
        student1.setPhoneNumber(student.getPhoneNumber());
        student1.setEmail(student.getEmail());
        student1.setStudyFormat(student.getStudyFormat());
        studentRepository.save(student1);
    }

    @Override
    public void deleteStudentById(Long id) {
        Student student = studentRepository.getById(id);
        student.getGroup().getCompany().minusStudent();
        for (Course c:student.getGroup().getCourses()) {
            for (Instructor i:c.getInstructors()) {
                i.minus();
            }
        }
        student.setGroup(null);
        studentRepository.delete(student);
    }

    @Override
    public void assignStudent(Long groupId, Long studentId) throws IOException {
        Student student = studentRepository.getById(studentId);
        Group group = groupRepository.getById(groupId);

        if (group.getStudents()!=null){
            for (Student g : group.getStudents()) {
                if (g.getId() == studentId) {
                    throw new IOException("This student already exists!");
                }
            }
        }
        for (Course c: student.getGroup().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.minus();
            }
        }
        for (Course c: group.getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }
        student.getGroup().getStudents().remove(student);
        group.assignStudent(student);
        student.setGroup(group);
        studentRepository.save(student);
        groupRepository.save(group);
    }

    private void validator(String phone, String firstName, String lastName) throws IOException {
        if (firstName.length()>2 && lastName.length()>2) {
            for (Character i : firstName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("Numbers cannot be inserted in the name of the instructor");
                }
            }

            for (Character i : lastName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("Numbers cannot be inserted into the lastname of the instructor");
                }
            }
        } else {
            throw new IOException("Instructor's first or last name must contain at least 3 letters");
        }

        if (phone.length()==12
                && phone.charAt(0) == '+'
                && phone.charAt(1) == '4'
                && phone.charAt(2) == '8'){
            int counter = 0;

            for (Character i : phone.toCharArray()) {
                if (counter!=0){
                    if (!Character.isDigit(i)) {
                        throw new IOException("Number format is not correct");
                    }
                }
                counter++;
            }
        }else {
            throw new IOException("Number format is not correct");
        }
    }
}
