package com.yogesh.employee_management_system.service.impl;

import com.yogesh.employee_management_system.dto.employee.EmployeeRequest;
import com.yogesh.employee_management_system.dto.employee.EmployeeResponse;
import com.yogesh.employee_management_system.dto.employee.EmployeeUpdateRequest;
import com.yogesh.employee_management_system.entity.Department;
import com.yogesh.employee_management_system.entity.Employee;
import com.yogesh.employee_management_system.entity.Role;
import com.yogesh.employee_management_system.entity.User;
import com.yogesh.employee_management_system.enums.EmployeeStatus;
import com.yogesh.employee_management_system.exception.DuplicateResourceException;
import com.yogesh.employee_management_system.exception.ResourceNotFoundException;
import com.yogesh.employee_management_system.repository.DepartmentRepository;
import com.yogesh.employee_management_system.repository.EmployeeRepository;
import com.yogesh.employee_management_system.repository.RoleRepository;
import com.yogesh.employee_management_system.repository.UserRepository;
import com.yogesh.employee_management_system.service.EmployeeService;
import com.yogesh.employee_management_system.util.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        logger.info("Creating employee with email: {}", request.getEmail());

        if(employeeRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            logger.warn("Employee email already exists: {}", request.getEmail());
            throw new DuplicateResourceException("Email already exists.");
        }
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists.");
        }

        Department department= departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new
                        ResourceNotFoundException("Department not found."));
        Role role= roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new
                        ResourceNotFoundException("Role not found."));

        Employee manager= null;
         if(request.getManagerId() != null) {
             manager= employeeRepository.findByIdAndIsDeletedFalse(request.getManagerId())
                     .orElseThrow(() -> new
                             ResourceNotFoundException("Manager not found."));
         }

         User user = new User();
         user.setUsername(request.getUsername());
         user.setPassword(passwordEncoder.encode(request.getPassword()));
         user.setRole(role);
         User savedUser= userRepository.save(user);

         Employee employee = new Employee();

         employee.setFirstName(request.getFirstName());
         employee.setLastName(request.getLastName());
         employee.setEmail(request.getEmail());
         employee.setPhone(request.getPhone());
         employee.setGender(request.getGender());
         employee.setDesignation(request.getDesignation());
         employee.setSalary(request.getSalary());
         employee.setJoiningDate(request.getJoiningDate());
         employee.setStatus(EmployeeStatus.ACTIVE);
         employee.setDepartment(department);
         employee.setUser(savedUser);
         employee.setManager(manager);

         Employee savedEmployee = employeeRepository.save(employee);

         logger.info("Employee created successfully with ID: {}", savedEmployee.getId());

         return EmployeeMapper.toResponse(savedEmployee);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        logger.info("Fetching employees. Page: {}, Size: {}, SortBy: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction
        );

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeRepository.findByIsDeletedFalse(pageable)
                .map(EmployeeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> searchEmployees(
            String keyword,
            int page,
            int size
    ) {

        logger.info("Searching employees with keyword: {}, Page: {}, Size: {}",
                keyword,
                page,
                size
        );

        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findByFirstNameContainingIgnoreCaseAndIsDeletedFalse(
                        keyword,
                        pageable
                )
                .map(EmployeeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        logger.info("Fetching employee with ID: {}", id);

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new
                        ResourceNotFoundException("Employee not found."));
        return EmployeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {

        logger.info("Updating employee with ID: {}", id);
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Employee not found."));

        if (employeeRepository.existsByEmailAndIsDeletedFalseAndIdNot(
                request.getEmail(),
                id
        )) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (employeeRepository.existsByPhoneAndIsDeletedFalseAndIdNot(
                request.getPhone(),
                id
        )) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(()-> new
                        ResourceNotFoundException("Department not found."));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setGender(request.getGender());
        employee.setDesignation(request.getDesignation());
        employee.setSalary(request.getSalary());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);

        logger.info("Employee updated successfully with ID: {}", updatedEmployee.getId());

        return EmployeeMapper.toResponse(updatedEmployee);

    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {

        logger.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Employee not found."));
        employee.setIsDeleted(true);
        employeeRepository.save(employee);

        logger.info("Employee soft deleted successfully with ID: {}", employee.getId());

    }

}
