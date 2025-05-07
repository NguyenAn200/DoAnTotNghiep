package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.AdvanceSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdvanceSalaryRepository extends JpaRepository<AdvanceSalary, Integer> {
    List<AdvanceSalary> findByEmployeeEmployeeId(Integer employeeId);
    List<AdvanceSalary> findByRequestDateBetween(LocalDate from, LocalDate to);
}

