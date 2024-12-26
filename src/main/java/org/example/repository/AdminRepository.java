package org.example.repository;

import org.example.entity.Admin;
import org.hibernate.Session;

import java.util.List;

public interface AdminRepository {
    void save(Admin admin);

    void
    update(Admin admin);

    Admin
    findById(Long id);

    List<Admin> findAll();

    int deleteById(Session session, Long id);
}
