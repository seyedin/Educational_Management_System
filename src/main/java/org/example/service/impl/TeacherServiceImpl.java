package org.example.service.impl;

import org.example.entity.Course;
import org.example.entity.Enrollment;
import org.example.entity.Student;
import org.example.entity.Teacher;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.TeacherRepository;
import org.example.repository.impl.TeacherRepositoryImpl;
import org.example.service.TeacherService;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            teacherRepository.save(teacher, session);
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

    /**
     * Changes the password of a teacher.
     *
     * @param teacherId   the ID of the teacher
     * @param newPassword the new password to set
     * @throws CustomException if there is an error while changing the password
     */
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

    /**
     * Displays the grades for students in the courses assigned to the current teacher.
     * This method retrieves the list of courses assigned to the teacher and displays the grades
     * of the students enrolled in those courses.
     *
     * @param teacherId the ID of the teacher
     * @throws CustomException if there is an error while retrieving the grades
     */
    @Override
    public void viewGrades(Long teacherId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.beginTransaction();
            Teacher teacher = session.get(Teacher.class, teacherId);
            if (teacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }

            List<Course> assignedCourses = teacherRepository.findCoursesByTeacherId(teacherId);
            if (assignedCourses.isEmpty()) {
                System.out.println("No assigned courses found.");
                return;
            }

            for (Course course : assignedCourses) {
                System.out.println("Course: " + course.getCourseName());
                List<Enrollment> enrollments = session.createQuery("FROM Enrollment WHERE course.id = :courseId", Enrollment.class)
                        .setParameter("courseId", course.getId())
                        .list();

                if (enrollments.isEmpty()) {
                    System.out.println("No students enrolled.");
                } else {
                    for (Enrollment enrollment : enrollments) {
                        System.out.println("Student: " + enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName() + " - Grade: " + enrollment.getGrade());
                    }
                }
                System.out.println("-----");
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new CustomException("Failed to view grades", ErrorCode.VIEW_GRADES_FAILED.getCode(), e);
        }
    }

    @Override
    public List<Student> viewEnrolledStudents(Long courseId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.beginTransaction();
            List<Enrollment> enrollments = session.createQuery("FROM Enrollment WHERE course.id = :courseId", Enrollment.class)
                    .setParameter("courseId", courseId)
                    .list();
            if (enrollments.isEmpty()) {
                return new ArrayList<>(); // هیچ دانشجویی ثبت‌نام نشده است
            } else {
                List<Student> students = enrollments.stream()
                        .map(Enrollment::getStudent)
                        .collect(Collectors.toList());
                session.getTransaction().commit();
                return students;
            }
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve enrolled students", ErrorCode.RETRIEVE_ENROLLMENTS_FAILED.getCode(), e);
        }
    }
}