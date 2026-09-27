package com.yogesh.employee_management_system.service.impl;

import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeRequest;
import com.yogesh.employee_management_system.dto.leavetype.LeaveTypeResponse;
import com.yogesh.employee_management_system.entity.LeaveType;
import com.yogesh.employee_management_system.exception.DuplicateResourceException;
import com.yogesh.employee_management_system.exception.ResourceNotFoundException;
import com.yogesh.employee_management_system.repository.LeaveTypeRepository;
import com.yogesh.employee_management_system.service.LeaveTypeService;
import com.yogesh.employee_management_system.util.mapper.LeaveTypeMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveTypeServiceImpl.class);

    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public LeaveTypeResponse createLeaveType(LeaveTypeRequest request) {

        logger.info("Creating leave type: {}", request.getName());

        if(leaveTypeRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            logger.warn("Leave type already exists: {}", request.getName());
            throw new DuplicateResourceException("Leave type already exists.");
        }
        LeaveType leaveType= LeaveTypeMapper.toEntity(request);
        LeaveType savedLeaveType = leaveTypeRepository.save(leaveType);

        logger.info("Leave type created successfully with ID: {}", savedLeaveType.getId());

        return LeaveTypeMapper.toResponse(savedLeaveType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveTypeResponse> getAllLeaveTypes(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        logger.info("Fetching leave types. Page: {}, Size: {}, SortBy: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return leaveTypeRepository.findByIsDeletedFalse(pageable)
                .map(LeaveTypeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveTypeResponse getLeaveTypeById(Long id) {
        logger.info("Fetching leave type with ID: {}", id);

        LeaveType leaveType = leaveTypeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave type not found."));

        return LeaveTypeMapper.toResponse(leaveType);
    }

    @Override
    @Transactional
    public LeaveTypeResponse updateLeaveType(
            Long id,
            LeaveTypeRequest request
    ) {
        logger.info("Updating leave type with ID: {}", id);

        LeaveType leaveType = leaveTypeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave type not found."));

        if (leaveTypeRepository.existsByNameAndIsDeletedFalseAndIdNot(
                request.getName(),
                id
        )) {
            throw new DuplicateResourceException(
                    "Leave type already exists."
            );
        }

        LeaveTypeMapper.updateEntity(leaveType, request);

        LeaveType updatedLeaveType = leaveTypeRepository.save(leaveType);

        logger.info("Leave type updated successfully with ID: {}", updatedLeaveType.getId());

        return LeaveTypeMapper.toResponse(updatedLeaveType);
    }

    @Override
    @Transactional
    public void deleteLeaveType(Long id) {

        logger.info("Deleting leave type with ID: {}", id);

        LeaveType leaveType = leaveTypeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Leave type not found."));
        leaveType.setIsDeleted(true);
        leaveTypeRepository.save(leaveType);

        logger.info("Leave type soft deleted successfully with ID: {}", leaveType.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveTypeResponse> searchLeaveTypes(
            String keyword,
            int page,
            int size
    ) {
        logger.info("Searching leave types. Keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Pageable pageable = PageRequest.of(page, size);

        return leaveTypeRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(
                keyword,
                pageable
        )
                .map(LeaveTypeMapper::toResponse);
    }

}
