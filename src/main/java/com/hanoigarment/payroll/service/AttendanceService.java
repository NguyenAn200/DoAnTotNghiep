package com.hanoigarment.payroll.service;


import com.hanoigarment.payroll.dto.AttendanceDTO;
import com.hanoigarment.payroll.dto.AttendanceRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.Attendance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AttendanceService {
    AttendanceDTO createAttendance(AttendanceRequestDTO requestDTO);

    List<AttendanceDTO> createBulkAttendance(List<AttendanceRequestDTO> requestDTOs);

    AttendanceDTO getAttendanceById(Integer attendanceId);

    AttendanceDTO getAttendanceByEmployeeAndDate(Integer employeeId, LocalDate workDate);

    List<AttendanceDTO> getAllAttendances();

    PageResponse<AttendanceDTO> getAllAttendancesPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<AttendanceDTO> getAttendancesByEmployeeId(Integer employeeId);

    List<AttendanceDTO> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate);

    List<AttendanceDTO> getAttendancesByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate);

    Long countPresentDaysByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumWorkingHoursByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumOvertimeHoursByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate);

    Map<Integer, BigDecimal> getWorkingDaysSummaryByDepartment(Integer departmentId, LocalDate startDate, LocalDate endDate);

    AttendanceDTO updateAttendance(Integer attendanceId, AttendanceRequestDTO requestDTO);

    void deleteAttendance(Integer attendanceId);

    void generateMonthlyAttendanceReport(Integer month, Integer year);
}

