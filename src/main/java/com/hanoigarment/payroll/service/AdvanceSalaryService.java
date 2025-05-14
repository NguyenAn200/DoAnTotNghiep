package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.AdvanceSalaryDTO;
import com.hanoigarment.payroll.dto.AdvanceSalaryRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.AdvanceSalary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdvanceSalaryService {
    AdvanceSalaryDTO createAdvanceSalary(AdvanceSalaryRequestDTO requestDTO);

    AdvanceSalaryDTO getAdvanceSalaryById(Integer advanceId);

    List<AdvanceSalaryDTO> getAllAdvanceSalaries();

    PageResponse<AdvanceSalaryDTO> getAllAdvanceSalariesPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<AdvanceSalaryDTO> getAdvanceSalariesByEmployeeId(Integer employeeId);

    List<AdvanceSalaryDTO> getAdvanceSalariesByStatus(String status);

    List<AdvanceSalaryDTO> getAdvanceSalariesByDateRange(LocalDate startDate, LocalDate endDate);

    List<AdvanceSalaryDTO> getAdvanceSalariesByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate);

    Double getTotalAdvanceAmountByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate);

    AdvanceSalaryDTO updateAdvanceSalary(Integer advanceId, AdvanceSalaryRequestDTO requestDTO);

    AdvanceSalaryDTO approveAdvanceSalary(Integer advanceId, Integer approvedBy, String note);

    AdvanceSalaryDTO rejectAdvanceSalary(Integer advanceId, Integer approvedBy, String note);

    void deleteAdvanceSalary(Integer advanceId);
}
