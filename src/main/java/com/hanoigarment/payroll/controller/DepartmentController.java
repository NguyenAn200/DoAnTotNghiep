package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.DepartmentDTO;
import com.hanoigarment.payroll.dto.DepartmentRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.service.DepartmentService;
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
@RequestMapping("/api/departments/v1.0")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@Validated
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    // Create
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody DepartmentRequestDTO request) {
        try {
            DepartmentDTO dto = departmentService.createDepartment(request);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating department", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            DepartmentDTO dto = departmentService.getDepartmentById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching department id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID with positions
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/positions", method = RequestMethod.GET)
    public ResponseEntity<?> getWithPositions(@PathVariable Integer id) {
        try {
            DepartmentDTO dto = departmentService.getDepartmentByIdWithPositions(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching department positions id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by ID with employees
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/employees", method = RequestMethod.GET)
    public ResponseEntity<?> getWithEmployees(@PathVariable Integer id) {
        try {
            DepartmentDTO dto = departmentService.getDepartmentByIdWithEmployees(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching department employees id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by name
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@RequestParam String name) {
        try {
            DepartmentDTO dto = departmentService.getDepartmentByName(name);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching department name {}", name, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<DepartmentDTO> list = departmentService.getAllDepartments();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all departments", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all active
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<?> getAllActive() {
        try {
            List<DepartmentDTO> list = departmentService.getAllActiveDepartments();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching active departments", e);
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
            @RequestParam(defaultValue = "departmentId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<DepartmentDTO> page = departmentService
                    .getAllDepartmentsPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged departments", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody DepartmentRequestDTO request) {
        try {
            DepartmentDTO dto = departmentService.updateDepartment(id, request);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating department id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Activate
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        try {
            DepartmentDTO dto = departmentService.activateDepartment(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error activating department id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Deactivate
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.POST)
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            DepartmentDTO dto = departmentService.deactivateDepartment(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error deactivating department id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting department id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
