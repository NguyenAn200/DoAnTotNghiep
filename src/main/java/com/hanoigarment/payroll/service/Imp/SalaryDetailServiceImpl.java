package com.hanoigarment.payroll.service.Imp;

import com.hanoigarment.payroll.dto.SalaryDetailDTO;
import com.hanoigarment.payroll.dto.SalaryDetailRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.Salary;
import com.hanoigarment.payroll.entity.SalaryDetail;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.SalaryDetailRepository;
import com.hanoigarment.payroll.repository.SalaryRepository;
import com.hanoigarment.payroll.service.SalaryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalaryDetailServiceImpl implements SalaryDetailService {

    private final SalaryDetailRepository detailRepo;
    private final SalaryRepository salaryRepo;

    @Autowired
    public SalaryDetailServiceImpl(SalaryDetailRepository detailRepo, SalaryRepository salaryRepo) {
        this.detailRepo = detailRepo;
        this.salaryRepo = salaryRepo;
    }

    private SalaryDetailDTO mapToDTO(SalaryDetail d) {
        SalaryDetailDTO dto = new SalaryDetailDTO();
        dto.setSalaryDetailId(d.getSalaryDetailId());
        dto.setSalaryId(d.getSalary().getSalaryId());
        dto.setItemName(d.getItemName());
        dto.setItemType(d.getItemType());
        dto.setAmount(d.getAmount());
        dto.setDescription(d.getDescription());
        Salary s = d.getSalary();
        dto.setEmployeeId(s.getEmployee().getEmployeeId());
        dto.setEmployeeName(s.getEmployee().getFullName());
        dto.setPeriodMonth(s.getPeriodMonth());
        dto.setPeriodYear(s.getPeriodYear());
        return dto;
    }

    @Override
    public SalaryDetailDTO createSalaryDetail(SalaryDetailRequestDTO req) {
        Salary s = salaryRepo.findById(req.getSalaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Salary", "id", req.getSalaryId()));
        SalaryDetail d = new SalaryDetail();
        d.setSalary(s);
        d.setItemName(req.getItemName());
        d.setItemType(req.getItemType());
        d.setAmount(req.getAmount());
        d.setDescription(req.getDescription());
        d = detailRepo.save(d);
        return mapToDTO(d);
    }

    @Override
    public SalaryDetailDTO getSalaryDetailById(Integer id) {
        SalaryDetail d = detailRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalaryDetail", "id", id));
        return mapToDTO(d);
    }

    @Override
    public List<SalaryDetailDTO> getAllSalaryDetails() {
        return detailRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SalaryDetailDTO> getAllSalaryDetailsPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pg = PageRequest.of(pageNo, pageSize, sort);
        Page<SalaryDetail> page = detailRepo.findAll(pg);
        List<SalaryDetailDTO> content = page.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        PageResponse<SalaryDetailDTO> resp = new PageResponse<>();
        resp.setContent(content);
        resp.setPageNo(page.getNumber());
        resp.setPageSize(page.getSize());
        resp.setTotalElements(page.getTotalElements());
        resp.setTotalPages(page.getTotalPages());
        resp.setLast(page.isLast());
        return resp;
    }

    @Override
    public List<SalaryDetailDTO> getSalaryDetailsBySalaryId(Integer salaryId) {
        return detailRepo.findBySalarySalaryId(salaryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryDetailDTO> getSalaryDetailsByItemType(String itemType) {
        return detailRepo.findByItemType(itemType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryDetailDTO> getSalaryDetailsByEmployeeAndPeriod(Integer employeeId, Integer month, Integer year) {
        return detailRepo.findByEmployeeAndPeriod(employeeId, month, year).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaryDetailDTO> getSalaryDetailsByEmployeeAndItemTypeAndPeriod(Integer employeeId, String itemType, Integer month, Integer year) {
        return detailRepo.findByEmployeeAndItemTypeAndPeriod(employeeId, itemType, month, year).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SalaryDetailDTO updateSalaryDetail(Integer id, SalaryDetailRequestDTO req) {
        SalaryDetail d = detailRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalaryDetail", "id", id));
        if (req.getItemName() != null) d.setItemName(req.getItemName());
        if (req.getItemType() != null) d.setItemType(req.getItemType());
        if (req.getAmount() != null) d.setAmount(req.getAmount());
        if (req.getDescription() != null) d.setDescription(req.getDescription());
        d = detailRepo.save(d);
        return mapToDTO(d);
    }

    @Override
    public void deleteSalaryDetail(Integer id) {
        SalaryDetail d = detailRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalaryDetail", "id", id));
        detailRepo.delete(d);
    }
}
