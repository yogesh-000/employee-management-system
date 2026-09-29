package com.yogesh.employee_management_system.service.impl;

import com.yogesh.employee_management_system.dto.department.DepartmentRequest;
import com.yogesh.employee_management_system.dto.department.DepartmentResponse;
import com.yogesh.employee_management_system.entity.Department;
import com.yogesh.employee_management_system.exception.DuplicateResourceException;
import com.yogesh.employee_management_system.exception.ResourceNotFoundException;
import com.yogesh.employee_management_system.repository.DepartmentRepository;
import com.yogesh.employee_management_system.service.DepartmentService;
import com.yogesh.employee_management_system.util.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yogesh.employee_management_system.exception.BusinessValidationException;
import com.yogesh.employee_management_system.repository.EmployeeRepository;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        logger.info("Creating department: {}", request.getName());

        if(departmentRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            logger.warn("Department already exists: {}", request.getName());
            throw new DuplicateResourceException("Department already exists.");
        }
        Department department= new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department savedDepartment= departmentRepository.save(department);

        logger.info("Department created successfully with ID: {}", savedDepartment.getId());

        return DepartmentMapper.toResponse(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        logger.info("Fetching departments. Page: {}, Size: {}, SortBy: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return departmentRepository.findByIsDeletedFalse(pageable)
                .map(DepartmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> searchDepartments(
            String keyword,
            int page,
            int size
    ) {

        logger.info("Searching departments with keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Pageable pageable = PageRequest.of(page, size);
        return departmentRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(
                        keyword,
                        pageable
                )
                .map(DepartmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        logger.info("Fetching department with ID: {}", id);
        Department department= departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        return DepartmentMapper.toResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        logger.info("Updating department with ID: {}", id);
        Department department= departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        if (departmentRepository.existsByNameAndIsDeletedFalseAndIdNot(
                request.getName(),
                id
        )) {
            throw new DuplicateResourceException(
                    "Department already exists."
            );
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department updatedDepartment= departmentRepository.save(department);

        logger.info("Department updated successfully with ID: {}", updatedDepartment.getId());

        return DepartmentMapper.toResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {

        logger.info("Deleting department with ID: {}", id);

        Department department= departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        long activeCount= employeeRepository.countByDepartmentIdAndIsDeletedFalse(id);
        if(activeCount > 0){
            throw new BusinessValidationException(
                    "Cannot delete department with " + activeCount + " active employee(s). Reassign them first."
            );
        }

        department.setIsDeleted(true);
        departmentRepository.save(department);

        logger.info("Department soft deleted successfully with ID: {}", department.getId());
    }

}
