package org.example.service.impl;

import org.example.entity.Student;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.StudentRepository;
import org.example.repository.impl.StudentRepositoryImpl;
import org.example.service.StudentService;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Implementation of the StudentService interface.
 */
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository = new StudentRepositoryImpl();

    /**
     * Saves a student entity to the database.
     *
     * @param student the student entity to save
     * @throws CustomException if there is an error while saving the student
     */
    @Override
    public void saveStudent(Student student) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            studentRepository.save(student, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
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
    public Student findStudentById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return studentRepository.findById(id);
        } catch (Exception e) {
            throw new CustomException("Failed to find student by ID", ErrorCode.FIND_STUDENT_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Finds all student entities.
     *
     * @return a list of all student entities
     * @throws CustomException if there is an error while finding the students
     */
    @Override
    public List<Student> findAllStudents() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return studentRepository.findAll();
        } catch (Exception e) {
            throw new CustomException("Failed to find all students", ErrorCode.VIEW_STUDENTS_FAILED.getCode());
        }
    }

    /**
     * Updates a student entity in the database.
     *
     * @param student the student entity with updated details
     * @throws CustomException if there is an error while updating the student
     */
    @Override
    public void updateStudent(Student student) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            studentRepository.update(student, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
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
    public void deleteStudent(Student student) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            studentRepository.delete(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to delete student", ErrorCode.DELETE_STUDENT_FAILED.getCode(), e);
        }
    }

    /**
     * Authenticates a student based on username and password.
     *
     * @param username the student's username
     * @param password the student's password
     * @return true if the username and password match a student, otherwise false
     * @throws CustomException if authentication fails
     */
    @Override
    public boolean authenticate(String username, String password) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            Student student = findByName(username);
            if (student != null && student.getPassword().equals(password)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new CustomException("Authentication failed", ErrorCode.LOGIN_USER_FAILED.getCode(), e);
        }
    }

    @Override
    public Student findByName(String username) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("FROM Student WHERE username = :username", Student.class).setParameter("username", username).uniqueResult();
        } catch (Exception e) {
            throw new CustomException("Failed to find student by username", ErrorCode.FIND_STUDENT_BY_ID_FAILED.getCode(), e);
        }
    }
}