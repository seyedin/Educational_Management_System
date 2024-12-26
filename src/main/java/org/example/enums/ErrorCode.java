package org.example.enums;

/**
 * Enumeration of error codes used in the application.
 */
public enum ErrorCode {
    // Auth Errors
    LOGIN_USER_FAILED_DB(300),
    LOGIN_USER_FAILED_USER_NOT_FOUND(301),

    // Student Errors
    REGISTER_STUDENT_FAILED(302),
    VIEW_STUDENTS_FAILED(303),
    FIND_STUDENT_BY_ID_FAILED(304),
    UPDATE_STUDENT_FAILED(305),
    DELETE_STUDENT_FAILED(306),
    FIND_STUDENTS_FAILED_BY_COURSE_ID(3134),
    LOGIN_USER_FAILED(301),

    // Teacher Errors
    REGISTER_TEACHER_FAILED(307),
    VIEW_TEACHERS_FAILED(308),
    FIND_TEACHER_BY_ID_FAILED(309),
    UPDATE_TEACHER_FAILED(310),
    DELETE_TEACHER_FAILED(311),
    FIND_TEACHER_BY_NAME_FAILED(312),
    INVALID_TEACHER_NAME_FORMAT(313),
    TEACHER_NOT_FOUND(314),
    RETRIEVE_PROFILE_FAILED(3115),
    RETRIEVE_COURSES_FAILED(3116),

    // Course Errors
    CREATE_COURSE_FAILED(312),
    VIEW_COURSES_FAILED(313),
    RETRIEVE_ASSIGNED_COURSES_FAILED(3133),
    FIND_COURSE_BY_ID_FAILED(314),
    UPDATE_COURSE_FAILED(315),
    DELETE_COURSE_FAILED(316),
    COURSE_ALREADY_EXISTS(3122),
    FIND_COURSES_FAILED_BY_TEACHER_ID(3132),
    ASSIGN_COURSE_FAILED(3131),
    COURSE_NOT_FOUND(3121),


    // Enrollment Errors
    CREATE_ENROLLMENT_FAILED(317),
    VIEW_ENROLLMENTS_FAILED(318),
    FIND_ENROLLMENT_BY_ID_FAILED(319),
    UPDATE_ENROLLMENT_FAILED(320),
    DELETE_ENROLLMENT_FAILED(321),
    RECORD_GRADES_FAILED(322),
    RETRIEVE_STUDENTS_FAILED(3134),
    RETRIEVE_ENROLLMENTS_FAILED(3135),
    SAVE_ENROLLMENT_FAILED(323);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
