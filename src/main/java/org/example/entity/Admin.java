package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @NotNull
    @Size(min = 8, max = 20)
    @Column(name = "password", nullable = false, length = 20)
    private String password;

}
/*
* توضیح روابط بین (Entities)
انتیتی Admin:
این انتیتی رابطه مستقیمی با سایر انتیتی‌ها ندارد و به عنوان کاربر ادمین عمل می‌کند.
انتیتی Course:
Many-to-One با Teacher: هر درس می‌تواند فقط یک معلم داشته باشد، اما هر معلم می‌تواند چندین درس تدریس کند.
One-to-Many با Enrollment: هر درس می‌تواند چندین ثبت‌نام داشته باشد.
انتیتی Enrollment:
Many-to-One با Student: هر ثبت‌نام مربوط به یک دانشجو است، اما هر دانشجو می‌تواند چندین درس ثبت‌نام کند.
Many-to-One با Course: هر ثبت‌نام مربوط به یک درس است، اما هر درس می‌تواند چندین ثبت‌نام داشته باشد.
انتیتی Student:
One-to-Many با Enrollment: هر دانشجو می‌تواند چندین ثبت‌نام داشته باشد.
انتیتی Teacher:
One-to-Many با Course: هر معلم می‌تواند چندین درس تدریس کند.
*/