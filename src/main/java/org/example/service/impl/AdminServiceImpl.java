package org.example.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.entity.Admin;
import org.example.entity.Course;
import org.example.entity.Student;
import org.example.entity.Teacher;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.AdminRepository;
import org.example.repository.CourseRepository;
import org.example.repository.StudentRepository;
import org.example.repository.TeacherRepository;
import org.example.repository.impl.AdminRepositoryImpl;
import org.example.repository.impl.CourseRepositoryImpl;
import org.example.repository.impl.StudentRepositoryImpl;
import org.example.repository.impl.TeacherRepositoryImpl;
import org.example.service.AdminService;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the AdminService interface.
 */
public class AdminServiceImpl implements AdminService {


    private final StudentRepository studentRepository = new StudentRepositoryImpl();
    private final TeacherRepository teacherRepository = new TeacherRepositoryImpl();
    private final AdminRepository adminRepository = new AdminRepositoryImpl();
    private final CourseRepository courseRepository = new CourseRepositoryImpl();
    private final Validator validator;

    private final List<Admin> admins = new ArrayList<>();

    /**
     * Constructor that initializes predefined admins.
     */
    public AdminServiceImpl() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        admins.add(new Admin(1L, "admin1", "123"));
        admins.add(new Admin(2L, "admin2", "456"));
        admins.add(new Admin(3L, "admin3", "789"));

        try {
            for (Admin admin : admins) {
                adminRepository.save(admin);
                System.out.println("Admin saved: " + admin.getUsername());
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticates an admin based on username and password.
     */
    @Override
    public boolean authenticate(String username, String password) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            for (Admin admin : admins) {
                if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                    return true;
                }
            }
            throw new CustomException("Authentication failed", ErrorCode.LOGIN_USER_FAILED_USER_NOT_FOUND.getCode());
        } catch (Exception e) {
            throw new CustomException("Database connection error during authentication", ErrorCode.LOGIN_USER_FAILED_DB.getCode(), e);
        }
    }

    /**
     * Registers a new student.
     *
     * @param student the student to be registered
     * @throws CustomException if there is an error while registering the student
     */

    @Override
    public void registerStudent(Student student) throws CustomException {
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Student> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage()).append("\n");
            }
            throw new CustomException("Validation failed: " + sb.toString(), ErrorCode.REGISTER_STUDENT_FAILED.getCode());
        }
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            studentRepository.save(student, session); // پاس دادن جلسه
            session.flush(); // اعمال تغییرات به پایگاه داده
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to register student", ErrorCode.REGISTER_STUDENT_FAILED.getCode(), e);
        }
    }

    /**
     * Returns a list of all students.
     *
     * @return the list of students
     * @throws CustomException if there is an error while fetching the students
     */
    @Override
    public List<Student> viewStudents() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("from Student", Student.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to view students", ErrorCode.VIEW_STUDENTS_FAILED.getCode());
        }
    }

    /**
     * Registers a new teacher.
     *
     * @param teacher the teacher to be registered
     * @throws CustomException if there is an error while registering the teacher
     */
    @Override
    public void registerTeacher(Teacher teacher) throws CustomException {
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Teacher> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage()).append("\n");
            }
            throw new CustomException("Validation failed: " + sb.toString(), ErrorCode.REGISTER_TEACHER_FAILED.getCode());
        }
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            teacherRepository.save(teacher, session); // پاس دادن جلسه
            session.flush(); // اعمال تغییرات به پایگاه داده
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to register teacher", ErrorCode.REGISTER_TEACHER_FAILED.getCode(), e);
        }
    }

    /**
     * Returns a list of all teachers.
     *
     * @return the list of teachers
     * @throws CustomException if there is an error while fetching the teachers
     */
    @Override
    public List<Teacher> viewTeachers() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("from Teacher", Teacher.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to view teachers", ErrorCode.VIEW_TEACHERS_FAILED.getCode());
        }
    }

    @Override
    public Teacher findTeacherByName(String teacherName) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            String[] nameParts = teacherName.split(" ");
            if (nameParts.length < 2) {
                throw new CustomException("Invalid teacher name format. Please enter both first name and last name.", ErrorCode.INVALID_TEACHER_NAME_FORMAT.getCode());
            }
            String firstName = nameParts[0];
            String lastName = nameParts[nameParts.length - 1];

            return session.createQuery("from Teacher where firstName = :firstName and lastName = :lastName", Teacher.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .uniqueResult();
        } catch (Exception e) {
            throw new CustomException("Failed to find teacher by name", ErrorCode.FIND_TEACHER_BY_NAME_FAILED.getCode(), e);
        }
    }

    /**
     * Finds a teacher by their ID.
     *
     * @param teacherId the ID of the teacher to be found
     * @return the teacher if found, otherwise null
     * @throws CustomException if there is an error while finding the teacher
     */
    @Override
    public Teacher findTeacherById(Long teacherId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.get(Teacher.class, teacherId);
        } catch (Exception e) {
            throw new CustomException("Failed to find teacher by ID", ErrorCode.FIND_TEACHER_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Deletes a teacher.
     *
     * @param teacher the teacher to be deleted
     * @throws CustomException if there is an error while deleting the teacher
     */
    @Override
    public void deleteTeacher(Teacher teacher) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Check if teacher exists in the database
            Teacher existingTeacher = session.get(Teacher.class, teacher.getId());
            if (existingTeacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }

            session.remove(existingTeacher);
            transaction.commit();
        } catch (CustomException e) {
            // Handle known custom exceptions separately
            if (transaction != null) {
                transaction.rollback();
            }
            throw e; // Re-throw custom exception
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to delete teacher", ErrorCode.DELETE_TEACHER_FAILED.getCode(), e);
        }
    }

    /**
     * Creates a new course.
     *
     * @param course the course to be created
     * @throws CustomException if there is an error while creating the course
     */
    @Override
    public void createCourse(Course course) throws CustomException {
        Transaction transaction = null;
        Session session = null; // تغییر: تعریف session در خارج از بلوک try-with-resources
        try {
            session = SessionFactoryInstance.sessionFactory.openSession();
            transaction = session.beginTransaction();

            // بررسی وجود دوره با همین نام
            Course existingCourse = session.createQuery("FROM Course WHERE courseName = :name", Course.class)
                    .setParameter("name", course.getCourseName())
                    .uniqueResult();
            if (existingCourse != null) {
                throw new CustomException("Course already exists", ErrorCode.COURSE_ALREADY_EXISTS.getCode());
            }

            // بررسی وجود معلم با نام
            Teacher teacher = session.createQuery("FROM Teacher WHERE lastName = :name", Teacher.class)
                    .setParameter("name", course.getTeacher().getLastName())
                    .uniqueResult();
            if (teacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }

            // اختصاص معلم به دوره و مقداردهی `teacherName`
            course.setTeacher(teacher);
            course.setTeacherName(teacher.getLastName()); // تغییر: تنظیم نام معلم

            session.persist(course);
            transaction.commit();
        } catch (CustomException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to create course", ErrorCode.CREATE_COURSE_FAILED.getCode(), e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Finds a course by its ID.
     *
     * @param courseId the ID of the course to be found
     * @return the course if found, otherwise null
     * @throws CustomException if there is an error while finding the course
     */
    @Override
    public Course findCourseById(Long courseId) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.get(Course.class, courseId);
        } catch (Exception e) {
            throw new CustomException("Failed to find course by ID", ErrorCode.FIND_COURSE_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Updates an existing course.
     *
     * @param course the course with updated details
     * @throws CustomException if there is an error while updating the course
     */
    @Override
    public void updateCourse(Course course) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to update course", ErrorCode.UPDATE_COURSE_FAILED.getCode());
        }
    }

    /**
     * Deletes a course.
     *
     * @param course the course to be deleted
     * @throws CustomException if there is an error while deleting the course
     */
    @Override
    public void deleteCourse(Course course) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to delete course", ErrorCode.DELETE_COURSE_FAILED.getCode());
        }
    }

    @Override
    public void assignCourseToTeacher(Long courseId, Long teacherId) throws CustomException {
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Course course = courseRepository.findById(courseId, session);
            Teacher teacher = teacherRepository.findById(teacherId, session);
            if (course == null) {
                throw new CustomException("Course not found", ErrorCode.COURSE_NOT_FOUND.getCode());
            }
            if (teacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }
            course.setTeacher(teacher);
            courseRepository.update(course, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CustomException("Failed to assign course to teacher", ErrorCode.ASSIGN_COURSE_FAILED.getCode(), e);
        }
    }
}
