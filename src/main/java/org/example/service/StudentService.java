package org.example.service;

import org.example.entity.Student;
import org.example.exception.CustomException;

import java.util.List;

public interface StudentService {
    void saveStudent(Student student);

    Student findStudentById(Long id);

    List<Student> findAllStudents();

    void updateStudent(Student student);

    void deleteStudent(Student student);

    boolean authenticate(String username, String password) throws CustomException;

    Student findByName(String username) throws CustomException;
}
