package org.example.service;

import org.example.entity.Course;
import org.example.entity.Student;
import org.example.exception.CustomException;

import java.util.List;

public interface CourseService {
    void saveCourse(Course course);

    Course findCourseById(Long id);

    List<Course> findAllCourses();

    void updateCourse(Course course);

    void deleteCourse(Course course);

    boolean authenticate(Long courseId) throws CustomException;

    List<Student> getEnrolledStudents(Long courseId) throws CustomException;
}
