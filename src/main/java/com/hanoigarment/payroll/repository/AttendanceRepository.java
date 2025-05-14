package com.hanoigarment.payroll.repository;

import com.google.common.base.Optional;
import com.hanoigarment.payroll.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByEmployeeEmployeeId(Integer employeeId);

    List<Attendance> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Attendance> findByEmployeeEmployeeIdAndWorkDate(Integer employeeId, LocalDate workDate);

    @Query("SELECT a FROM Attendance a WHERE a.employee.employeeId = :employeeId AND a.workDate BETWEEN :startDate AND :endDate")
    List<Attendance> findAttendancesByEmployeeAndDateRange(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.employeeId = :employeeId AND a.workDate BETWEEN :startDate AND :endDate AND a.status = 'PRESENT'")
    Long countPresentDaysByEmployeeAndPeriod(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(a.workingHours) FROM Attendance a WHERE a.employee.employeeId = :employeeId AND a.workDate BETWEEN :startDate AND :endDate")
    BigDecimal sumWorkingHoursByEmployeeAndPeriod(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(a.overtimeHours) FROM Attendance a WHERE a.employee.employeeId = :employeeId AND a.workDate BETWEEN :startDate AND :endDate")
    BigDecimal sumOvertimeHoursByEmployeeAndPeriod(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

