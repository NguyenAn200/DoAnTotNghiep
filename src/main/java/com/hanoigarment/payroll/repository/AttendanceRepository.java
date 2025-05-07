package com.hanoigarment.payroll.repository;

import com.hanoigarment.payroll.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByEmployeeEmployeeIdAndWorkDate(Integer employeeId, LocalDate workDate);
    List<Attendance> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);
}
