package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.SalaryDTO;
import com.hanoigarment.payroll.entity.Salary;

import java.util.List;
import java.util.Optional;

public interface SalaryService {
    List<SalaryDTO> getAllSalaries();
    Optional<SalaryDTO> getSalaryById(Integer id);
    Optional<SalaryDTO> getByEmployeePeriod(Integer employeeId, Integer month, Integer year);
    SalaryDTO createSalary(SalaryDTO salaryDTO);
    SalaryDTO updateSalary(Integer id, SalaryDTO salaryDTO);
    void deleteSalary(Integer id);
}