package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequestDTO {
    private String positionName;
    private BigDecimal baseSalary;
    private String description;
    private Boolean isActive;
    private Integer departmentId;
}
