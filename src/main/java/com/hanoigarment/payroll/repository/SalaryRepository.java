package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    List<Salary> findByEmployeeEmployeeId(Integer employeeId);

    List<Salary> findByPeriodMonthAndPeriodYear(Integer month, Integer year);

    Optional<Salary> findByEmployeeEmployeeIdAndPeriodMonthAndPeriodYear(
            Integer employeeId, Integer month, Integer year
    );

    List<Salary> findByPaymentStatus(String paymentStatus);

    @Query("SELECT s FROM Salary s WHERE s.employee.department.departmentId = :departmentId AND s.periodMonth = :month AND s.periodYear = :year")
    List<Salary> findByDepartmentAndPeriod(
            @Param("departmentId") Integer departmentId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT SUM(s.grossSalary) FROM Salary s WHERE s.periodMonth = :month AND s.periodYear = :year")
    Double getTotalGrossSalaryByPeriod(
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT SUM(s.netSalary) FROM Salary s WHERE s.periodMonth = :month AND s.periodYear = :year")
    Double getTotalNetSalaryByPeriod(
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT s FROM Salary s JOIN FETCH s.salaryDetails WHERE s.salaryId = :salaryId")
    Optional<Salary> findByIdWithDetails(Integer salaryId);
}
