package org.example.repository;

import org.example.entity.Course;
import org.example.entity.Student;
import org.example.exception.CustomException;
import org.hibernate.Session;

import java.util.List;

public interface CourseRepository {
    void save(Course course);

    List<Course> findAll(Session session) throws CustomException;

    void update(Course course);

    void delete(Course course);

    Course findById(Long id);

    List<Student> findStudentsByCourseId(Long courseId) throws CustomException;

    Course findById(Long courseId, Session session) throws CustomException;

    void update(Course course, Session session) throws CustomException;
}
