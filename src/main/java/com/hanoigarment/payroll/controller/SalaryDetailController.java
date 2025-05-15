package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.SalaryDetailDTO;
import com.hanoigarment.payroll.dto.SalaryDetailRequestDTO;
import com.hanoigarment.payroll.service.SalaryDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salary-details/v1.0")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@Validated
public class SalaryDetailController {
    private static final Logger logger = LoggerFactory.getLogger(SalaryDetailController.class);

    @Autowired
    private SalaryDetailService salaryDetailService;

    // Create
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody SalaryDetailRequestDTO req) {
        try {
            SalaryDetailDTO dto = salaryDetailService.createSalaryDetail(req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating salary detail", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            SalaryDetailDTO dto = salaryDetailService.getSalaryDetailById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching salary detail id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<SalaryDetailDTO> list = salaryDetailService.getAllSalaryDetails();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all salary details", e);
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
            @RequestParam(defaultValue = "salaryDetailId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<SalaryDetailDTO> page = salaryDetailService.getAllSalaryDetailsPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged salary details", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Filter by salaryId
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/salary/{salaryId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBySalary(@PathVariable Integer salaryId) {
        try {
            List<SalaryDetailDTO> list = salaryDetailService.getSalaryDetailsBySalaryId(salaryId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salary details for salary id {}", salaryId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Filter by itemType
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/item-type/{itemType}", method = RequestMethod.GET)
    public ResponseEntity<?> getByItemType(@PathVariable String itemType) {
        try {
            List<SalaryDetailDTO> list = salaryDetailService.getSalaryDetailsByItemType(itemType);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salary details by item type {}", itemType, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Filter by employee, month, year
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/employee/{employeeId}/period", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmployeeAndPeriod(
            @PathVariable Integer employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            List<SalaryDetailDTO> list = salaryDetailService.getSalaryDetailsByEmployeeAndPeriod(employeeId, month, year);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salary details for employee {} for {}/{}", employeeId, month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Filter by employee, itemType, month, year
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/employee/{employeeId}/item-type/{itemType}/period", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmpItemAndPeriod(
            @PathVariable Integer employeeId,
            @PathVariable String itemType,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            List<SalaryDetailDTO> list = salaryDetailService.getSalaryDetailsByEmployeeAndItemTypeAndPeriod(employeeId, itemType, month, year);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching salary details for employee {} type {} for {}/{}", employeeId, itemType, month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody SalaryDetailRequestDTO req) {
        try {
            SalaryDetailDTO dto = salaryDetailService.updateSalaryDetail(id, req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating salary detail id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            salaryDetailService.deleteSalaryDetail(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting salary detail id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
