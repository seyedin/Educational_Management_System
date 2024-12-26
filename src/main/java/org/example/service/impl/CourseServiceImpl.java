package org.example.service.impl;

import org.example.entity.Course;
import org.example.entity.Student;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.CourseRepository;
import org.example.repository.impl.CourseRepositoryImpl;
import org.example.service.CourseService;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Implementation of the CourseService interface.
 */
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository = new CourseRepositoryImpl();

    /**
     * Saves a course entity to the database.
     *
     * @param course the course entity to save
     * @throws CustomException if there is an error while saving the course
     */
    @Override
    public void saveCourse(Course course) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            courseRepository.save(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to save course", ErrorCode.CREATE_COURSE_FAILED.getCode(), e);
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
    public Course findCourseById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return courseRepository.findById(id);
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
    public List<Course> findAllCourses() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return courseRepository.findAll(session);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve courses", ErrorCode.RETRIEVE_COURSES_FAILED.getCode(), e);
        }
    }

    /**
     * Updates a course entity in the database.
     *
     * @param course the course entity with updated details
     * @throws CustomException if there is an error while updating the course
     */
    @Override
    public void updateCourse(Course course) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            courseRepository.update(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to update course", ErrorCode.UPDATE_COURSE_FAILED.getCode(), e);
        }
    }

    /**
     * Deletes a course entity from the database.
     *
     * @param course the course entity to delete
     * @throws CustomException if there is an error while deleting the course
     */
    @Override
    public void deleteCourse(Course course) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            courseRepository.delete(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to delete course", ErrorCode.DELETE_COURSE_FAILED.getCode(), e);
        }
    }

    /**
     * Authenticates a course based on its ID.
     *
     * @param courseId the ID of the course
     * @return true if the course is found, otherwise false
     * @throws CustomException if authentication fails
     */
    @Override
    public boolean authenticate(Long courseId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            Course course = courseRepository.findById(courseId);
            if (course != null) {
                return true;
            }
            throw new CustomException("Authentication failed", ErrorCode.LOGIN_USER_FAILED_USER_NOT_FOUND.getCode());
        } catch (Exception e) {
            throw new CustomException("Database connection error during authentication", ErrorCode.LOGIN_USER_FAILED_DB.getCode(), e);
        }
    }

    /**
     * Retrieves the list of students enrolled in a specific course.
     *
     * @param courseId the ID of the course
     * @return a list of students enrolled in the course
     * @throws CustomException if there is an error while retrieving the students
     */
    @Override
    public List<Student> getEnrolledStudents(Long courseId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return courseRepository.findStudentsByCourseId(courseId);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve enrolled students", ErrorCode.RETRIEVE_STUDENTS_FAILED.getCode(), e);
        }
    }
}
