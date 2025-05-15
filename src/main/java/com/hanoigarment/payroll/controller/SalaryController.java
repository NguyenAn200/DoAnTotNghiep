package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.*;
import com.hanoigarment.payroll.service.SalaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salaries/v1.0")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@Validated
public class SalaryController {
    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    @Autowired
    private SalaryService salaryService;

    // Create salary with optional details
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody SalaryRequestDTO req) {
        try {
            SalaryDTO dto = salaryService.createSalary(req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating salary", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get salary by ID
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            SalaryDTO dto = salaryService.getSalaryById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching salary id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get salary with details
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getWithDetails(@PathVariable Integer id) {
        try {
            SalaryDTO dto = salaryService.getSalaryByIdWithDetails(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching salary details id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all salaries
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<SalaryDTO> list = salaryService.getAllSalaries();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all salaries", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Paged salaries
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public ResponseEntity<?> getPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "salaryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<SalaryDTO> page = salaryService.getAllSalariesPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged salaries", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By employee
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/employee/{employeeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmployee(@PathVariable Integer employeeId) {
        try {
            List<SalaryDTO> list = salaryService.getSalariesByEmployeeId(employeeId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salaries for employee {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By period
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/period", method = RequestMethod.GET)
    public ResponseEntity<?> getByPeriod(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            List<SalaryDTO> list = salaryService.getSalariesByPeriod(month, year);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salaries for period {}/{}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By employee and period
    @RequestMapping(value = "/employee/{employeeId}/period", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmployeeAndPeriod(
            @PathVariable Integer employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            SalaryDTO dto = salaryService.getSalaryByEmployeeAndPeriod(employeeId, month, year);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching salary for employee {} period {}/{}", employeeId, month, year, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By payment status
    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        try {
            List<SalaryDTO> list = salaryService.getSalariesByPaymentStatus(status);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salaries by status {}", status, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By department and period
    @RequestMapping(value = "/department/{deptId}/period", method = RequestMethod.GET)
    public ResponseEntity<?> getByDepartmentAndPeriod(
            @PathVariable Integer deptId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            List<SalaryDTO> list = salaryService.getSalariesByDepartmentAndPeriod(deptId, month, year);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salaries for department {} period {}/{}", deptId, month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Aggregates: total gross, net
    @RequestMapping(value = "/summary/total-gross", method = RequestMethod.GET)
    public ResponseEntity<?> totalGross(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            Double total = salaryService.getTotalGrossSalaryByPeriod(month, year);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("Error total gross for period {}/{}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @RequestMapping(value = "/summary/total-net", method = RequestMethod.GET)
    public ResponseEntity<?> totalNet(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            Double total = salaryService.getTotalNetSalaryByPeriod(month, year);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("Error total net for period {}/{}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Salary summary by department
    @RequestMapping(value = "/summary/department", method = RequestMethod.GET)
    public ResponseEntity<?> summaryByDept(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            Map<String, Double> map = salaryService.getSalarySummaryByDepartment(month, year);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            logger.error("Error summary by department for period {}/{}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update salary record
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody SalaryRequestDTO req) {
        try {
            SalaryDTO dto = salaryService.updateSalary(id, req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating salary id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update payment status and date
    @RequestMapping(value = "/{id}/payment", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePayment(
            @PathVariable Integer id,
            @RequestParam String status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDate) {
        try {
            SalaryDTO dto = salaryService.updateSalaryPaymentStatus(id, status, paymentDate);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating payment for salary id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Add salary detail
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseEntity<?> addDetail(@RequestBody SalaryDetailRequestDTO req) {
        try {
            SalaryDetailDTO dto = salaryService.addSalaryDetail(req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error adding salary detail", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update salary detail
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateDetail(@PathVariable Integer id, @RequestBody SalaryDetailRequestDTO req) {
        try {
            SalaryDetailDTO dto = salaryService.updateSalaryDetail(id, req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating salary detail id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete salary detail
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDetail(@PathVariable Integer id) {
        try {
            salaryService.deleteSalaryDetail(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting salary detail id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete salary
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSalary(@PathVariable Integer id) {
        try {
            salaryService.deleteSalary(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting salary id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Generate monthly salaries
    @RequestMapping(value = "/generate/{month}/{year}", method = RequestMethod.POST)
    public ResponseEntity<?> generateMonthly(@PathVariable Integer month, @PathVariable Integer year) {
        try {
            salaryService.generateMonthlySalaries(month, year);
            return ResponseEntity.ok(Map.of("message", "Monthly salaries generation initiated"));
        } catch (Exception e) {
            logger.error("Error generating monthly salaries for {}/{}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Generate salary slip PDF
    @RequestMapping(value = "/{id}/slip", method = RequestMethod.GET)
    public ResponseEntity<?> getSlip(@PathVariable Integer id) {
        try {
            byte[] pdf = salaryService.generateSalarySlipPdf(id);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .body(pdf);
        } catch (Exception e) {
            logger.error("Error generating salary slip pdf id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Generate monthly report PDF
    @RequestMapping(value = "/report/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity<?> getMonthlyReport(
            @PathVariable Integer month,
            @PathVariable Integer year,
            @RequestParam(required = false) Integer departmentId) {
        try {
            byte[] pdf = salaryService.generateMonthlySalaryReportPdf(month, year, departmentId);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .body(pdf);
        } catch (Exception e) {
            logger.error("Error generating monthly report pdf for {}/{} dept {}", month, year, departmentId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
