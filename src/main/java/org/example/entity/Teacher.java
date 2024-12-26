package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

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
    private String username; // Default: personnel code

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
    @Size(min = 1, max = 50)
    @Column(name = "specialty_field", nullable = false, length = 50)
    private String specialtyField;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "degree", nullable = false)
    private DegreeEnum degree;

    @NotNull
    @Pattern(regexp = "\\d{5}", message = "Personnel code must be 5 digits")
    @Column(name = "personnel_code", nullable = false, unique = true, length = 5)
    private String personnelCode;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses; // List of courses taught by the teacher

    public enum DegreeEnum {BACHELOR, MASTER, DOCTORATE}
}
