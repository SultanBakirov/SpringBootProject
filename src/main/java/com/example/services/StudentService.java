package com.example.services;

import com.example.models.Student;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    List<Student> getAllListStudent();

    List<Student> getAllStudents(Long groupId);

    void addStudent(Long id, Student student) throws IOException;

    Student getStudentById(Long id);

    void updateStudent(Student student, Long id) throws IOException;

    void deleteStudentById(Long id);

    void assignStudent(Long groupId, Long studentId) throws IOException;

}
