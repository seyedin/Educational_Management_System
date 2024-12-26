package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // Foreign Key

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Foreign Key

    @Column(name = "grade")
    private Double grade;

}

