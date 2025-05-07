package com.hanoigarment.payroll.service;


import com.hanoigarment.payroll.dto.AttendanceDTO;
import com.hanoigarment.payroll.entity.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    List<AttendanceDTO> getAllAttendance();
    Optional<AttendanceDTO> getAttendanceById(Integer id);
    List<AttendanceDTO> getByEmployeeAndDate(Integer employeeId, LocalDate date);
    List<AttendanceDTO> getBetweenDates(LocalDate start, LocalDate end);
    AttendanceDTO createAttendance(AttendanceDTO attendanceDTO);
    AttendanceDTO updateAttendance(Integer id, AttendanceDTO attendanceDTO);
    void deleteAttendance(Integer id);
}

