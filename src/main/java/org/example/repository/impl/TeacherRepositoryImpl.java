package org.example.repository.impl;

import org.example.entity.Course;
import org.example.entity.Teacher;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.TeacherRepository;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;

import java.util.List;

/**
 * Implementation of the TeacherRepository interface.
 */
public class TeacherRepositoryImpl implements TeacherRepository {

    /**
     * Saves a teacher entity to the database.
     *
     * @param teacher the teacher entity to save
     * @throws CustomException if there is an error while saving the teacher
     */
    @Override
    public void save(Teacher teacher, Session session) throws CustomException {
        try {
            session.persist(teacher);
        } catch (Exception e) {
            throw new CustomException("Failed to save teacher", ErrorCode.REGISTER_TEACHER_FAILED.getCode(), e);
        }
    }

    /**
     * Finds a teacher entity by its ID.
     *
     * @param teacherId the ID of the teacher
     * @param session the Hibernate session
     * @return the teacher entity, or null if not found
     * @throws CustomException if there is an error while finding the teacher
     */
    @Override
    public Teacher findById(Long teacherId, Session session) throws CustomException {
        try {
            return session.get(Teacher.class, teacherId);
        } catch (Exception e) {
            throw new CustomException("Failed to find teacher by ID", ErrorCode.TEACHER_NOT_FOUND.getCode(), e);
        }
    }

    /**
     * Finds all teacher entities.
     *
     * @return a list of all teacher entities
     * @throws CustomException if there is an error while finding the teachers
     */
    @Override
    public List<Teacher> findAll() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("from Teacher", Teacher.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to find all teachers", ErrorCode.VIEW_TEACHERS_FAILED.getCode(), e);
        }
    }

    /**
     * This method retrieves a teacher from the database based on their last name.
     *
     * @param name the last name of the teacher to be found
     * @return the teacher with the specified last name, or null if no teacher is found
     * @throws CustomException if there is an error while retrieving the teacher
     */
    @Override
    public Teacher findByName(String name) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("FROM Teacher WHERE lastName = :name", Teacher.class)
                    .setParameter("name", name) // تصحیح پارامتر به "name"
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
    public void update(Teacher teacher) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.merge(teacher);
        } catch (Exception e) {
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
    public void delete(Teacher teacher) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.remove(teacher);
        } catch (Exception e) {
            throw new CustomException("Failed to delete teacher", ErrorCode.DELETE_TEACHER_FAILED.getCode(), e);
        }
    }

    @Override
    public List<Course> findCoursesByTeacherId(Long teacherId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("FROM Course WHERE teacher.id = :teacherId", Course.class).setParameter("teacherId", teacherId).list();
        } catch (Exception e) {
            throw new CustomException("Failed to find courses by teacher ID", ErrorCode.FIND_COURSES_FAILED_BY_TEACHER_ID.getCode(), e);
        }
    }

    /**
     * Changes the password of a teacher.
     *
     * @param teacherId   the ID of the teacher
     * @param newPassword the new password to set
     * @throws CustomException if there is an error while changing the password
     */
    @Override
    public void changePassword(Long teacherId, String newPassword) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            Teacher teacher = session.get(Teacher.class, teacherId);
            if (teacher != null) {
                teacher.setPassword(newPassword);
                session.beginTransaction();
                session.merge(teacher);
                session.getTransaction().commit();
            } else {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }
        } catch (Exception e) {
            throw new CustomException("Failed to change teacher's password", ErrorCode.UPDATE_TEACHER_FAILED.getCode(), e);
        }
    }


    /**
     * Retrieves the profile of a teacher by their ID.
     *
     * @param teacherId the ID of the teacher
     * @return the teacher entity
     * @throws CustomException if there is an error while retrieving the profile
     */
    @Override
    public Teacher getProfile(Long teacherId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            Teacher teacher = session.get(Teacher.class, teacherId);
            if (teacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }
            return teacher;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve teacher's profile", ErrorCode.RETRIEVE_PROFILE_FAILED.getCode(), e);
        }
    }
}
