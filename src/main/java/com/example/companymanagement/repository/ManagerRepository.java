package com.example.companymanagement.repository;

import com.example.companymanagement.domain.Manager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Integer> {
}
