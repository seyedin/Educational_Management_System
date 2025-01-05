package org.example.view;

import org.example.entity.*;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.service.*;
import org.example.service.impl.*;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.*;

public class Main {

    private static final AdminService adminService = new AdminServiceImpl();
    private static final StudentService studentService = new StudentServiceImpl();
    private static final TeacherService teacherService = new TeacherServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private static final List<Admin> admins = new ArrayList<>();
    private static Long currentTeacherId = null;
    private static Long currentStudentId = null;

    /**
     * The entry point of the application. It initializes admins and displays the main menu.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        initializeAdmins(); // Initialize predefined admins
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            try {
                System.out.println("Welcome to the Educational Management System");
                System.out.println("1. Admin");
                System.out.println("2. Teacher");
                System.out.println("3. Student");
                System.out.println("4. Exit");
                System.out.print("Please select your role: ");
                int role = scanner.nextInt();
                scanner.nextLine();

                switch (role) {
                    case 1:
                        selectAdmin(scanner);
                        break;
                    case 2:
                        teacherLogin(scanner);
                        break;
                    case 3:
                        studentLogin(scanner);
                        break;
                    case 4:
                        System.out.println("Exiting the system. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (CustomException e) {
                System.out.println("An error occurred: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Initializes predefined admin users.
     */
    private static void initializeAdmins() {
        admins.add(new Admin(1L, "admin1", "password1"));
        admins.add(new Admin(2L, "admin2", "password2"));
        admins.add(new Admin(3L, "admin3", "password3"));
    }

    /**
     * Displays the admin selection menu.
     *
     * @param scanner the input scanner
     */
    private static void selectAdmin(Scanner scanner) {
        try {   // 1-1
            System.out.println("Select Admin");
            System.out.println("1. Admin 1");
            System.out.println("2. Admin 2");
            System.out.println("3. Admin 3");
            System.out.print("Select an admin: ");
            int adminChoice = scanner.nextInt();

            switch (adminChoice) {
                case 1:
                case 2:
                case 3:
                    adminLogin(scanner, adminChoice);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    selectAdmin(scanner);
                    break;
            }
        } catch (CustomException e) {
            System.out.println("An error occurred: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Handles the admin login process.
     *
     * @param scanner     the input scanner
     * @param adminChoice the selected admin
     */
    private static void adminLogin(Scanner scanner, int adminChoice) { // 1-2
        try {
            System.out.println("Admin Login");
            System.out.print("Username: ");
            String username = scanner.next();
            System.out.print("Password: ");
            String password = scanner.next();

            Admin selectedAdmin = admins.get(adminChoice - 1);
            if (authenticateAdmin(username, password, selectedAdmin)) {
                System.out.println("Welcome Admin!"); // 1-3: Welcome message
                adminMenu(scanner); // Display admin menu
            } else {
                System.out.println("Invalid username or password. Please try again.");
                adminLogin(scanner, adminChoice);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Authenticates the admin based on username and password.
     *
     * @param username      the username
     * @param password      the password
     * @param selectedAdmin the selected admin
     * @return true if authentication is successful, otherwise false
     */
    private static boolean authenticateAdmin(String username, String password, Admin selectedAdmin) { // 1-2
        try {
            return adminService.authenticate(username, password);
        } catch (CustomException e) {
            System.out.println("An error occurred during authentication: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            return false;
        } catch (Exception e) {
            // System.out.println("An unexpected error occurred during authentication: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays the admin menu and handles admin operations.
     *
     * @param scanner the input scanner
     */
    private static void adminMenu(Scanner scanner) { // 1-3
        while (true) {
            try {
                System.out.println("Admin Menu");
                System.out.println("1. Register Student ");
                System.out.println("2. View List of Students ");
                System.out.println("3. Register Teacher ");
                System.out.println("4. View List of Teachers ");
                System.out.println("5. Delete Teacher ");
                System.out.println("6. Create Course ");
                System.out.println("7. Edit Course ");
                System.out.println("8. Delete Course ");
                System.out.println("9. View List of Courses ");
                System.out.println("10. Assign Course to Teacher");
                System.out.println("11. Back to Main Menu");
                System.out.print("Please select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        registerStudent(scanner);
                        break;
                    case 2:
                        viewStudents();
                        break;
                    case 3:
                        registerTeacher(scanner);
                        break;
                    case 4:
                        viewTeachers();
                        break;
                    case 5:
                        deleteTeacher(scanner);
                        break;
                    case 6:
                        createCourse(scanner);
                        break;
                    case 7:
                        editCourse(scanner);
                        break;
                    case 8:
                        deleteCourse(scanner);
                        break;
                    case 9:
                        viewCourses(scanner);
                        break;
                    case 10:
                        assignCourseToTeacher(scanner);
                        break;
                    case 11:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (CustomException e) {
                System.out.println("An error occurred: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Registers a new student.
     *
     * @param scanner the input scanner
     */
    private static void registerStudent(Scanner scanner) {
        try {
            System.out.println("Register Student");
            System.out.print("First Name: ");
            String firstName = scanner.next();
            System.out.print("Last Name: ");
            String lastName = scanner.next();
            System.out.print("Student Number, (Student number must be 5 digits): ");
            String studentNumber = scanner.next();
            System.out.print("National Code, (National code must be 10 digits): ");
            String nationalCode = scanner.next();
            System.out.print("Mobile Number, (Mobile number must be 11 digits): ");
            String mobileNumber = scanner.next();
            System.out.print("Email Address: ");
            String emailAddress = scanner.next();

            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setUsername(studentNumber);
            student.setPassword(nationalCode);
            student.setMobileNumber(mobileNumber);
            student.setEmailAddress(emailAddress);
            student.setNationalCode(nationalCode);
            student.setStudentNumber(studentNumber);

            adminService.registerStudent(student);
            System.out.println("Student registered successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while registering the student: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            //  System.out.println("An unexpected error occurred while registering the student: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Displays the list of students.
     */
    private static void viewStudents() { // 1-4-2
        try {
            System.out.println("View Students");
            List<Student> students = adminService.viewStudents();
            for (Student student : students) {
                System.out.println(student);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing students: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing students: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Registers a new teacher.
     *
     * @param scanner the input scanner
     */
    private static void registerTeacher(Scanner scanner) {
        try { // 1-2-3
            System.out.println("Register Teacher");
            System.out.print("First Name: ");
            String firstName = scanner.next();
            System.out.print("Last Name: ");
            String lastName = scanner.next();
            System.out.print("Personnel Code, (Personnel code must be 5 digits): ");
            String personnelCode = scanner.next();
            System.out.print("National Code, (National code must be 10 digits): ");
            String nationalCode = scanner.next();
            System.out.print("Mobile Number, (Mobile number must be 11 digits): ");
            String mobileNumber = scanner.next();
            System.out.print("Email Address: ");
            String emailAddress = scanner.next();
            System.out.print("Specialty Field: ");
            String specialtyField = scanner.next();
            System.out.print("Degree (BACHELOR, MASTER, DOCTORATE): ");
            String degree = scanner.next();

            Teacher teacher = new Teacher();
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setUsername(personnelCode);
            teacher.setPassword(nationalCode);
            teacher.setMobileNumber(mobileNumber);
            teacher.setEmailAddress(emailAddress);
            teacher.setNationalCode(nationalCode);
            teacher.setSpecialtyField(specialtyField);
            teacher.setDegree(Teacher.DegreeEnum.valueOf(degree));
            teacher.setPersonnelCode(personnelCode);

            adminService.registerTeacher(teacher);  // Use adminService to register teacher
            System.out.println("Teacher registered successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while registering the teacher: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            // System.out.println("An unexpected error occurred while registering the teacher: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Displays the list of teachers.
     */
    private static void viewTeachers() {
        try {
            System.out.println("View Teachers");
            List<Teacher> teachers = adminService.viewTeachers(); // Use adminService to get teachers
            for (Teacher teacher : teachers) {
                System.out.println(teacher);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing teachers: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing teachers: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Deletes a teacher based on their ID.
     *
     * @param scanner the input scanner
     */
    private static void deleteTeacher(Scanner scanner) {
        try {
            System.out.println("Delete Teacher");
            System.out.print("Teacher ID: ");
            Long teacherId = scanner.nextLong();
            Teacher teacher = adminService.findTeacherById(teacherId);

            if (teacher == null) {
                System.out.println("Teacher not found.");
                return;
            }
            adminService.deleteTeacher(teacher);
            System.out.println("Teacher deleted successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while deleting the teacher: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while deleting the teacher: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Creates a new course.
     *
     * @param scanner the input scanner
     */
    private static void createCourse(Scanner scanner) {
        try {
            System.out.println("Create Course");

            System.out.print("Course Name: ");
            String courseName = scanner.next();
            System.out.print("Units: ");
            int units = scanner.nextInt();
            System.out.print("Capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            System.out.print("Teacher Name: ");
            String teacherName = scanner.nextLine();

            System.out.print("Start Date (YYYY-MM-DD): ");
            String startDate = scanner.next();
            LocalDate parsedDate = LocalDate.parse(startDate);

            Teacher teacher = adminService.findTeacherByName(teacherName);
            if (teacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }

            Course course = new Course();
            course.setCourseName(courseName);
            course.setUnits(units);
            course.setCapacity(capacity);
            course.setTeacher(teacher);
            course.setStartDate(parsedDate);

            adminService.createCourse(course);

            System.out.println("Course created successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while creating the course: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while creating the course: " + e.getMessage());
        }
    }

    /**
     * Edits an existing course based on the provided details.
     *
     * @param scanner the input scanner
     */
    private static void editCourse(Scanner scanner) {
        try {
            System.out.println("Edit Course");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = adminService.findCourseById(courseId);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            System.out.print("Course Name (" + course.getCourseName() + "): ");
            scanner.nextLine();
            String courseName = scanner.nextLine();
            if (!courseName.trim().isEmpty()) {
                course.setCourseName(courseName);
            }

            System.out.print("Units (" + course.getUnits() + "): ");
            String unitsInput = scanner.nextLine();
            if (!unitsInput.trim().isEmpty()) {
                int units = Integer.parseInt(unitsInput);
                course.setUnits(units);
            }

            System.out.print("Capacity (" + course.getCapacity() + "): ");
            String capacityInput = scanner.nextLine();
            if (!capacityInput.trim().isEmpty()) {
                int capacity = Integer.parseInt(capacityInput);
                course.setCapacity(capacity);
            }

            System.out.print("Teacher Name (" + course.getTeacherName() + "): ");
            String teacherName = scanner.nextLine();
            if (!teacherName.trim().isEmpty()) {
                course.setTeacherName(teacherName);
            }

            System.out.print("Start Date (" + course.getStartDate() + "): ");
            String startDate = scanner.nextLine();
            if (!startDate.trim().isEmpty()) {
                course.setStartDate(LocalDate.parse(startDate));
            }

            adminService.updateCourse(course);
            System.out.println("Course updated successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while updating the course: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while updating the course: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Deletes a course based on its ID.
     *
     * @param scanner the input scanner
     */
    private static void deleteCourse(Scanner scanner) {
        try {
            System.out.println("Delete Course");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = adminService.findCourseById(courseId);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            adminService.deleteCourse(course);
            System.out.println("Course deleted successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while deleting the course: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while deleting the course: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Displays the list of Course
     */
    private static void viewCourses(Scanner scanner) {
        try {
            List<Course> courses = courseService.findAllCourses();

            if (courses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                System.out.println("List of Courses:");
                for (Course course : courses) {
                    System.out.println("Course ID: " + course.getId());
                    System.out.println("Course Course Name: " + course.getCourseName());
                    System.out.println("Course Units: " + course.getUnits());
                    System.out.println("Course Capacity: " + course.getCapacity());
                    System.out.println("Course Teacher Name: " + course.getTeacherName());
                    System.out.println("-----");
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving courses: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        adminMenu(scanner);
    }

    /**
     * Assigns a course to a teacher.
     *
     * @param scanner the input scanner
     */
    private static void assignCourseToTeacher(Scanner scanner) {
        try {
            System.out.println("Assign Course to Teacher");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            System.out.print("Teacher ID: ");
            Long teacherId = scanner.nextLong();
            adminService.assignCourseToTeacher(courseId, teacherId);
            System.out.println("Course assigned to teacher successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while assigning the course to the teacher: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the teacher menu and handles teacher operations.
     *
     * @param scanner the input scanner
     */
    private static void teacherMenu(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Teacher Menu");
                System.out.println("1. View Assigned Courses ");
                System.out.println("2. View Enrolled Students ");
                System.out.println("3. Record Grades ");
                System.out.println("4. View Course Capacities ");
                System.out.println("5. View Course Start Dates ");
                System.out.println("6. View Teacher Profile");
                System.out.println("7. Change Password");
                System.out.println("8. View Grades");
                System.out.println("9. Back to Main Menu");
                System.out.print("Please select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewAssignedCourses(scanner);
                        break;
                    case 2:
                        viewEnrolledStudents(scanner);
                        break;
                    case 3:
                        recordGrades(scanner);
                        break;
                    case 4:
                        viewCourseCapacities(scanner);
                        break;
                    case 5:
                        viewCourseStartDates(scanner);
                        break;
                    case 6:
                        viewTeacherProfile(scanner);
                        break;
                    case 7:
                        changeTeacherPassword(scanner);
                        break;
                    case 8:
                        viewGrades(scanner);
                        break;
                    case 9:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (CustomException e) {
                System.out.println("An error occurred: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Displays the list of courses assigned to the teacher.
     *
     * @param scanner the input scanner
     */
    private static void viewAssignedCourses(Scanner scanner) {
        System.out.println("Assigned Courses");
        try {
            Long teacherId = getCurrentTeacherId();
            List<Course> courses = teacherService.getAssignedCourses(teacherId);

            if (courses.isEmpty()) {
                System.out.println("No assigned courses found.");
            } else {
                for (Course course : courses) {
                    System.out.println("Course ID: " + course.getId());
                    System.out.println("Course Name: " + course.getCourseName());
                    System.out.println("-----");
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving assigned courses: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the list of students enrolled in a specific course.
     *
     * @param scanner the input scanner
     */
    private static void viewEnrolledStudents(Scanner scanner) {
        try {
            System.out.println("View Enrolled Students");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = courseService.findCourseById(courseId);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            List<Enrollment> enrollments = enrollmentService.findAllEnrollments();
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourse().getId().equals(course.getId())) {
                    Student student = enrollment.getStudent();
                    System.out.println("Student(id=" + student.getId() + ", firstName=" + student.getFirstName() + ", lastName=" + student.getLastName() + ")");
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing the enrolled students: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing the enrolled students: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Records grades for students in a specific course.
     *
     * This method prompts the user to enter the course ID and the student IDs along with their grades.
     * It updates the grades of the students in the specified course in the database.
     * If an error occurs during the process, the transaction is rolled back.
     *
     * @param scanner the input scanner
     */
    private static void recordGrades(Scanner scanner) {
        System.out.println("Record Grades");
        Transaction transaction = null;
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            scanner.nextLine(); // Clear the buffer

            Map<Long, Double> grades = new HashMap<>();
            while (true) {
                System.out.print("Student ID (or 0 to finish): ");
                Long studentId = scanner.nextLong();
                if (studentId == 0) break;
                System.out.print("Grade: ");
                Double grade = scanner.nextDouble();
                grades.put(studentId, grade);
            }

            for (Map.Entry<Long, Double> entry : grades.entrySet()) {
                Long studentId = entry.getKey();
                Double grade = entry.getValue();

                Enrollment enrollment = session.createQuery("FROM Enrollment WHERE course.id = :courseId AND student.id = :studentId", Enrollment.class)
                        .setParameter("courseId", courseId)
                        .setParameter("studentId", studentId)
                        .uniqueResult();

                if (enrollment != null) {
                    enrollment.setGrade(grade);
                    session.update(enrollment);
                }
            }

            transaction.commit();
            System.out.println("Grades recorded successfully.");
        } catch (CustomException e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            System.out.println("An error occurred while recording grades: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the student menu and handles student operations.
     *
     * @param scanner the input scanner
     */
    private static void studentMenu(Scanner scanner) { // 2-1
        while (true) {
            try {
                System.out.println("Student Menu");
                System.out.println("1. View Available Courses");
                System.out.println("2. Enroll in a Course");
                System.out.println("3. View Grades");
                System.out.println("4. View Student Profile");
                System.out.println("5. Back to Main Menu");
                System.out.print("Please select an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        viewAvailableCourses(); // 2-5
                        break;
                    case 2:
                        enrollInCourse(scanner); // 2-5
                        break;
                    case 3:
                        studentGrades(scanner); // 2-4
                        break;
                    case 4:
                        viewStudentProfile(scanner); // 2-7
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (CustomException e) {
                System.out.println("An error occurred: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Views the available courses for students.
     * Only courses that have not yet started and have available capacity are displayed.
     * <p>
     * Steps:
     * 1. Retrieve all available courses using CourseService.
     * 2. Display the courses that meet the criteria.
     * 3. Handle and display any exceptions that occur during the process.
     */
    private static void viewAvailableCourses() { // 2-5
        try {
            System.out.println("View Available Courses");
            List<Course> availableCourses = courseService.findAvailableCourses();
            if (availableCourses.isEmpty()) {
                System.out.println("No available courses found.");
            } else {
                System.out.println("Available Courses:");
                for (Course course : availableCourses) {
                    System.out.println("Course ID: " + course.getId());
                    System.out.println("Course Name: " + course.getCourseName());
                    System.out.println("Units: " + course.getUnits());
                    System.out.println("Capacity: " + course.getCapacity());
                    System.out.println("Teacher: " + course.getTeacher().getLastName());
                    System.out.println("Start Date: " + course.getStartDate());
                    System.out.println("-----");
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving available courses: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Allows the current student to enroll in a course.
     *
     * @param scanner the input scanner
     */
    private static void enrollInCourse(Scanner scanner) { // 2-5
        try {
            System.out.println("Enroll in a Course");

            Long studentId = getCurrentStudentId();
            if (studentId == null) {
                System.out.println("No student is currently logged in.");
                return;
            }

            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = courseService.findCourseById(courseId);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(course);
            enrollment.setStudent(studentService.findStudentById(studentId));

            enrollmentService.saveEnrollment(enrollment);
            System.out.println("Enrolled in course successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while enrolling in the course: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while enrolling in the course: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Teacher login.
     *
     * @param scanner the input scanner
     */
    private static void teacherLogin(Scanner scanner) {
        try {
            System.out.println("Teacher Login");
            System.out.print("Username(Personal Number, Personnel code must be 5 digits): ");
            String username = scanner.next();
            System.out.print("Password(National Code, National code must be 10 digits): ");
            String password = scanner.next();
            boolean authenticated = teacherService.authenticate(username, password);
            if (authenticated) {
                Teacher teacher = teacherService.findByName(username);
                setCurrentTeacherId(teacher.getId());
                System.out.println("Welcome Teacher!");
                teacherMenu(scanner);
            } else {
                System.out.println("Invalid username or password. Please try again.");
                teacherLogin(scanner);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred during authentication: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during authentication: " + e.getMessage());
        }
    }

    /**
     * Displays the profile of the current teacher.
     *
     * @param scanner the input scanner
     */
    private static void viewTeacherProfile(Scanner scanner) {
        System.out.println("View Teacher Profile");
        try {
            Long teacherId = getCurrentTeacherId();
            if (teacherId == null) {
                System.out.println("No teacher is currently logged in.");
                return;
            }
            Teacher teacher = teacherService.findTeacherById(teacherId);
            System.out.println("Teacher Profile:");
            System.out.println("ID: " + teacher.getId());
            System.out.println("First Name: " + teacher.getFirstName());
            System.out.println("Last Name: " + teacher.getLastName());
            System.out.println("Username: " + teacher.getUsername());
            System.out.println("Mobile Number: " + teacher.getMobileNumber());
            System.out.println("Email Address: " + teacher.getEmailAddress());
            System.out.println("National Code: " + teacher.getNationalCode());
            System.out.println("Specialty Field: " + teacher.getSpecialtyField());
            System.out.println("Degree: " + teacher.getDegree());
            System.out.println("Personnel Code: " + teacher.getPersonnelCode());
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving the profile: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Allows the current teacher to change their password
     *
     * @param scanner the input scanner
     */
    private static void changeTeacherPassword(Scanner scanner) {
        System.out.println("Change Password");
        try {
            Long teacherId = getCurrentTeacherId();
            if (teacherId == null) {
                System.out.println("No teacher is currently logged in.");
                return;
            }
            System.out.print("Old Password: ");
            String oldPassword = scanner.next();
            Teacher teacher = teacherService.findTeacherById(teacherId);
            if (!teacher.getPassword().equals(oldPassword)) {
                System.out.println("Old password is incorrect.");
                return;
            }
            System.out.print("New Password(min = 8, max = 20): ");
            String newPassword = scanner.next();
            teacherService.changePassword(teacherId, newPassword);
            System.out.println("Password updated successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while changing the password: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the profile of the logged-in student.
     *
     * @param scanner the input scanner
     */
    private static void viewStudentProfile(Scanner scanner) {
        try {
            Long studentId = getCurrentStudentId();
            if (studentId == null) {
                System.out.println("No student is currently logged in.");
                return;
            }

            Student student = studentService.findStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            System.out.println("Profile Information:");
            System.out.println("First Name: " + student.getFirstName());
            System.out.println("Last Name: " + student.getLastName());
            System.out.println("Username: " + student.getUsername());
            System.out.println("Mobile Number: " + student.getMobileNumber());
            System.out.println("Email Address: " + student.getEmailAddress());
            System.out.println("National Code: " + student.getNationalCode());
            System.out.println("Student Number: " + student.getStudentNumber());

            System.out.print("Do you want to edit your password? (yes/no): ");
            String editPassword = scanner.next();

            if (editPassword.equalsIgnoreCase("yes")) {
                System.out.print("Old Password: ");
                String oldPassword = scanner.next();
                System.out.print("New Password(min = 8, max = 20): ");
                String newPassword = scanner.next();
                student.setPassword(newPassword);
                studentService.updateStudent(student);
                System.out.println("Password updated successfully.");
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing the profile: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing the profile: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Displays the capacities of the courses assigned to the current teacher.
     *
     * @param scanner the input scanner
     */
    private static void viewCourseCapacities(Scanner scanner) {
        System.out.println("Course Capacities");
        try {
            Long teacherId = getCurrentTeacherId();
            List<Course> courses = teacherService.getAssignedCourses(teacherId);

            if (courses.isEmpty()) {
                System.out.println("No assigned courses found.");
            } else {
                for (Course course : courses) {
                    System.out.println("Course ID: " + course.getId());
                    System.out.println("Course Name: " + course.getCourseName());
                    System.out.println("Capacity: " + course.getCapacity());
                    System.out.println("-----");
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving course capacities: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the start dates of the courses assigned to the current teacher.
     *
     * @param scanner the input scanner
     */
    private static void viewCourseStartDates(Scanner scanner) {
        System.out.println("Course Start Dates");
        try {
            Long teacherId = getCurrentTeacherId();
            List<Course> courses = teacherService.getAssignedCourses(teacherId);

            if (courses.isEmpty()) {
                System.out.println("No assigned courses found.");
            } else {
                for (Course course : courses) {
                    System.out.println("Course ID: " + course.getId());
                    System.out.println("Course Name: " + course.getCourseName());
                    System.out.println("Start Date: " + course.getStartDate());
                    System.out.println("-----");
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving course start dates: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the grades for students in the courses assigned to the current teacher.
     *
     * @param scanner the input scanner
     */
    private static void studentGrades(Scanner scanner) {
        System.out.println("Grades List");
        try {
            Long teacherId = getCurrentTeacherId();
            if (teacherId == null) {
                System.out.println("No teacher is currently logged in.");
                return;
            }

            List<Course> courses = teacherService.getAssignedCourses(teacherId);
            if (courses.isEmpty()) {
                System.out.println("No assigned courses found.");
                return;
            }

            for (Course course : courses) {
                System.out.println("Course: " + course.getCourseName());
                List<Enrollment> enrollments = enrollmentService.findEnrollmentsByCourseId(course.getId());
                if (enrollments.isEmpty()) {
                    System.out.println("No students enrolled.");
                } else {
                    for (Enrollment enrollment : enrollments) {
                        System.out.println("Student: " + enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName() + " - Grade: " + enrollment.getGrade());
                    }
                }
                System.out.println("-----");
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while retrieving grades: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Views the grades for the courses assigned to the current teacher
     * This method retrieves the teacher ID from the session and calls the viewGrades method
     * in the TeacherService to display the grades of students in the assigned courses
     *
     * @param scanner the input scanner
     */
    private static void viewGrades(Scanner scanner) {
        try {
            System.out.println("View Grades");

            Long teacherId = getCurrentTeacherId();
            if (teacherId == null) {
                System.out.println("No teacher is currently logged in.");
                return;
            }

            teacherService.viewGrades(teacherId);
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing the grades: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing the grades: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Handles the student login process.
     *
     * @param scanner the input scanner
     */
    private static void studentLogin(Scanner scanner) {
        try {
            System.out.println("Student Login");
            System.out.print("Username (Student Number, Student number must be 5 digits): ");
            String username = scanner.next();
            System.out.print("Password (National Code, National code must be 10 digits): ");
            String password = scanner.next();

            boolean authenticated = studentService.authenticate(username, password);
            if (authenticated) {
                Student student = studentService.findByName(username);
                currentStudentId = student.getId(); // تنظیم متغیر currentStudentId
                setCurrentStudentId(student.getId());
                System.out.println("Welcome Student!");
                studentMenu(scanner);
            } else {
                System.out.println("Invalid username or password. Please try again.");
                studentLogin(scanner);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred during authentication: "
                    + e.getMessage()
                    + " (Code: " + e.getErrorCode() + ")");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during authentication: " + e.getMessage());
        }
    }

    private static void setCurrentTeacherId(Long teacherId) {
        currentTeacherId = teacherId;
    }

    private static Long getCurrentTeacherId() {
        return currentTeacherId;
    }

    private static void setCurrentStudentId(Long studentId) {
        currentStudentId = studentId;
    }

    private static Long getCurrentStudentId() {
        return currentStudentId;
    }
}