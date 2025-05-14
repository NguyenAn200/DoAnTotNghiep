package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.*;
import com.hanoigarment.payroll.entity.Salary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SalaryService {
    SalaryDTO createSalary(SalaryRequestDTO requestDTO);

    SalaryDTO getSalaryById(Integer salaryId);

    SalaryDTO getSalaryByIdWithDetails(Integer salaryId);

    List<SalaryDTO> getAllSalaries();

    PageResponse<SalaryDTO> getAllSalariesPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<SalaryDTO> getSalariesByEmployeeId(Integer employeeId);

    List<SalaryDTO> getSalariesByPeriod(Integer month, Integer year);

    SalaryDTO getSalaryByEmployeeAndPeriod(Integer employeeId, Integer month, Integer year);

    List<SalaryDTO> getSalariesByPaymentStatus(String paymentStatus);

    List<SalaryDTO> getSalariesByDepartmentAndPeriod(Integer departmentId, Integer month, Integer year);

    Double getTotalGrossSalaryByPeriod(Integer month, Integer year);

    Double getTotalNetSalaryByPeriod(Integer month, Integer year);

    Map<String, Double> getSalarySummaryByDepartment(Integer month, Integer year);

    SalaryDTO updateSalary(Integer salaryId, SalaryRequestDTO requestDTO);

    SalaryDTO updateSalaryPaymentStatus(Integer salaryId, String paymentStatus, LocalDateTime paymentDate);

    SalaryDetailDTO addSalaryDetail(SalaryDetailRequestDTO requestDTO);

    SalaryDetailDTO updateSalaryDetail(Integer salaryDetailId, SalaryDetailRequestDTO requestDTO);

    void deleteSalaryDetail(Integer salaryDetailId);

    void deleteSalary(Integer salaryId);

    void generateMonthlySalaries(Integer month, Integer year);

    byte[] generateSalarySlipPdf(Integer salaryId);

    byte[] generateMonthlySalaryReportPdf(Integer month, Integer year, Integer departmentId);
}