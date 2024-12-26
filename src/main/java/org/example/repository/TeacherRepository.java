package org.example.repository;

import org.example.entity.Course;
import org.example.entity.Teacher;
import org.example.exception.CustomException;
import org.hibernate.Session;

import java.util.List;

public interface TeacherRepository {
    Teacher findByName(String name) throws CustomException;

    void update(Teacher teacher);

    void delete(Teacher teacher);

    void save(Teacher teacher, Session session) throws CustomException;

    Teacher findById(Long teacherId, Session session) throws CustomException;

    List<Teacher> findAll();

    List<Course> findCoursesByTeacherId(Long teacherId) throws CustomException;

    void changePassword(Long teacherId, String newPassword) throws CustomException;

    Teacher getProfile(Long teacherId) throws CustomException;
}