package com.yogesh.employee_management_system.repository;

import com.yogesh.employee_management_system.entity.Department;
import com.yogesh.employee_management_system.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByIdAndIsDeletedFalse(Long id);

    boolean existsByNameAndIsDeletedFalse(String name);

    List<Department> findByIsDeletedFalse();

    Page<Department> findByIsDeletedFalse(Pageable pageable);

    Page<Department> findByNameContainingIgnoreCaseAndIsDeletedFalse(
            String keyword,
            Pageable pageable
    );

    boolean existsByNameAndIsDeletedFalseAndIdNot(
            String name,
            Long id
    );
}
