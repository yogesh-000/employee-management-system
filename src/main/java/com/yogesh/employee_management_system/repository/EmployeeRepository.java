package com.yogesh.employee_management_system.repository;

import com.yogesh.employee_management_system.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByIdAndIsDeletedFalse(Long id);

    boolean existsByEmailAndIsDeletedFalse(String email);

    List<Employee> findByIsDeletedFalse();

    Page<Employee> findByIsDeletedFalse(Pageable pageable);

    Page<Employee> findByFirstNameContainingIgnoreCaseAndIsDeletedFalse(
            String keyword,
            Pageable pageable
    );

    boolean existsByEmailAndIsDeletedFalseAndIdNot(
            String email,
            Long id
    );

    boolean existsByPhoneAndIsDeletedFalseAndIdNot(
            String phone,
            Long id
    );
}
