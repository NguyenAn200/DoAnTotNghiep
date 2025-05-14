package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.AdvanceSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdvanceSalaryRepository extends JpaRepository<AdvanceSalary, Integer> {
    List<AdvanceSalary> findByEmployeeEmployeeId(Integer employeeId);

    List<AdvanceSalary> findByStatus(String status);

    List<AdvanceSalary> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM AdvanceSalary a WHERE a.employee.employeeId = :employeeId AND a.requestDate BETWEEN :startDate AND :endDate")
    List<AdvanceSalary> findAdvancesByEmployeeAndDateRange(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(a.amount) FROM AdvanceSalary a WHERE a.employee.employeeId = :employeeId AND a.status = 'APPROVED' AND a.requestDate BETWEEN :startDate AND :endDate")
    Double getTotalAdvanceAmountByEmployeeAndPeriod(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
