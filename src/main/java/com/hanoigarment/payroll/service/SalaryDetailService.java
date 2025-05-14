package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.SalaryDetailDTO;
import com.hanoigarment.payroll.dto.SalaryDetailRequestDTO;

import java.util.List;

public interface SalaryDetailService {
    SalaryDetailDTO createSalaryDetail(SalaryDetailRequestDTO requestDTO);

    SalaryDetailDTO getSalaryDetailById(Integer salaryDetailId);

    List<SalaryDetailDTO> getAllSalaryDetails();

    PageResponse<SalaryDetailDTO> getAllSalaryDetailsPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<SalaryDetailDTO> getSalaryDetailsBySalaryId(Integer salaryId);

    List<SalaryDetailDTO> getSalaryDetailsByItemType(String itemType);

    List<SalaryDetailDTO> getSalaryDetailsByEmployeeAndPeriod(Integer employeeId, Integer month, Integer year);

    List<SalaryDetailDTO> getSalaryDetailsByEmployeeAndItemTypeAndPeriod(
            Integer employeeId, String itemType, Integer month, Integer year);

    SalaryDetailDTO updateSalaryDetail(Integer salaryDetailId, SalaryDetailRequestDTO requestDTO);

    void deleteSalaryDetail(Integer salaryDetailId);
}
