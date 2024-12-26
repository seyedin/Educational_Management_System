package org.example.repository;

import org.example.entity.Student;
import org.example.exception.CustomException;
import org.hibernate.Session;

import java.util.List;

public interface StudentRepository {

    void update(Student student, Session session) throws CustomException;

    void delete(Student student);

    void save(Student student, Session session) throws CustomException;

    Student findById(Long id);
    List<Student> findAll();
}