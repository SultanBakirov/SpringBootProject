package com.example.services;

import com.example.models.Instructor;

import java.io.IOException;
import java.util.List;

public interface InstructorService {

    List<Instructor> getAllInstructors();

    List<Instructor> getAllInstructorsByCourseId(Long courseId);

    void addInstructor(Long id, Instructor instructor) throws IOException;

    Instructor getInstructorById(Long id);

    void updateInstructor(Instructor instructor, Long id) throws IOException;

    void deleteInstructorById(Long id);

    void assignInstructor(Long courseId, Long instructorId) throws IOException;

}
