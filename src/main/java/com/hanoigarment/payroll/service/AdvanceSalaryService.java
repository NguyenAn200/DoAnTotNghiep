package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.AdvanceSalaryDTO;
import com.hanoigarment.payroll.entity.AdvanceSalary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdvanceSalaryService {
    List<AdvanceSalaryDTO> getAllAdvances();
    Optional<AdvanceSalaryDTO> getAdvanceById(Integer id);
    List<AdvanceSalaryDTO> getByEmployee(Integer employeeId);
    List<AdvanceSalaryDTO> getBetweenDates(LocalDate start, LocalDate end);
    AdvanceSalaryDTO createAdvance(AdvanceSalaryDTO advanceDTO);
    AdvanceSalaryDTO updateAdvance(Integer id, AdvanceSalaryDTO advanceDTO);
    void deleteAdvance(Integer id);
}
