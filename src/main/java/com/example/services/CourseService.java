package com.example.services;

import com.example.models.Course;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    List<Course> getAllCourses(Long companyId);

    void addCourse(Long id, Course course) throws IOException;

    Course getCourseById(Long id);

    Course updateCourse(Course course, Long id) throws IOException;

    void deleteCourseById(Long id);

}
