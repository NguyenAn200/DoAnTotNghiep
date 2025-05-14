package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.AttendanceDTO;
import com.hanoigarment.payroll.dto.AttendanceRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/attendances/v1.0")
@Validated
public class AttendanceController {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    /**
     * Create single attendance record
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody AttendanceRequestDTO dto) {
        try {
            AttendanceDTO created = attendanceService.createAttendance(dto);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Error creating attendance", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create attendance: " + e.getMessage()));
        }
    }

    /**
     * Bulk create attendance records
     */
    @RequestMapping(value = "/bulk", method = RequestMethod.POST)
    public ResponseEntity<?> createBulk(@RequestBody List<AttendanceRequestDTO> dtos) {
        try {
            List<AttendanceDTO> created = attendanceService.createBulkAttendance(dtos);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Error bulk creating attendance", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create bulk attendance: " + e.getMessage()));
        }
    }

    /**
     * Get by attendance ID
     */
    @RequestMapping(value = "/{attendanceId}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer attendanceId) {
        try {
            AttendanceDTO dto = attendanceService.getAttendanceById(attendanceId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching attendance id {}", attendanceId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get by employee and date
     */
    @RequestMapping(value = "/employee/{employeeId}", params = "workDate", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmployeeAndDate(
            @PathVariable Integer employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDate) {
        try {
            AttendanceDTO dto = attendanceService.getAttendanceByEmployeeAndDate(employeeId, workDate);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching attendance for employee {} on {}", employeeId, workDate, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get all attendances
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<AttendanceDTO> list = attendanceService.getAllAttendances();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all attendances", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get paged attendances
     */
    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public ResponseEntity<?> getPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "workDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<AttendanceDTO> page = attendanceService.getAllAttendancesPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged attendances", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get attendances by employee
     */
    @RequestMapping(value = "/employee/{employeeId}/all", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmployee(@PathVariable Integer employeeId) {
        try {
            List<AttendanceDTO> list = attendanceService.getAttendancesByEmployeeId(employeeId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching attendances for employee {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get attendances by date range
     */
    @RequestMapping(value = "/date-range", method = RequestMethod.GET)
    public ResponseEntity<?> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<AttendanceDTO> list = attendanceService.getAttendancesByDateRange(startDate, endDate);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching attendances between {} and {}", startDate, endDate, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get attendances by employee and date range
     */
    @RequestMapping(value = "/employee/{employeeId}/date-range", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmployeeAndDateRange(
            @PathVariable Integer employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<AttendanceDTO> list = attendanceService.getAttendancesByEmployeeAndDateRange(employeeId, startDate, endDate);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching attendances for employee {} between {} and {}", employeeId, startDate, endDate, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Count present days by employee and period
     */
    @RequestMapping(value = "/employee/{employeeId}/count-present", method = RequestMethod.GET)
    public ResponseEntity<?> countPresent(
            @PathVariable Integer employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Long count = attendanceService.countPresentDaysByEmployeeAndPeriod(employeeId, startDate, endDate);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error counting present days for employee {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Sum working hours by employee and period
     */
    @RequestMapping(value = "/employee/{employeeId}/sum-working", method = RequestMethod.GET)
    public ResponseEntity<?> sumWorking(
            @PathVariable Integer employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            BigDecimal sum = attendanceService.sumWorkingHoursByEmployeeAndPeriod(employeeId, startDate, endDate);
            return ResponseEntity.ok(sum);
        } catch (Exception e) {
            logger.error("Error summing working hours for employee {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Sum overtime hours by employee and period
     */
    @RequestMapping(value = "/employee/{employeeId}/sum-overtime", method = RequestMethod.GET)
    public ResponseEntity<?> sumOvertime(
            @PathVariable Integer employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            BigDecimal sum = attendanceService.sumOvertimeHoursByEmployeeAndPeriod(employeeId, startDate, endDate);
            return ResponseEntity.ok(sum);
        } catch (Exception e) {
            logger.error("Error summing overtime hours for employee {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get working days summary by department
     */
    @RequestMapping(value = "/department/{departmentId}/summary", method = RequestMethod.GET)
    public ResponseEntity<?> getDeptSummary(
            @PathVariable Integer departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<Integer, ? extends Number> summary = attendanceService.getWorkingDaysSummaryByDepartment(departmentId, startDate, endDate);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error fetching department summary {}", departmentId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update attendance
     */
    @RequestMapping(value = "/{attendanceId}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable Integer attendanceId,
            @RequestBody AttendanceRequestDTO dto) {
        try {
            AttendanceDTO updated = attendanceService.updateAttendance(attendanceId, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating attendance id {}", attendanceId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete attendance
     */
    @RequestMapping(value = "/{attendanceId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer attendanceId) {
        try {
            attendanceService.deleteAttendance(attendanceId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting attendance id {}", attendanceId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Generate monthly attendance report (stub)
     */
    @RequestMapping(value = "/report/monthly", method = RequestMethod.POST)
    public ResponseEntity<?> generateReport(
            @RequestParam int month,
            @RequestParam int year) {
        try {
            attendanceService.generateMonthlyAttendanceReport(month, year);
            return ResponseEntity.ok(Map.of("message", "Report generation initiated"));
        } catch (Exception e) {
            logger.error("Error generating monthly report for {}/{}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

