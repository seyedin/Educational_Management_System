package org.example.service;

import org.example.entity.Course;
import org.example.entity.Student;
import org.example.entity.Teacher;
import org.example.exception.CustomException;

import java.util.List;

public interface AdminService {
    boolean authenticate(String username, String password);

    void registerStudent(Student student);

    List<Student> viewStudents();

    void registerTeacher(Teacher teacher);

    List<Teacher> viewTeachers();

    Teacher findTeacherByName(String teacherName) throws CustomException;

    Teacher findTeacherById(Long teacherId);

    void deleteTeacher(Teacher teacher);

    void createCourse(Course course);

    Course findCourseById(Long courseId);

    void updateCourse(Course course);

    void deleteCourse(Course course);

    void assignCourseToTeacher(Long courseId, Long teacherId) throws CustomException;
}
