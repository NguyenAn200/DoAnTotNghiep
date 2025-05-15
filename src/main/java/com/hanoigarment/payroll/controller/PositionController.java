package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.PositionDTO;
import com.hanoigarment.payroll.dto.PositionRequestDTO;
import com.hanoigarment.payroll.service.PositionService;
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
@RequestMapping("/api/positions/v1.0")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@Validated
public class PositionController {
    private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    private PositionService positionService;

    // Create a new position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody PositionRequestDTO req) {
        try {
            PositionDTO dto = positionService.createPosition(req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating position", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get position by ID
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            PositionDTO dto = positionService.getPositionById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching position id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get position with employees
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/employees", method = RequestMethod.GET)
    public ResponseEntity<?> getWithEmployees(@PathVariable Integer id) {
        try {
            PositionDTO dto = positionService.getPositionByIdWithEmployees(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching employees for position id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get by name
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@RequestParam String name) {
        try {
            PositionDTO dto = positionService.getPositionByName(name);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching position by name {}", name, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all positions
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<PositionDTO> list = positionService.getAllPositions();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all positions", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get all active positions
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<?> getAllActive() {
        try {
            List<PositionDTO> list = positionService.getAllActivePositions();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching active positions", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Filter by isActive flag
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public ResponseEntity<?> filterByActive(@RequestParam(required = false) Boolean isActive) {
        try {
            List<PositionDTO> list = positionService.getPositionsByActiveFlag(isActive);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error filtering positions by active {}", isActive, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Paged positions
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public ResponseEntity<?> getPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "positionId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<PositionDTO> page = positionService
                    .getAllPositionsPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged positions", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // By department
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/department/{deptId}", method = RequestMethod.GET)
    public ResponseEntity<?> getByDepartment(@PathVariable Integer deptId) {
        try {
            List<PositionDTO> list = positionService.getPositionsByDepartmentId(deptId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching positions by department {}", deptId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Active by department
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/department/{deptId}/active", method = RequestMethod.GET)
    public ResponseEntity<?> getActiveByDepartment(@PathVariable Integer deptId) {
        try {
            List<PositionDTO> list = positionService.getActivePositionsByDepartmentId(deptId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching active positions by department {}", deptId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody PositionRequestDTO req) {
        try {
            PositionDTO dto = positionService.updatePosition(id, req);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error updating position id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Activate position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        try {
            PositionDTO dto = positionService.activatePosition(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error activating position id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Deactivate position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.POST)
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            PositionDTO dto = positionService.deactivatePosition(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error deactivating position id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete position
    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            positionService.deletePosition(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting position id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
