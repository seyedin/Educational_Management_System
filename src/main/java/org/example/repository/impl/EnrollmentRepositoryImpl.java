package org.example.repository.impl;

import org.example.entity.Course;
import org.example.entity.Enrollment;
import org.example.entity.Student;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.EnrollmentRepository;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the EnrollmentRepository interface.
 */
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    /**
     * * Saves an enrollment entity to the database.
     *
     * @param enrollment the enrollment entity to save
     * @param session the Hibernate session
     * @throws CustomException if there is an error while saving the enrollment
     */
    @Override
    public void save(Enrollment enrollment, Session session) throws CustomException {
        try {
            session.persist(enrollment);
        } catch (Exception e) {
            throw new CustomException("Failed to save enrollment", ErrorCode.SAVE_ENROLLMENT_FAILED.getCode(), e);
        }
    }

    /**
     * Updates an enrollment entity in the database.
     *
     * @param enrollment the enrollment entity to update
     * @throws CustomException if there is an error while updating the enrollment
     */
    @Override
    public void update(Enrollment enrollment) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.merge(enrollment);
        } catch (Exception e) {
            throw new CustomException("Failed to update enrollment", ErrorCode.UPDATE_ENROLLMENT_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
        }
    }

    /**
     * Deletes an enrollment entity from the database.
     *
     * @param enrollment the enrollment entity to delete
     * @throws CustomException if there is an error while deleting the enrollment
     */
    @Override
    public void delete(Enrollment enrollment) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.remove(enrollment);
        } catch (Exception e) {
            throw new CustomException("Failed to delete enrollment", ErrorCode.DELETE_ENROLLMENT_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
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
    public Enrollment findById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.get(Enrollment.class, id);
        } catch (Exception e) {
            throw new CustomException("Failed to find enrollment by ID", ErrorCode.FIND_ENROLLMENT_BY_ID_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
        }
    }

    /**
     * Finds all enrollment entities.
     *
     * @return a list of all enrollment entities
     * @throws CustomException if there is an error while finding the enrollments
     */
    @Override
    public List<Enrollment> findAll() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("from Enrollment", Enrollment.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to find all enrollments", ErrorCode.VIEW_ENROLLMENTS_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
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
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            String hql = "FROM Enrollment E WHERE E.student = :student AND E.course = :course";
            Query query = session.createQuery(hql);
            query.setParameter("student", student);
            query.setParameter("course", course);
            return (Enrollment) query.uniqueResult();
        } catch (Exception e) {
            throw new CustomException("Failed to find enrollment by student and course", ErrorCode.FIND_ENROLLMENT_BY_ID_FAILED.getCode(), e); // استفاده از e برای نمایش جزئیات خطا
        }
    }

    /**
     * Records grades for students in a course.
     *
     * @param courseId the ID of the course
     * @param grades   a map of student IDs and their corresponding grades
     * @param session  the Hibernate session
     * @throws CustomException if there is an error while recording grades
     */
    @Override
    public void recordGrades(Long courseId, Map<Long, Double> grades, Session session) throws CustomException {
        try {
            for (Map.Entry<Long, Double> entry : grades.entrySet()) {
                Long studentId = entry.getKey();
                Double grade = entry.getValue();
                Enrollment enrollment = session.createQuery("FROM Enrollment WHERE course.id = :courseId AND student.id = :studentId", Enrollment.class).setParameter("courseId", courseId).setParameter("studentId", studentId).uniqueResult();
                if (enrollment != null) {
                    enrollment.setGrade(grade);
                }
            }
        } catch (Exception e) {
            throw new CustomException("Failed to record grades", ErrorCode.RECORD_GRADES_FAILED.getCode(), e);
        }
    }

    /**
     * Finds enrollments by course ID.
     *
     * @param courseId the ID of the course
     * @param session  the Hibernate session
     * @return a list of enrollments for the course
     * @throws CustomException if there is an error while finding the enrollments
     */
    @Override
    public List<Enrollment> findEnrollmentsByCourseId(Long courseId, Session session) throws CustomException {
        try {
            return session.createQuery("FROM Enrollment WHERE course.id = :courseId", Enrollment.class)
                    .setParameter("courseId", courseId)
                    .list();
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve enrollments for course", ErrorCode.RETRIEVE_ENROLLMENTS_FAILED.getCode(), e);
        }
    }
}
