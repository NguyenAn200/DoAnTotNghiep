package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceSalaryRequestDTO {
    private Integer employeeId;
    private LocalDate requestDate;
    private BigDecimal amount;
    private String reason;
    private String status;
    private LocalDate approvalDate;
    private Integer approvedBy;
    private String note;
}
