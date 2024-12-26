package org.example.service.impl;

import org.example.entity.Course;
import org.example.entity.Enrollment;
import org.example.entity.Student;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.EnrollmentRepository;
import org.example.repository.impl.EnrollmentRepositoryImpl;
import org.example.service.EnrollmentService;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the EnrollmentService interface.
 */
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryImpl();

    /**
     * Saves an enrollment entity to the database.
     *
     * @param enrollment the enrollment entity to save
     * @throws CustomException if there is an error while saving the enrollment
     */
    @Override
    public void saveEnrollment(Enrollment enrollment) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            enrollmentRepository.save(enrollment, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to save enrollment", ErrorCode.SAVE_ENROLLMENT_FAILED.getCode(), e);
        }
    }

    /**
     * Finds an enrollment entity by student and course.
     *
     * @param student the student entity
     * @param course  the course entity
     * @return the enrollment entity, or null if not found
     * @throws CustomException if there is an error while finding the enrollment
     */
    @Override
    public Enrollment findEnrollmentByStudentAndCourse(Student student, Course course) throws CustomException {
        try {
            return enrollmentRepository.findEnrollmentByStudentAndCourse(student, course);
        } catch (Exception e) {
            throw new CustomException("Failed to find enrollment by student and course", ErrorCode.FIND_ENROLLMENT_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Finds an enrollment entity by its ID.
     *
     * @param id the ID of the enrollment entity
     * @return the enrollment entity, or null if not found
     * @throws CustomException if there is an error while finding the enrollment
     */
    @Override
    public Enrollment findEnrollmentById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return enrollmentRepository.findById(id);
        } catch (Exception e) {
            throw new CustomException("Failed to find enrollment by ID", ErrorCode.FIND_ENROLLMENT_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Finds all enrollment entities.
     *
     * @return a list of all enrollment entities
     * @throws CustomException if there is an error while finding the enrollments
     */
    @Override
    public List<Enrollment> findAllEnrollments() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return enrollmentRepository.findAll();
        } catch (Exception e) {
            throw new CustomException("Failed to find all enrollments", ErrorCode.VIEW_ENROLLMENTS_FAILED.getCode());
        }
    }

    /**
     * Updates an enrollment entity in the database.
     *
     * @param enrollment the enrollment entity to update
     * @throws CustomException if there is an error while updating the enrollment
     */
    @Override
    public void updateEnrollment(Enrollment enrollment) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            enrollmentRepository.update(enrollment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to update enrollment", ErrorCode.UPDATE_ENROLLMENT_FAILED.getCode(), e);
        }
    }

    /**
     * Deletes an enrollment entity from the database.
     *
     * @param enrollment the enrollment entity to delete
     * @throws CustomException if there is an error while deleting the enrollment
     */
    @Override
    public void deleteEnrollment(Enrollment enrollment) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            enrollmentRepository.delete(enrollment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to delete enrollment", ErrorCode.DELETE_ENROLLMENT_FAILED.getCode(), e);
        }
    }

    /**
     * Authenticates an enrollment based on student and course.
     *
     * @param student the student entity * @param course the course entity
     * @return true if the student and course match an enrollment, otherwise false
     * @throws CustomException if authentication fails
     */
    @Override
    public boolean authenticate(Student student, Course course) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            Enrollment enrollment = enrollmentRepository.findEnrollmentByStudentAndCourse(student, course);
            if (enrollment != null) {
                return true;
            }
            throw new CustomException("Authentication failed", ErrorCode.LOGIN_USER_FAILED_USER_NOT_FOUND.getCode());
        } catch (Exception e) {
            throw new CustomException("Database connection error during authentication", ErrorCode.LOGIN_USER_FAILED_DB.getCode(), e);
        }
    }

    @Override
    public void recordGrades(Long courseId, Map<Long, Double> grades) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            enrollmentRepository.recordGrades(courseId, grades, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to record grades", ErrorCode.RECORD_GRADES_FAILED.getCode(), e);
        }
    }

    @Override
    public List<Enrollment> findEnrollmentsByCourseId(Long courseId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return enrollmentRepository.findEnrollmentsByCourseId(courseId, session);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve enrollments for course", ErrorCode.RETRIEVE_ENROLLMENTS_FAILED.getCode(), e);
        }
    }
}