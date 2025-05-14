package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.EmployeeDTO;
import com.hanoigarment.payroll.dto.EmployeeRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeRequestDTO requestDTO);

    EmployeeDTO getEmployeeById(Integer employeeId);

    EmployeeDTO getEmployeeByIdWithAttendances(Integer employeeId);

    EmployeeDTO getEmployeeByIdWithSalaries(Integer employeeId);

    EmployeeDTO getEmployeeByIdWithAdvances(Integer employeeId);

    EmployeeDTO getEmployeeByEmployeeCode(String employeeCode);

    EmployeeDTO getEmployeeByIdCard(String idCard);

    List<EmployeeDTO> getAllEmployees();

    List<EmployeeDTO> getAllActiveEmployees();

    PageResponse<EmployeeDTO> getAllEmployeesPaged(int pageNo, int pageSize, String sortBy, String sortDir);

    List<EmployeeDTO> getEmployeesByDepartmentId(Integer departmentId);

    List<EmployeeDTO> getEmployeesByPositionId(Integer positionId);

    List<EmployeeDTO> getEmployeesByNameContaining(String name);

    List<EmployeeDTO> getEmployeesByJoinDateRange(LocalDate startDate, LocalDate endDate);

    Map<String, Long> getEmployeeCountByDepartment();

    Map<String, Long> getEmployeeCountByPosition();

    EmployeeDTO updateEmployee(Integer employeeId, EmployeeRequestDTO requestDTO);

    EmployeeDTO activateEmployee(Integer employeeId);

    EmployeeDTO deactivateEmployee(Integer employeeId);

    void deleteEmployee(Integer employeeId);
}

