package org.example.repository.impl;

import org.example.entity.Admin;
import org.example.enums.ErrorCode;
import org.example.exception.CustomException;
import org.example.repository.AdminRepository;
import org.example.util.SessionFactoryInstance;
import org.hibernate.Session;

import java.util.List;

/**
 * Implementation of the AdminRepository interface.
 */
public class AdminRepositoryImpl implements AdminRepository {

    /**
     * Saves an admin entity to the database.
     *
     * @param admin the admin entity to save
     * @throws CustomException if there is an error while saving the admin
     */
    @Override
    public void save(Admin admin) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.persist(admin);
        } catch (Exception e) {
            throw new CustomException("Failed to save admin", ErrorCode.REGISTER_TEACHER_FAILED.getCode());
        }
    }

    /**
     * Finds an admin entity by its ID.
     *
     * @param id the ID of the admin entity
     * @return the admin entity, or null if not found
     * @throws CustomException if there is an error while finding the admin
     */
    @Override
    public Admin findById(Long id) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.get(Admin.class, id);
        } catch (Exception e) {
            throw new CustomException("Failed to find admin by ID", ErrorCode.FIND_TEACHER_BY_ID_FAILED.getCode());
        }
    }

    /**
     * Finds all admin entities.
     *
     * @return a list of all admin entities
     * @throws CustomException if there is an error while finding the admins
     */
    @Override
    public List<Admin> findAll() throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            return session.createQuery("FROM Admin", Admin.class).list();
        } catch (Exception e) {
            throw new CustomException("Failed to find all admins", ErrorCode.VIEW_TEACHERS_FAILED.getCode());
        }
    }

    /**
     * Updates an admin entity in the database.
     *
     * @param admin the admin entity to update
     * @throws CustomException if there is an error while updating the admin
     */
    @Override
    public void update(Admin admin) throws CustomException {
        try (Session session = SessionFactoryInstance.sessionFactory.openSession()) {
            session.merge(admin);
        } catch (Exception e) {
            throw new CustomException("Failed to update admin", ErrorCode.UPDATE_COURSE_FAILED.getCode());
        }
    }

    /**
     * Deletes an admin entity by its ID.
     *
     * @param session the session object
     * @param id      the ID of the admin entity to delete
     * @return the number of entities deleted
     * @throws CustomException if there is an error while deleting the admin
     */
    @Override
    public int deleteById(Session session, Long id) throws CustomException {
        try {
            return session.createMutationQuery(
                            "DELETE FROM Admin a WHERE a.id = :id"
                    )
                    .setParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            throw new CustomException("Failed to delete admin by ID", ErrorCode.DELETE_TEACHER_FAILED.getCode());
        }
    }
}
