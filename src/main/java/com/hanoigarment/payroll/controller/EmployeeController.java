package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.EmployeeDTO;
import com.hanoigarment.payroll.dto.EmployeeRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/api/employees/v1.0")
@Validated
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    // Create
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody EmployeeRequestDTO req) {
        try {
            EmployeeDTO dto = employeeService.createEmployee(req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating employee", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            EmployeeDTO dto = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get with attendances
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/attendances", method = RequestMethod.GET)
    public ResponseEntity<?> getWithAttendances(@PathVariable Integer id) {
        try {
            EmployeeDTO dto = employeeService.getEmployeeByIdWithAttendances(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching attendances for employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get with salaries
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/salaries", method = RequestMethod.GET)
    public ResponseEntity<?> getWithSalaries(@PathVariable Integer id) {
        try {
            EmployeeDTO dto = employeeService.getEmployeeByIdWithSalaries(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching salaries for employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get with advances
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/advances", method = RequestMethod.GET)
    public ResponseEntity<?> getWithAdvances(@PathVariable Integer id) {
        try {
            EmployeeDTO dto = employeeService.getEmployeeByIdWithAdvances(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching advances for employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by code
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search/code", method = RequestMethod.GET)
    public ResponseEntity<?> getByCode(@RequestParam String code) {
        try {
            EmployeeDTO dto = employeeService.getEmployeeByEmployeeCode(code);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching employee by code {}", code, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID card
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search/idcard", method = RequestMethod.GET)
    public ResponseEntity<?> getByIdCard(@RequestParam String idCard) {
        try {
            EmployeeDTO dto = employeeService.getEmployeeByIdCard(idCard);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching employee by idCard {}", idCard, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<EmployeeDTO> list = employeeService.getAllEmployees();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all employees", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all active
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<?> getAllActive() {
        try {
            List<EmployeeDTO> list = employeeService.getAllActiveEmployees();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching active employees", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Paged
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public ResponseEntity<?> getPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<EmployeeDTO> page = employeeService.getAllEmployeesPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged employees", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By department
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/department/{deptId}", method = RequestMethod.GET)
    public ResponseEntity<?> getByDepartment(@PathVariable Integer deptId) {
        try {
            List<EmployeeDTO> list = employeeService.getEmployeesByDepartmentId(deptId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching employees by department {}", deptId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/position/{posId}", method = RequestMethod.GET)
    public ResponseEntity<?> getByPosition(@PathVariable Integer posId) {
        try {
            List<EmployeeDTO> list = employeeService.getEmployeesByPositionId(posId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching employees by position {}", posId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By name containing
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search/name", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@RequestParam String name) {
        try {
            List<EmployeeDTO> list = employeeService.getEmployeesByNameContaining(name);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching employees by name {}", name, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By join date range
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/join-date-range", method = RequestMethod.GET)
    public ResponseEntity<?> getByJoinDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        try {
            List<EmployeeDTO> list = employeeService.getEmployeesByJoinDateRange(start, end);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching employees by join date between {} and {}", start, end, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Count by department
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/count/department", method = RequestMethod.GET)
    public ResponseEntity<?> countByDepartment() {
        try {
            Map<String, Long> map = employeeService.getEmployeeCountByDepartment();
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            logger.error("Error counting employees by department", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Count by position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/count/position", method = RequestMethod.GET)
    public ResponseEntity<?> countByPosition() {
        try {
            Map<String, Long> map = employeeService.getEmployeeCountByPosition();
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            logger.error("Error counting employees by position", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody EmployeeRequestDTO req) {
        try {
            EmployeeDTO dto = employeeService.updateEmployee(id, req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Activate
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        try {
            EmployeeDTO dto = employeeService.activateEmployee(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error activating employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Deactivate
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.POST)
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            EmployeeDTO dto = employeeService.deactivateEmployee(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error deactivating employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
