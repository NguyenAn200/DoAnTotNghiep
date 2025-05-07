package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    Optional<Salary> findByEmployeeEmployeeIdAndPeriodMonthAndPeriodYear(Integer employeeId, Integer month, Integer year);
}
