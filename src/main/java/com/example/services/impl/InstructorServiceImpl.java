package com.example.services.impl;

import com.example.models.Course;
import com.example.models.Group;
import com.example.models.Instructor;
import com.example.models.Student;
import com.example.repositories.CourseRepository;
import com.example.repositories.InstructorRepository;
import com.example.services.InstructorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public InstructorServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public List<Instructor> getAllInstructorsByCourseId(Long courseId) {
        return instructorRepository.getAllInstructors(courseId);
    }

    @Override
    public void addInstructor(Long id, Instructor instructor) throws IOException {
        Course course = courseRepository.findById(id).get();
        if (course.getGroups()!=null){
            for (Group group : course.getGroups()) {
                for (Student student: group.getStudents()) {
                    instructor.plus();
                }
            }
        }
        validator(instructor.getPhoneNumber().replace(" ", ""), instructor.getLastName().replace(" ", ""),
                instructor.getFirstName().replace(" ", ""));
        course.addInstructor(instructor);
        instructor.setCourse(course);
        instructorRepository.save(instructor);
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id).get();
    }

    @Override
    public void updateInstructor(Instructor instructor, Long id) throws IOException {
        validator(instructor.getPhoneNumber().replace(" ", ""), instructor.getLastName().replace(" ", ""),
                instructor.getFirstName().replace(" ", ""));
        Instructor instructor1 = instructorRepository.findById(id).get();
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setEmail(instructor.getEmail());
        instructor1.setSpecialization(instructor.getSpecialization());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructorRepository.save(instructor1);
    }

    @Override
    public void deleteInstructorById(Long id) {
        instructorRepository.deleteById(id);
    }

    @Override
    public void assignInstructor(Long courseId, Long instructorId) throws IOException {
        Instructor instructor = instructorRepository.getById(instructorId);
        Course course = courseRepository.getById(courseId);
        if (course.getInstructors()!=null){
            for (Instructor g : course.getInstructors()) {
                if (g.getId() == instructorId) {
                    throw new IOException("This instructor already exists!");
                }
            }
        }
        for (Group g:instructor.getCourse().getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.minus();
            }
        }
        for (Group g: course.getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.plus();
            }
        }
        instructor.getCourse().getInstructors().remove(instructor);
        instructor.setCourse(course);
        course.addInstructor(instructor);
        instructorRepository.save(instructor);
        courseRepository.save(course);
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
                    throw new IOException("Numbers cannot be inserted into the name of the instructor");
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
