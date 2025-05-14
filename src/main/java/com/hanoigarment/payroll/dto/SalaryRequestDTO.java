package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRequestDTO {
    private Integer employeeId;
    private Integer periodMonth;
    private Integer periodYear;
    private BigDecimal baseSalary;
    private BigDecimal totalWorkingDays;
    private BigDecimal actualWorkingDays;
    private BigDecimal overtimeAmount;
    private BigDecimal bonus;
    private BigDecimal allowance;
    private BigDecimal grossSalary;
    private BigDecimal insuranceAmount;
    private BigDecimal taxAmount;
    private BigDecimal advanceAmount;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private LocalDateTime paymentDate;
    private String paymentStatus;
    private List<SalaryDetailRequestDTO> salaryDetails;
}