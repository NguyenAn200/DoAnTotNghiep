package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Integer employeeId;
    private String employeeCode;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String idCard;
    private LocalDate joinDate;
    private String address;
    private String phone;
    private String email;
    private String bankAccount;
    private String bankName;
    private Boolean isActive;
    private Integer departmentId;
    private Integer positionId;
}
