package org.example.service;

import org.example.entity.Course;
import org.example.entity.Student;
import org.example.entity.Teacher;
import org.example.exception.CustomException;

import java.util.List;

public interface TeacherService {
    void saveTeacher(Teacher teacher);

    Teacher findTeacherById(Long id);

    List<Teacher> findAllTeachers();

    Teacher findByName(String name) throws CustomException;

    void updateTeacher(Teacher teacher);

    void deleteTeacher(Teacher teacher);

    boolean authenticate(String username, String password) throws CustomException;

    List<Course> getAssignedCourses(Long teacherId) throws CustomException;

    void changePassword(Long teacherId, String newPassword) throws CustomException;

    void viewGrades(Long studentId) throws CustomException;

    List<Student> viewEnrolledStudents(Long courseId) throws CustomException;
}
