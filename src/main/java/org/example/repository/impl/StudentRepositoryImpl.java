package org.example.repository.impl;

import org.example.entity.Student;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.StudentRepository;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;

import java.util.List;

/**
 * Implementation of the StudentRepository interface.
 */
public class StudentRepositoryImpl implements StudentRepository {

    /**
     * Saves a student entity to the database.
     *
     * @param student the student entity to save
     * @param session the Hibernate session
     * @throws CustomException if there is an error while saving the student
     */
    @Override
    public void save(Student student, Session session) throws CustomException {
        try {
            session.persist(student);
        } catch (Exception e) {
            throw new CustomException("Failed to save student", ErrorCode.REGISTER_STUDENT_FAILED.getCode(), e);
        }
    }

    /**
     * Finds a student entity by its ID.
     *
     * @param id the ID of the student entity
     * @return the student entity, or null if not found
     * @throws CustomException if there is an error while finding the student
     */
    @Override
    public Student findById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            throw new CustomException("Failed to find student by ID", ErrorCode.FIND_STUDENT_BY_ID_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
        }
    }

    /**
     * Finds all student entities.
     *
     * @return a list of all student entities
     * @throws CustomException if there is an error while finding the students
     */
    @Override
    public List<Student> findAll() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("from Student", Student.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to find all students", ErrorCode.VIEW_STUDENTS_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
        }
    }

    /**
     * Updates a student entity in the database.
     *
     * @param student the student entity to update
     * @param session the Hibernate session
     * @throws CustomException if there is an error while updating the student
     */
    @Override
    public void update(Student student, Session session) throws CustomException {
        try {
            session.merge(student);
        } catch (Exception e) {
            throw new CustomException("Failed to update student", ErrorCode.UPDATE_STUDENT_FAILED.getCode(), e);
        }
    }

    /**
     * Deletes a student entity from the database.
     *
     * @param student the student entity to delete
     * @throws CustomException if there is an error while deleting the student
     */
    @Override
    public void delete(Student student) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.remove(student);
        } catch (Exception e) {
            throw new CustomException("Failed to delete student", ErrorCode.DELETE_STUDENT_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
        }
    }
}