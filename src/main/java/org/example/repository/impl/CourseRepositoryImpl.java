package org.example.repository.impl;

import org.example.entity.Course;
import org.example.entity.Student;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.CourseRepository;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;

import java.util.List;

/**
 * Implementation of the CourseRepository interface.
 */
public class CourseRepositoryImpl implements CourseRepository {

    /**
     * Saves a course entity to the database.
     *
     * @param course the course entity to save
     * @throws CustomException if there is an error while saving the course
     */
    @Override
    public void save(Course course) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.persist(course);
        } catch (Exception e) {
            throw new CustomException("Failed to create course", ErrorCode.CREATE_COURSE_FAILED.getCode());
        }
    }

    /**
     * Finds a course entity by its ID.
     *
     * @param id the ID of the course entity
     * @return the course entity, or null if not found
     * @throws CustomException if there is an error while finding the course
     */
    @Override
    public Course findById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            throw new CustomException("Failed to find course by ID", ErrorCode.FIND_COURSE_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Finds all course entities.
     *
     * @return a list of all course entities
     * @throws CustomException if there is an error while finding the courses
     */
    @Override
    public List<Course> findAll(Session session) throws CustomException {
        try {
            return session.createQuery("FROM Course", Course.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve courses", ErrorCode.RETRIEVE_COURSES_FAILED.getCode(), e);
        }
    }

    /**
     * Updates a course entity in the database.
     *
     * @param course the course entity to update
     * @throws CustomException if there is an error while updating the course
     */
    @Override
    public void update(Course course) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.merge(course);
        } catch (Exception e) {
            throw new CustomException("Failed to update course", ErrorCode.UPDATE_COURSE_FAILED.getCode());
        }
    }

    /**
     * Deletes a course entity from the database.
     *
     * @param course the course entity to delete
     * @throws CustomException if there is an error while deleting the course
     */
    @Override
    public void delete(Course course) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.remove(course);
        } catch (Exception e) {
            throw new CustomException("Failed to delete course", ErrorCode.DELETE_COURSE_FAILED.getCode());
        }
    }

    /**
     * Finds students enrolled in a course by the course ID.
     *
     * @param courseId the ID of the course
     * @return a list of students enrolled in the course
     * @throws CustomException if there is an error while finding the students
     */
    @Override
    public List<Student> findStudentsByCourseId(Long courseId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("SELECT e.student FROM Enrollment e WHERE e.course.id = :courseId", Student.class).setParameter("courseId", courseId).list();
        } catch (Exception e) {
            throw new CustomException("Failed to find students by course ID", ErrorCode.FIND_STUDENTS_FAILED_BY_COURSE_ID.getCode(), e);
        }
    }

    /**
     * Finds a course entity by its ID.
     *
     * @param courseId the ID of the course entity
     * @param session  the session object
     * @return the course entity, or null if not found
     * @throws CustomException if there is an error while finding the course
     */
    @Override
    public Course findById(Long courseId, Session session) throws CustomException {
        try {
            return session.get(Course.class, courseId);
        } catch (Exception e) {
            throw new CustomException("Failed to find course by ID", ErrorCode.COURSE_NOT_FOUND.getCode(), e);
        }
    }

    /**
     * Updates a course entity in the database.
     *
     * @param course  the course entity to update
     * @param session the session object
     * @throws CustomException if there is an error while updating the course
     */
    @Override
    public void update(Course course, Session session) throws CustomException {
        try {
            session.merge(course);
        } catch (Exception e) {
            throw new CustomException("Failed to update course", ErrorCode.UPDATE_COURSE_FAILED.getCode(), e);
        }
    }
}