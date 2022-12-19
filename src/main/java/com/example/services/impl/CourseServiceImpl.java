package com.example.services.impl;

import com.example.models.Company;
import com.example.models.Course;
import com.example.repositories.CompanyRepository;
import com.example.repositories.CourseRepository;
import com.example.services.CourseService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CompanyRepository companyRepository;
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CompanyRepository companyRepository, CourseRepository courseRepository) {
        this.companyRepository = companyRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public List<Course> getAllCourses(Long companyId) {
        return courseRepository.getAllCourses(companyId);
    }

    @Override
    public void addCourse(Long id, Course course) throws IOException {
        validator(course.getCourseName(),course.getDuration(), course.getDescription());
        Company company = companyRepository.findById(id).get();
        company.addCourse(course);
        course.setCompany(company);
        courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public Course updateCourse(Course course, Long id) throws IOException {
        validator(course.getCourseName(),course.getDuration(), course.getDescription());
        Course course1 = courseRepository.findById(id).get();
        course1.setCourseName(course.getCourseName());
        course1.setDuration(course.getDuration());
        course1.setDescription(course.getDescription());
        return courseRepository.save(course1);
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    private void validator(String courseName, String description, String duration) throws IOException {
        if (courseName.length()>3 && duration.length()>0 && duration.length()<24 && description.length()>5 && description.length()<15){
            for (Character i: courseName.toCharArray()) {
                if (!Character.isLetter(i)){
                    throw new IOException("You can't put numbers in the course name!");
                }
            }
        }else {
            throw new IOException("Form error course registration");
        }
    }
}
