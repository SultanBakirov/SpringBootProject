package com.example.services;

import com.example.models.Lesson;

import java.util.List;

public interface LessonService {

    List<Lesson> getAllLessons(Long courseId);

    void addLesson(Long id, Lesson lesson);

    Lesson getLessonById(Long id);

    void updateLesson(Lesson lesson, Long id);

    void deleteLessonById(Long id);

}
