package com.hanoigarment.payroll.service.Imp;


import com.hanoigarment.payroll.dto.AdvanceSalaryDTO;
import com.hanoigarment.payroll.dto.AdvanceSalaryRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.AdvanceSalary;
import com.hanoigarment.payroll.entity.Employee;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.AdvanceSalaryRepository;
import com.hanoigarment.payroll.service.AdvanceSalaryService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdvanceSalaryServiceImpl implements AdvanceSalaryService {

    private final AdvanceSalaryRepository repo;

    public AdvanceSalaryServiceImpl(AdvanceSalaryRepository repo) {
        this.repo = repo;
    }

    @Override
    public AdvanceSalaryDTO createAdvanceSalary(AdvanceSalaryRequestDTO req) {
        AdvanceSalary e = new AdvanceSalary();
        // Tham chiếu Employee chỉ bằng ID
        Employee emp = new Employee();
        emp.setEmployeeId(req.getEmployeeId());
        e.setEmployee(emp);

        e.setRequestDate(req.getRequestDate());
        e.setAmount(req.getAmount());
        e.setReason(req.getReason());
        e.setStatus(req.getStatus() != null ? req.getStatus() : "PENDING");
        e.setApprovalDate(req.getApprovalDate());
        e.setApprovedBy(req.getApprovedBy());
        e.setNote(req.getNote());

        AdvanceSalary saved = repo.save(e);
        return toDto(saved);
    }

    @Override
    public AdvanceSalaryDTO getAdvanceSalaryById(Integer advanceId) {
        AdvanceSalary e = repo.findById(advanceId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvanceSalary", "advanceId", advanceId));
        return toDto(e);
    }

    @Override
    public List<AdvanceSalaryDTO> getAllAdvanceSalaries() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AdvanceSalaryDTO> getAllAdvanceSalariesPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<AdvanceSalary> page = repo.findAll(PageRequest.of(pageNo, pageSize, sort));
        List<AdvanceSalaryDTO> dtos = page.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageResponse<>(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public List<AdvanceSalaryDTO> getAdvanceSalariesByEmployeeId(Integer employeeId) {
        return repo.findByEmployeeEmployeeId(employeeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvanceSalaryDTO> getAdvanceSalariesByStatus(String status) {
        return repo.findByStatus(status).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvanceSalaryDTO> getAdvanceSalariesByDateRange(LocalDate startDate, LocalDate endDate) {
        return repo.findByRequestDateBetween(startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvanceSalaryDTO> getAdvanceSalariesByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        return repo.findAdvancesByEmployeeAndDateRange(employeeId, startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalAdvanceAmountByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        Double sum = repo.getTotalAdvanceAmountByEmployeeAndPeriod(employeeId, startDate, endDate);
        return sum == null ? 0.0 : sum;
    }

    @Override
    public AdvanceSalaryDTO updateAdvanceSalary(Integer advanceId, AdvanceSalaryRequestDTO req) {
        AdvanceSalary e = repo.findById(advanceId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvanceSalary", "advanceId", advanceId));
        if (req.getRequestDate()  != null) e.setRequestDate(req.getRequestDate());
        if (req.getAmount()       != null) e.setAmount(req.getAmount());
        if (req.getReason()       != null) e.setReason(req.getReason());
        if (req.getStatus()       != null) e.setStatus(req.getStatus());
        if (req.getApprovalDate() != null) e.setApprovalDate(req.getApprovalDate());
        if (req.getApprovedBy()   != null) e.setApprovedBy(req.getApprovedBy());
        if (req.getNote()         != null) e.setNote(req.getNote());

        return toDto(repo.save(e));
    }

    @Override
    public AdvanceSalaryDTO approveAdvanceSalary(Integer advanceId, Integer approvedBy, String note) {
        AdvanceSalary e = repo.findById(advanceId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvanceSalary", "advanceId", advanceId));
        e.setStatus("APPROVED");
        e.setApprovalDate(LocalDate.now());
        e.setApprovedBy(approvedBy);
        e.setNote(note);
        return toDto(repo.save(e));
    }

    @Override
    public AdvanceSalaryDTO rejectAdvanceSalary(Integer advanceId, Integer approvedBy, String note) {
        AdvanceSalary e = repo.findById(advanceId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvanceSalary", "advanceId", advanceId));
        e.setStatus("REJECTED");
        e.setApprovalDate(LocalDate.now());
        e.setApprovedBy(approvedBy);
        e.setNote(note);
        return toDto(repo.save(e));
    }

    @Override
    public void deleteAdvanceSalary(Integer advanceId) {
        if (!repo.existsById(advanceId)) {
            throw new ResourceNotFoundException("AdvanceSalary", "advanceId", advanceId);
        }
        repo.deleteById(advanceId);
    }

    // Helper chuyển Entity -> DTO
    private AdvanceSalaryDTO toDto(AdvanceSalary e) {
        AdvanceSalaryDTO dto = new AdvanceSalaryDTO();
        dto.setAdvanceId(e.getAdvanceId());
        dto.setEmployeeId(e.getEmployee().getEmployeeId());
        dto.setEmployeeCode(e.getEmployee().getEmployeeCode());
        dto.setRequestDate(e.getRequestDate());
        dto.setAmount(e.getAmount());
        dto.setReason(e.getReason());
        dto.setStatus(e.getStatus());
        dto.setApprovalDate(e.getApprovalDate());
        dto.setApprovedBy(e.getApprovedBy());
        dto.setApprovedName(null);  // nếu có lookup user, bổ sung sau
        dto.setNote(e.getNote());
        return dto;
    }
}