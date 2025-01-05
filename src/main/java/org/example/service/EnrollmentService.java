package org.example.service;

import org.example.entity.Course;
import org.example.entity.Enrollment;
import org.example.entity.Student;
import org.example.exception.CustomException;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    void saveEnrollment(Enrollment enrollment);
    Enrollment findEnrollmentByStudentAndCourse(Student student, Course course);
    Enrollment findEnrollmentById(Long id);
    List<Enrollment> findAllEnrollments();
    void updateEnrollment(Enrollment enrollment);
    void deleteEnrollment(Enrollment enrollment);

    boolean authenticate(Student student, Course course) throws CustomException;

    void recordGrades(Long courseId, Map<Long, Double> grades) throws CustomException;

    List<Enrollment> findEnrollmentsByCourseId(Long courseId) throws CustomException;

    List<Enrollment> findEnrollmentsByStudentId(Long studentId) throws CustomException;
}
