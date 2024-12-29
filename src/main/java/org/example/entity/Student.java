package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @NotNull
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username; // Default: Student number

    @NotNull
    @Size(min = 8, max = 20)
    @Column(name = "password", nullable = false, unique = true, length = 20)
    private String password; // Default: National code

    @NotNull
    @Pattern(regexp = "\\d{11}", message = "Mobile number must be 11 digits")
    @Column(name = "mobile_number", nullable = false, unique = true, length = 11)
    private String mobileNumber;

    @NotNull
    @Email(message = "Email address must be valid")
    @Column(name = "email_address", nullable = false, unique = true, length = 50)
    private String emailAddress;

    @NotNull
    @Pattern(regexp = "\\d{10}", message = "National code must be 10 digits")
    @Column(name = "national_code", nullable = false, unique = true, length = 10)
    private String nationalCode;

    @NotNull
    @Pattern(regexp = "\\d{5}", message = "Student number must be 5 digits")
    @Column(name = "student_number", nullable = false, unique = true, length = 5)
    private String studentNumber;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Enrollment> enrollments;
}