package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDTO {
    private Integer positionId;
    private String positionName;
    private BigDecimal baseSalary;
    private String description;
    private Boolean isActive;
    private Integer departmentId;
}
