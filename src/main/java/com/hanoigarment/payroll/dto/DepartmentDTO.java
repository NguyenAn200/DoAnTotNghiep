package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Integer departmentId;
    private String departmentName;
    private String description;
    private Boolean isActive;
    private int employeeCount;
    private int positionCount;
    private List<PositionDTO> positions;
}