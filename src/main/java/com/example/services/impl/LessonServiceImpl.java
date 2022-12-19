package com.example.services.impl;

import com.example.models.Course;
import com.example.models.Lesson;
import com.example.repositories.CourseRepository;
import com.example.repositories.LessonRepository;
import com.example.services.LessonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public LessonServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<Lesson> getAllLessons(Long courseId) {
        return lessonRepository.getAllLessons(courseId);
    }

    @Override
    public void addLesson(Long id, Lesson lesson) {
        Course course = courseRepository.findById(id).get();
        course.addLesson(lesson);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).get();
    }

    @Override
    public void updateLesson(Lesson lesson, Long id) {
        Lesson lesson1 = lessonRepository.findById(id).get();
        lesson1.setLessonName(lesson.getLessonName());
        lessonRepository.save(lesson1);
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.deleteById(id);
    }
}
