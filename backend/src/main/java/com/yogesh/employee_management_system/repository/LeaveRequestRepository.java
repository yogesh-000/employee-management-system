package com.yogesh.employee_management_system.repository;

import com.yogesh.employee_management_system.entity.Employee;
import com.yogesh.employee_management_system.entity.LeaveRequest;
import com.yogesh.employee_management_system.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

    Page<LeaveRequest> findByStatusAndEmployee_Manager_Id(LeaveStatus status, Long managerId, Pageable pageable);

    Optional<LeaveRequest> findById(Long id);

    Page<LeaveRequest> findAll(Pageable pageable);

    List<LeaveRequest> findByEmployee(Employee employee);

    Page<LeaveRequest> findByEmployee(Employee employee, Pageable pageable);

    Page<LeaveRequest> findByEmployee_FirstNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    List<LeaveRequest> findByEmployeeAndStatus(
            Employee employee,
            LeaveStatus status
    );

}
