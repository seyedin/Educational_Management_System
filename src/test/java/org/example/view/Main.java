package org.example.view;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.example.entity.*;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.service.*;
import org.example.service.impl.*;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();
        try {
            configurator.doConfigure("src/main/resources/logback.xml");
        } catch (JoranException je) {
            je.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

    private static final AdminService adminService = new AdminServiceImpl();
    private static final StudentService studentService = new StudentServiceImpl();
    private static final TeacherService teacherService = new TeacherServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private static final List<Admin> admins = new ArrayList<>();

    /**
     * The entry point of the application. It initializes admins and displays the main menu.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        initializeAdmins(); // Initialize predefined admins
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Welcome to the Educational Management System");
                System.out.println("1. Admin َ (1-1)");
                System.out.println("2. Teacher (3-1)");
                System.out.println("3. Student (2-1)");
                System.out.println("4. Exit");
                System.out.print("Please select your role: ");
                int role = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (role) {
                    case 1:
                        selectAdmin(scanner); // 1-1: Admin selection menu
                        break;
                    case 2:
                        teacherMenu(scanner); // 3-1: Teacher menu
                        break;
                    case 3:
                        studentMenu(scanner); // 2-1: Student menu
                        break;
                    case 4:
                        System.out.println("Exiting the system. Goodbye!");
                        System.exit(0);
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
                    adminLogin(scanner, adminChoice); // 1-2: Admin login
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
                System.out.println("1. Register Student (1-4-1)");
                System.out.println("2. View List of Students (1-4-2)");
                System.out.println("3. Register Teacher (1-2-3)");
                System.out.println("4. View List of Teachers (1-2-1)");
                System.out.println("5. Delete Teacher (1-2-2)");
                System.out.println("6. Create Course (1-3-3)");
                System.out.println("7. Edit Course (1-3-1)");
                System.out.println("8. Delete Course (1-3-2)");
                System.out.println("9. View List of Courses (1-3)");
                System.out.println("10. Back to Main Menu");
                System.out.print("Please select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1:
                        registerStudent(scanner); // 1-4-1: Register student
                        break;
                    case 2:
                        viewStudents(); // 1-4-2: View students
                        break;
                    case 3:
                        registerTeacher(scanner); // 1-2-3: Register teacher
                        break;
                    case 4:
                        viewTeachers(); // 1-2-1: View teachers
                        break;
                    case 5:
                        deleteTeacher(scanner); // 1-2-2: Delete teacher
                        break;
                    case 6:
                        createCourse(scanner); // 1-3-3: Create course
                        break;
                    case 7:
                        editCourse(scanner); // 1-3-1: Edit course
                        break;
                    case 8:
                        deleteCourse(scanner); // 1-3-2: Delete course
                        break;
                    case 9:
                        viewCourses(); // 1-3: View list of courses                        break;
                    case 10:
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
    private static void registerStudent(Scanner scanner) { // 1-4-1
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
            //     System.out.println("An unexpected error occurred while viewing students: " + e.getMessage());
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
        try {// 1-2-1
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
        try {// 1-2-2
            System.out.println("Delete Teacher");
            System.out.print("Teacher ID: ");
            Long teacherId = scanner.nextLong();
            Teacher teacher = adminService.findTeacherById(teacherId); // Use adminService to find teacher

            if (teacher == null) {
                System.out.println("Teacher not found.");
                return;
            }
            adminService.deleteTeacher(teacher);  // Use adminService to delete teacher
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
            String teacherName = scanner.nextLine(); // گرفتن نام معلم با هر دو نام کوچک و نام خانوادگی

            System.out.print("Start Date (YYYY-MM-DD): ");
            String startDate = scanner.next();
            startDate = startDate.trim(); // حذف فضای اضافی احتمالی

            // بررسی و تبدیل تاریخ
            LocalDate parsedDate;
            try {
                parsedDate = LocalDate.parse(startDate);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                return; // خروج از متد در صورت وارد کردن تاریخ نامعتبر
            }

            Teacher teacher = adminService.findTeacherByName(teacherName);
            if (teacher == null) {
                throw new CustomException("Teacher not found", ErrorCode.TEACHER_NOT_FOUND.getCode());
            }

            System.out.println("Teacher found: " + teacher.getFirstName() + " " + teacher.getLastName());

            Course course = new Course();
            course.setCourseName(courseName);
            course.setUnits(units);
            course.setCapacity(capacity);
            course.setTeacher(teacher); // تنظیم teacher به جای teacherName
            course.setStartDate(parsedDate); // تنظیم تاریخ پردازش شده

            adminService.createCourse(course);

            System.out.println("Course created successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while creating the course: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            e.printStackTrace(); // نمایش جزئیات کامل خطا
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while creating the course: " + e.getMessage());
            e.printStackTrace(); // اضافه کردن e.printStackTrace برای عیب‌یابی دقیق‌تر
            System.out.println("Please try again.");
        }
    }

    /**
     * Edits an existing course based on the provided details.
     *
     * @param scanner the input scanner
     */
    private static void editCourse(Scanner scanner) { // 1-3-1
        try {
            System.out.println("Edit Course");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = adminService.findCourseById(courseId); // Use adminService to find course

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            System.out.print("Course Name (" + course.getCourseName() + "): ");
            scanner.nextLine();  // Consume the leftover \n from nextLong
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
                course.setStartDate(java.time.LocalDate.parse(startDate));
            }

            adminService.updateCourse(course); // Use adminService to update course
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
    private static void deleteCourse(Scanner scanner) { // 1-3-2
        try {
            System.out.println("Delete Course");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = adminService.findCourseById(courseId); // Use adminService to find course

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            adminService.deleteCourse(course); // Use adminService to delete course
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
    private static void viewCourses() {
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
    }

    /**
     * Displays the teacher menu and handles teacher operations.
     *
     * @param scanner the input scanner
     */
    private static void teacherMenu(Scanner scanner) { // 3-1
        while (true) {
            try {
                System.out.println("Teacher Menu");
                System.out.println("1. View Assigned Courses (3-8)");
                System.out.println("2. View Enrolled Students (3-7)");
                System.out.println("3. Record Grades (3-4)");
                System.out.println("4. Back to Main Menu");
                System.out.print("Please select an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        viewAssignedCourses(scanner); // 3-8
                        break;
                    case 2:
                        viewEnrolledStudents(scanner); // 3-7
                        break;
                    case 3:
                        recordGrades(scanner); // 3-4
                        break;
                    case 4:
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
     * Displays the list of courses assigned to a teacher.
     *
     * @param scanner the input scanner
     */
    private static void viewAssignedCourses(Scanner scanner) { // 3-8
        try {
            System.out.println("View Assigned Courses");
            System.out.print("Teacher ID: ");
            Long teacherId = scanner.nextLong();
            Teacher teacher = teacherService.findTeacherById(teacherId);

            if (teacher == null) {
                System.out.println("Teacher not found.");
                return;
            }

            List<Course> courses = courseService.findAllCourses();
            for (Course course : courses) {
                if (course.getTeacherName().equals(teacher.getFirstName() + " " + teacher.getLastName())) {
                    System.out.println(course);
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing the assigned courses: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing the assigned courses: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Displays the list of students enrolled in a specific course.
     *
     * @param scanner the input scanner
     */
    private static void viewEnrolledStudents(Scanner scanner) { // 3-7
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
                    System.out.println(enrollment.getStudent());
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
     * @param scanner the input scanner
     */
    private static void recordGrades(Scanner scanner) { // 3-4
        try {
            System.out.println("Record Grades");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = courseService.findCourseById(courseId);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            System.out.print("Student ID: ");
            Long studentId = scanner.nextLong();
            Student student = studentService.findStudentById(studentId);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            System.out.print("Grade: ");
            Double grade = scanner.nextDouble();

            Enrollment enrollment = enrollmentService.findEnrollmentByStudentAndCourse(student, course);

            if (enrollment == null) {
                System.out.println("Enrollment not found.");
                return;
            }

            enrollment.setGrade(grade);
            enrollmentService.updateEnrollment(enrollment);
            System.out.println("Grade recorded successfully.");
        } catch (CustomException e) {
            System.out.println("An error occurred while recording the grades: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while recording the grades: " + e.getMessage());
            System.out.println("Please try again.");
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
                System.out.println("1. View Available Courses (2-5)");
                System.out.println("2. Enroll in a Course (2-5)");
                System.out.println("3. View Grades (2-4)");
                System.out.println("4. View Profile (2-7)");
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
                        viewGrades(scanner); // 2-4
                        break;
                    case 4:
                        viewProfile(scanner); // 2-7
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
     * Displays the available courses for students to enroll in.
     */
    private static void viewAvailableCourses() { // 2-5
        try {
            System.out.println("View Available Courses");
            List<Course> courses = courseService.findAllCourses();
            for (Course course : courses) {
                if (course.getStartDate().isAfter(java.time.LocalDate.now()) && course.getCapacity() > 0) {
                    System.out.println(course);
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing the available courses: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing the available courses: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Allows students to enroll in a course.
     *
     * @param scanner the input scanner
     */
    private static void enrollInCourse(Scanner scanner) { // 2-5
        try {
            System.out.println("Enroll in a Course");
            System.out.print("Course ID: ");
            Long courseId = scanner.nextLong();
            Course course = courseService.findCourseById(courseId);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            System.out.print("Student ID: ");
            Long studentId = scanner.nextLong();
            Student student = studentService.findStudentById(studentId);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(course);
            enrollment.setStudent(student);

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
     * Displays the grades for the logged-in student.
     *
     * @param scanner the input scanner
     */
    private static void viewGrades(Scanner scanner) { // 2-4
        try {
            System.out.println("View Grades");
            System.out.print("Student ID: ");
            Long studentId = scanner.nextLong();
            Student student = studentService.findStudentById(studentId);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            List<Enrollment> enrollments = enrollmentService.findAllEnrollments();
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudent().getId().equals(student.getId())) {
                    System.out.println(enrollment.getCourse() + ": " + enrollment.getGrade());
                }
            }
        } catch (CustomException e) {
            System.out.println("An error occurred while viewing the grades: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")");
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing the grades: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Displays the profile of the logged-in student.
     *
     * @param scanner the input scanner
     */
    private static void viewProfile(Scanner scanner) { // 2-7
        try {
            System.out.println("View Profile");
            System.out.print("Student ID: ");
            Long studentId = scanner.nextLong();
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
                System.out.print("New Password: ");
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
}