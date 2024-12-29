package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @NotNull
    @Column(name = "units", nullable = false)
    private Integer units;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "teacher_name", nullable = false, length = 100)
    private String teacherName;

    @NotNull
    @PastOrPresent(message = "Start date must be in the past or present")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude // جلوگیری از تکرار بی‌نهایت
    private Teacher teacher; // Foreign Key

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments; // List of enrollments for the course
}

