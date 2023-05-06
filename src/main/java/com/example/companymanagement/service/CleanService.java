package com.example.companymanagement.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class CleanService {
    @PersistenceContext
    public EntityManager entityManager;

    public void deleteConnections(String table, Integer id){
        entityManager
                .createNativeQuery("DELETE FROM " + table + " WHERE project_id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
