package com.yogesh.employee_management_system.service.impl;

import com.yogesh.employee_management_system.dto.leaverequest.ApplyLeaveRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveApprovalRequest;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveHistoryResponse;
import com.yogesh.employee_management_system.dto.leaverequest.LeaveRequestResponse;
import com.yogesh.employee_management_system.entity.Employee;
import com.yogesh.employee_management_system.entity.LeaveRequest;
import com.yogesh.employee_management_system.entity.LeaveType;
import com.yogesh.employee_management_system.enums.LeaveStatus;
import com.yogesh.employee_management_system.exception.BusinessValidationException;
import com.yogesh.employee_management_system.exception.ResourceNotFoundException;
import com.yogesh.employee_management_system.repository.EmployeeRepository;
import com.yogesh.employee_management_system.repository.LeaveRequestRepository;
import com.yogesh.employee_management_system.repository.LeaveTypeRepository;
import com.yogesh.employee_management_system.service.LeaveRequestService;
import com.yogesh.employee_management_system.util.mapper.LeaveRequestMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    private final LeaveRequestRepository leaveRequestRepository;

    private final EmployeeRepository employeeRepository;

    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public LeaveRequestResponse applyLeave(ApplyLeaveRequest request) {

        logger.info("Employee {} is applying for leave.", request.getEmployeeId());

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(request.getEmployeeId())
                .orElseThrow(()-> new
                        ResourceNotFoundException("Employee not found."));

        LeaveType leaveType = leaveTypeRepository.findByIdAndIsDeletedFalse(request.getLeaveTypeId())
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave type not found."));

        if(request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessValidationException("End date cannot be before start date.");
        }

        List<LeaveRequest> existingLeaves = leaveRequestRepository.findByEmployee(
                employee
        );

        for(LeaveRequest existingLeave: existingLeaves) {

            if(existingLeave.getStatus() == LeaveStatus.REJECTED
            ||
            existingLeave.getStatus() == LeaveStatus.CANCELLED) {
                continue;
            }

            boolean isOverlapping =
                    !
                            request.getEndDate().isBefore(existingLeave.getStartDate())
                    &&
                            !
                                    request.getStartDate().isAfter(existingLeave.getEndDate());

            if(isOverlapping) {
                throw new BusinessValidationException(
                        "Leave request overlaps with an existing pending or approved leave."
                );
            }
        }

        long leaveDays = ChronoUnit.DAYS.between(
                request.getStartDate(),
                request.getEndDate()
        ) + 1;

        if(leaveDays > leaveType.getMaxDays()) {
            throw new BusinessValidationException(
                    "Requested leave exceeds the maximum allowed "
                    + leaveType.getMaxDays()
                    + " day(s) for "
                    +leaveType.getName()
                    +"."
            );
        }

        LeaveRequest leaveRequest = LeaveRequestMapper.toEntity(
                request,
                employee,
                leaveType
        );

        leaveRequest.setStatus(LeaveStatus.PENDING);

        leaveRequest.setAppliedAt(LocalDateTime.now());

        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        logger.info("Leave request created successfully with ID: {}", savedLeaveRequest.getId());

        return LeaveRequestMapper.toResponse(savedLeaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> getAllLeaveRequests(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        logger.info("Fetching all leave requests. Page: {}, Size: {}", page, size);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return leaveRequestRepository
                .findAll(pageable)
                .map(LeaveRequestMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestResponse getLeaveRequestById(Long id) {

        logger.info("Fetching leave request with ID: {}", id);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave request not found."));

        logger.info("Leave request found with ID: {}", id);

        return LeaveRequestMapper.toResponse(leaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveHistoryResponse getEmployeeLeaveHistory(Long employeeId) {

        logger.info("Fetching leave history for employee ID: {}", employeeId);

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(employeeId)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Employee not found."));

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployee(employee);

        List<LeaveRequestResponse> leaveRequestResponses = leaveRequests.stream()
                .map(LeaveRequestMapper::toResponse)
                .toList();

        logger.info("Found {} leave request(s) for employee ID: {}",
                leaveRequestResponses.size(),
                employeeId
        );

        return LeaveHistoryResponse.builder()
                .employeeId(employee.getId())
                .employeeName(
                        employee.getFirstName()+ " "+
                                employee.getLastName()
                )
                .leaveRequests(leaveRequestResponses)
                .build();
    }

    @Override
    @Transactional
    public LeaveRequestResponse approveOrRejectLeave(
            Long leaveRequestId,
            LeaveApprovalRequest request
    ) {
        logger.info("Processing leave request ID: {} with status: {}",
                leaveRequestId,
                request.getStatus()
        );

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave request not found."));

        Employee approver = employeeRepository.findByIdAndIsDeletedFalse(request.getApprovedBy())
                .orElseThrow(()-> new
                        ResourceNotFoundException("Approver not found."));

        if(leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessValidationException(
                    "Only pending leave requests can be approved or rejected."
            );
        }

        if(request.getStatus() != LeaveStatus.APPROVED &&
        request.getStatus() != LeaveStatus.REJECTED) {
            throw new BusinessValidationException(
                    "Status must be APPROVED or REJECTED."
            );
        }

        leaveRequest.setStatus(request.getStatus());
        leaveRequest.setApprovedBy(approver);
        leaveRequest.setRemarks(request.getRemarks());

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        logger.info(
                "Leave request {} updated successfully to {}",
                leaveRequestId,
                updatedLeaveRequest.getStatus()
        );

        return LeaveRequestMapper.toResponse(updatedLeaveRequest);
    }

    @Override
    @Transactional
    public LeaveRequestResponse cancelLeave(Long leaveRequestId) {

        logger.info("Cancelling leave request with ID: {}", leaveRequestId);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave request not found."));

        if(leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new
                    BusinessValidationException("Only pending leave requests can be cancelled.");
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);

        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        logger.info(
                "Leave request {} cancelled successfully.", leaveRequestId
        );

        return LeaveRequestMapper.toResponse(updatedLeaveRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveRequestResponse> searchLeaveRequests(
            String keyword,
            int page,
            int size
    ) {
        logger.info(
                "Searching leave requests. Keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Pageable pageable = PageRequest.of(page, size);

        return leaveRequestRepository.findByEmployee_FirstNameContainingIgnoreCase(
                keyword,
                pageable
        )
                .map(LeaveRequestMapper::toResponse);
    }

}
