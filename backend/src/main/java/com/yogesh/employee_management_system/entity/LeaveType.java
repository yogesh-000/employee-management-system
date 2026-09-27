package com.yogesh.employee_management_system.entity;

import com.yogesh.employee_management_system.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "leave_types")
@Getter
@Setter
public class LeaveType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name= "max_days", nullable = false)
    private Integer maxDays;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted= false;

}
