package org.example.service.impl;

import org.example.entity.Course;
import org.example.entity.Teacher;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.TeacherRepository;
import org.example.repository.impl.TeacherRepositoryImpl;
import org.example.service.TeacherService;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Implementation of the TeacherService interface.
 */
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository = new TeacherRepositoryImpl();

    /**
     * Saves a teacher entity to the database.
     *
     * @param teacher the teacher entity to save
     * @throws CustomException if there is an error while saving the teacher
     */
    @Override
    public void saveTeacher(Teacher teacher) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            teacherRepository.save(teacher, session); // پاس دادن جلسه
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to save teacher", ErrorCode.REGISTER_TEACHER_FAILED.getCode(), e);
        }
    }

    /**
     * Finds a teacher entity by its ID.
     *
     * @param id the ID of the teacher entity
     * @return the teacher entity, or null if not found
     * @throws CustomException if there is an error while finding the teacher
     */
    @Override
    public Teacher findTeacherById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return teacherRepository.findById(id, session);
        } catch (Exception e) {
            throw new CustomException("Failed to find teacher by ID", ErrorCode.FIND_TEACHER_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Finds all teacher entities.
     *
     * @return a list of all teacher entities
     * @throws CustomException if there is an error while finding the teachers
     */
    @Override
    public List<Teacher> findAllTeachers() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return teacherRepository.findAll();
        } catch (Exception e) {
            throw new CustomException("Failed to find all teachers", ErrorCode.VIEW_TEACHERS_FAILED.getCode());
        }
    }

    /**
     * Finds a teacher by their last name.
     * This method retrieves a teacher from the database based on their last name.
     *
     * @param name the last name of the teacher to be found
     * @return the teacher with the specified last name, or null if no teacher is found
     * @throws CustomException if there is an error while retrieving the teacher
     */
    @Override
    public Teacher findByName(String name) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("FROM Teacher WHERE username = :name", Teacher.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            throw new CustomException("Failed to find teacher by name", ErrorCode.TEACHER_NOT_FOUND.getCode(), e);
        }
    }


    /**
     * Updates a teacher entity in the database.
     *
     * @param teacher the teacher entity to update
     * @throws CustomException if there is an error while updating the teacher
     */
    @Override
    public void updateTeacher(Teacher teacher) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            teacherRepository.update(teacher);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to update teacher", ErrorCode.UPDATE_TEACHER_FAILED.getCode(), e);
        }
    }

    /**
     * Deletes a teacher entity from the database.
     *
     * @param teacher the teacher entity to delete
     * @throws CustomException if there is an error while deleting the teacher
     */
    @Override
    public void deleteTeacher(Teacher teacher) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            teacherRepository.delete(teacher);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to delete teacher", ErrorCode.DELETE_TEACHER_FAILED.getCode(), e);
        }
    }

    /**
     * Authenticates a teacher based on username and password.
     *
     * @param username the teacher's username
     * @param password the teacher's password
     * @return true if the username and password match a teacher, otherwise false
     * @throws CustomException if authentication fails
     */
    @Override
    public boolean authenticate(String username, String password) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            List<Teacher> teachers = session.createQuery("from Teacher", Teacher.class).list();
            for (Teacher teacher : teachers) {
                if (teacher.getUsername().equals(username) && teacher.getPassword().equals(password)) {
                    return true;
                }
            }
            throw new CustomException("Authentication failed", ErrorCode.LOGIN_USER_FAILED_USER_NOT_FOUND.getCode());
        } catch (Exception e) {
            throw new CustomException("Database connection error during authentication", ErrorCode.LOGIN_USER_FAILED_DB.getCode(), e);
        }
    }

    /**
     * Retrieves the list of courses assigned to the teacher.
     *
     * @param teacherId the teacher's ID
     * @return the list of assigned courses
     * @throws CustomException if there is an error while retrieving the courses
     */
    @Override
    public List<Course> getAssignedCourses(Long teacherId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return teacherRepository.findCoursesByTeacherId(teacherId);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve assigned courses", ErrorCode.RETRIEVE_COURSES_FAILED.getCode(), e);
        }
    }

    @Override
    public void changePassword(Long teacherId, String newPassword) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            teacherRepository.changePassword(teacherId, newPassword);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to change teacher's password", ErrorCode.UPDATE_TEACHER_FAILED.getCode(), e);
        }
    }
}