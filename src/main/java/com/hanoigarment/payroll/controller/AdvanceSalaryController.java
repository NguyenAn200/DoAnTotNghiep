package com.hanoigarment.payroll.controller;

import com.hanoigarment.payroll.config.HostConfig;
import com.hanoigarment.payroll.dto.AdvanceSalaryDTO;
import com.hanoigarment.payroll.dto.AdvanceSalaryRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.service.AdvanceSalaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/advance-salaries/v1.0")
@Validated
public class AdvanceSalaryController {
    private static final Logger logger = LoggerFactory.getLogger(AdvanceSalaryController.class);

    @Autowired
    private AdvanceSalaryService advanceSalaryService;

    @Autowired
    private HostConfig config;

    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createAdvance(
            @RequestBody AdvanceSalaryRequestDTO requestDTO) {
        try {
            AdvanceSalaryDTO dto = advanceSalaryService.createAdvanceSalary(requestDTO);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error creating advance salary", e);
            Map<String, String> body = new HashMap<>();
            body.put("error", "Failed to create advance salary: " + e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = {"/{advanceId}"}, method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer advanceId) {
        try {
            AdvanceSalaryDTO dto = advanceSalaryService.getAdvanceSalaryById(advanceId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error fetching advance salary id {}", advanceId, e);
            Map<String, String> body = new HashMap<>();
            body.put("error", "Advance salary not found: " + e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
    }

    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            List<AdvanceSalaryDTO> list = advanceSalaryService.getAllAdvanceSalaries();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            logger.error("Error fetching all advance salaries", e);
            Map<String, String> body = new HashMap<>();
            body.put("error", "Failed to fetch advance salaries: " + e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings({"rawtypes", "null"})
    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public ResponseEntity<?> getPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "advanceId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            PageResponse<AdvanceSalaryDTO> page = advanceSalaryService
                    .getAllAdvanceSalariesPaged(pageNo, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("Error fetching paged advance salaries", e);
            Map<String, String> body = new HashMap<>();
            body.put("error", "Failed to fetch paged data: " + e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

}
