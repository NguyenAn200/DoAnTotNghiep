package com.hanoigarment.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Integer attendanceId;
    private Integer employeeId;
    private String employeeCode;
    private String employeeName;
    private LocalDate workDate;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private BigDecimal workingHours;
    private BigDecimal overtimeHours;
    private String status;
    private String note;
    private String departmentName;
    private String positionName;
}
