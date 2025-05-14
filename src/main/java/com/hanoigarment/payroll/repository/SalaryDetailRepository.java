package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.SalaryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryDetailRepository extends JpaRepository<SalaryDetail, Integer> {
  List<SalaryDetail> findBySalarySalaryId(Integer salaryId);

  List<SalaryDetail> findByItemType(String itemType);

  @Query("SELECT sd FROM SalaryDetail sd WHERE sd.salary.employee.employeeId = :employeeId AND sd.salary.periodMonth = :month AND sd.salary.periodYear = :year")
  List<SalaryDetail> findByEmployeeAndPeriod(
          @Param("employeeId") Integer employeeId,
          @Param("month") Integer month,
          @Param("year") Integer year
  );

  @Query("SELECT sd FROM SalaryDetail sd WHERE sd.salary.employee.employeeId = :employeeId AND sd.itemType = :itemType AND sd.salary.periodMonth = :month AND sd.salary.periodYear = :year")
  List<SalaryDetail> findByEmployeeAndItemTypeAndPeriod(
          @Param("employeeId") Integer employeeId,
          @Param("itemType") String itemType,
          @Param("month") Integer month,
          @Param("year") Integer year
  );
}