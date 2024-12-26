package org.example.repository;

import org.example.entity.Course;
import org.example.entity.Enrollment;
import org.example.entity.Student;
import org.example.exception.CustomException;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

public interface EnrollmentRepository {

    Enrollment findById(Long id);

    List<Enrollment> findAll();

    void save(Enrollment enrollment, Session session) throws CustomException;

    void update(Enrollment enrollment);

    void delete(Enrollment enrollment);

    Enrollment findEnrollmentByStudentAndCourse(Student student, Course course);

    void recordGrades(Long courseId, Map<Long, Double> grades, Session session) throws CustomException;

    List<Enrollment> findEnrollmentsByCourseId(Long courseId, Session session) throws CustomException;
}
