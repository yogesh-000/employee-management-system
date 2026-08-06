package com.yogesh.employee_management_system.repository;

import com.yogesh.employee_management_system.entity.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    Optional<LeaveType> findByIdAndIsDeletedFalse(Long id);

    boolean existsByNameAndIsDeletedFalse(String name);

    List<LeaveType> findByIsDeletedFalse();

    Page<LeaveType> findByIsDeletedFalse(Pageable pageable);

    Page<LeaveType> findByNameContainingIgnoreCaseAndIsDeletedFalse(
            String keyword,
            Pageable pageable
    );

    boolean existsByNameAndIsDeletedFalseAndIdNot(
            String name,
            Long id
    );
}
