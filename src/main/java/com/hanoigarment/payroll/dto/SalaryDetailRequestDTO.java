package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDetailRequestDTO {
    private Integer salaryId;
    private String itemName;
    private String itemType;
    private BigDecimal amount;
    private String description;
}
