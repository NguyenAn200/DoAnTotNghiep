package com.hanoigarment.payroll.service.Imp;

import com.hanoigarment.payroll.dto.*;
import com.hanoigarment.payroll.entity.Employee;
import com.hanoigarment.payroll.entity.Salary;
import com.hanoigarment.payroll.entity.SalaryDetail;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.EmployeeRepository;
import com.hanoigarment.payroll.repository.SalaryDetailRepository;
import com.hanoigarment.payroll.repository.SalaryRepository;
import com.hanoigarment.payroll.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryDetailRepository salaryDetailRepository;

    @Autowired
    public SalaryServiceImpl(SalaryRepository salaryRepository,
                             EmployeeRepository employeeRepository,
                             SalaryDetailRepository salaryDetailRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
        this.salaryDetailRepository = salaryDetailRepository;
    }

    private SalaryDTO mapToDTO(Salary s) {
        SalaryDTO dto = new SalaryDTO();
        dto.setSalaryId(s.getSalaryId());
        Employee e = s.getEmployee();
        dto.setEmployeeId(e.getEmployeeId());
        dto.setEmployeeCode(e.getEmployeeCode());
        dto.setEmployeeName(e.getFullName());
        dto.setPeriodMonth(s.getPeriodMonth());
        dto.setPeriodYear(s.getPeriodYear());
        dto.setBaseSalary(s.getBaseSalary());
        dto.setTotalWorkingDays(s.getTotalWorkingDays());
        dto.setActualWorkingDays(s.getActualWorkingDays());
        dto.setOvertimeAmount(s.getOvertimeAmount());
        dto.setBonus(s.getBonus());
        dto.setAllowance(s.getAllowance());
        dto.setGrossSalary(s.getGrossSalary());
        dto.setInsuranceAmount(s.getInsuranceAmount());
        dto.setTaxAmount(s.getTaxAmount());
        dto.setAdvanceAmount(s.getAdvanceAmount());
        dto.setDeductions(s.getDeductions());
        dto.setNetSalary(s.getNetSalary());
        dto.setPaymentDate(s.getPaymentDate());
        dto.setPaymentStatus(s.getPaymentStatus());
        dto.setDepartmentName(e.getDepartment().getDepartmentName());
        dto.setPositionName(e.getPosition().getPositionName());
        // map details if fetched
        if (s.getSalaryDetails() != null) {
            List<SalaryDetailDTO> details = s.getSalaryDetails().stream()
                    .map(d -> {
                        SalaryDetailDTO dd = new SalaryDetailDTO();
                        dd.setSalaryDetailId(d.getSalaryDetailId());
                        dd.setSalaryId(d.getSalary().getSalaryId());
                        dd.setDescription(d.getDescription());
                        dd.setAmount(d.getAmount());
                        return dd;
                    }).collect(Collectors.toList());
            dto.setSalaryDetails(details);
        }
        return dto;
    }

    @Override
    public SalaryDTO createSalary(SalaryRequestDTO req) {
        Employee e = employeeRepository.findById(req.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", req.getEmployeeId()));
        // check unique constraint
        salaryRepository.findByEmployeeEmployeeIdAndPeriodMonthAndPeriodYear(
                e.getEmployeeId(), req.getPeriodMonth(), req.getPeriodYear()
        ).ifPresent(s -> { throw new IllegalStateException("Salary for this period already exists"); });

        Salary s = new Salary();
        s.setEmployee(e);
        s.setPeriodMonth(req.getPeriodMonth());
        s.setPeriodYear(req.getPeriodYear());
        s.setBaseSalary(req.getBaseSalary());
        s.setTotalWorkingDays(req.getTotalWorkingDays());
        s.setActualWorkingDays(req.getActualWorkingDays());
        s.setOvertimeAmount(req.getOvertimeAmount());
        s.setBonus(req.getBonus());
        s.setAllowance(req.getAllowance());
        s.setGrossSalary(req.getGrossSalary());
        s.setInsuranceAmount(req.getInsuranceAmount());
        s.setTaxAmount(req.getTaxAmount());
        s.setAdvanceAmount(req.getAdvanceAmount());
        s.setDeductions(req.getDeductions());
        s.setNetSalary(req.getNetSalary());
        s.setPaymentDate(req.getPaymentDate());
        s.setPaymentStatus(req.getPaymentStatus());
        Salary saved = salaryRepository.save(s);
        if (req.getSalaryDetails() != null) {
            req.getSalaryDetails().forEach(dReq -> {
                SalaryDetail d = new SalaryDetail();
                d.setSalary(saved);
                d.setDescription(dReq.getDescription());
                d.setAmount(dReq.getAmount());
                salaryDetailRepository.save(d);
            });
        }
        return mapToDTO(saved);
    }

    @Override
    public SalaryDTO getSalaryById(Integer id) {
        Salary s = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", id));
        return mapToDTO(s);
    }

    @Override
    public SalaryDTO getSalaryByIdWithDetails(Integer id) {
        Salary s = salaryRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", id));
        return mapToDTO(s);
    }

    @Override
    public List<SalaryDTO> getAllSalaries() {
        return salaryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<SalaryDTO> getAllSalariesPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pg = PageRequest.of(pageNo, pageSize, sort);
        Page<Salary> page = salaryRepository.findAll(pg);
        List<SalaryDTO> content = page.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        PageResponse<SalaryDTO> resp = new PageResponse<>();
        resp.setContent(content);
        resp.setPageNo(page.getNumber()); resp.setPageSize(page.getSize());
        resp.setTotalElements(page.getTotalElements()); resp.setTotalPages(page.getTotalPages());
        resp.setLast(page.isLast()); return resp;
    }

    @Override
    public List<SalaryDTO> getSalariesByEmployeeId(Integer empId) {
        return salaryRepository.findByEmployeeEmployeeId(empId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<SalaryDTO> getSalariesByPeriod(Integer month, Integer year) {
        return salaryRepository.findByPeriodMonthAndPeriodYear(month, year)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public SalaryDTO getSalaryByEmployeeAndPeriod(Integer empId, Integer month, Integer year) {
        Salary s = salaryRepository.findByEmployeeEmployeeIdAndPeriodMonthAndPeriodYear(empId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "employeeId & period", empId));
        return mapToDTO(s);
    }

    @Override
    public List<SalaryDTO> getSalariesByPaymentStatus(String status) {
        return salaryRepository.findByPaymentStatus(status)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<SalaryDTO> getSalariesByDepartmentAndPeriod(Integer deptId, Integer month, Integer year) {
        return salaryRepository.findByDepartmentAndPeriod(deptId, month, year)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Double getTotalGrossSalaryByPeriod(Integer month, Integer year) {
        return salaryRepository.getTotalGrossSalaryByPeriod(month, year);
    }

    @Override
    public Double getTotalNetSalaryByPeriod(Integer month, Integer year) {
        return salaryRepository.getTotalNetSalaryByPeriod(month, year);
    }

    @Override
    public Map<String, Double> getSalarySummaryByDepartment(Integer month, Integer year) {
        return salaryRepository.findAll().stream()
                .filter(s -> s.getPeriodMonth().equals(month) && s.getPeriodYear().equals(year))
                .collect(Collectors.groupingBy(
                        sal -> sal.getEmployee().getDepartment().getDepartmentName(),
                        Collectors.summingDouble(sal -> sal.getNetSalary().doubleValue())
                ));
    }

    @Override
    public SalaryDTO updateSalary(Integer id, SalaryRequestDTO req) {
        Salary s = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", id));
        if (req.getBaseSalary() != null) s.setBaseSalary(req.getBaseSalary());
        if (req.getTotalWorkingDays() != null) s.setTotalWorkingDays(req.getTotalWorkingDays());
        if (req.getActualWorkingDays() != null) s.setActualWorkingDays(req.getActualWorkingDays());
        if (req.getOvertimeAmount() != null) s.setOvertimeAmount(req.getOvertimeAmount());
        if (req.getBonus() != null) s.setBonus(req.getBonus());
        if (req.getAllowance() != null) s.setAllowance(req.getAllowance());
        if (req.getGrossSalary() != null) s.setGrossSalary(req.getGrossSalary());
        if (req.getInsuranceAmount() != null) s.setInsuranceAmount(req.getInsuranceAmount());
        if (req.getTaxAmount() != null) s.setTaxAmount(req.getTaxAmount());
        if (req.getAdvanceAmount() != null) s.setAdvanceAmount(req.getAdvanceAmount());
        if (req.getDeductions() != null) s.setDeductions(req.getDeductions());
        if (req.getNetSalary() != null) s.setNetSalary(req.getNetSalary());
        if (req.getPaymentDate() != null) s.setPaymentDate(req.getPaymentDate());
        if (req.getPaymentStatus() != null) s.setPaymentStatus(req.getPaymentStatus());
        return mapToDTO(salaryRepository.save(s));
    }

    @Override
    public SalaryDTO updateSalaryPaymentStatus(Integer id, String status, LocalDateTime paymentDate) {
        Salary s = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", id));
        s.setPaymentStatus(status);
        s.setPaymentDate(paymentDate);
        return mapToDTO(salaryRepository.save(s));
    }

    @Override
    public SalaryDetailDTO addSalaryDetail(SalaryDetailRequestDTO req) {
        Salary s = salaryRepository.findById(req.getSalaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", req.getSalaryId()));
        SalaryDetail d = new SalaryDetail();
        d.setSalary(s);
        d.setDescription(req.getDescription());
        d.setAmount(req.getAmount());
        d = salaryDetailRepository.save(d);
        SalaryDetailDTO dd = new SalaryDetailDTO();
        dd.setSalaryDetailId(d.getSalaryDetailId());
        dd.setSalaryId(s.getSalaryId());
        dd.setDescription(d.getDescription());
        dd.setAmount(d.getAmount());
        return dd;
    }

    @Override
    public SalaryDetailDTO updateSalaryDetail(Integer id, SalaryDetailRequestDTO req) {
        SalaryDetail d = salaryDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalaryDetail", "id", id));
        if (req.getAmount() != null) d.setAmount(req.getAmount());
        if (req.getDescription() != null) d.setDescription(req.getDescription());
        d = salaryDetailRepository.save(d);
        SalaryDetailDTO dd = new SalaryDetailDTO();
        dd.setSalaryDetailId(d.getSalaryDetailId());
        dd.setSalaryId(d.getSalary().getSalaryId());
        dd.setDescription(d.getDescription());
        dd.setAmount(d.getAmount());
        return dd;
    }

    @Override
    public void deleteSalaryDetail(Integer id) {
        SalaryDetail d = salaryDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalaryDetail", "id", id));
        salaryDetailRepository.delete(d);
    }

    @Override
    public void deleteSalary(Integer id) {
        Salary s = salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", id));
        salaryRepository.delete(s);
    }

    @Override
    public void generateMonthlySalaries(Integer month, Integer year) {
        // TODO: implement business logic to create salaries for all active employees
    }

    @Override
    public byte[] generateSalarySlipPdf(Integer salaryId) {
        // TODO: implement PDF generation logic
        return new byte[0];
    }

    @Override
    public byte[] generateMonthlySalaryReportPdf(Integer month, Integer year, Integer departmentId) {
        // TODO: implement PDF report logic
        return new byte[0];
    }
}
