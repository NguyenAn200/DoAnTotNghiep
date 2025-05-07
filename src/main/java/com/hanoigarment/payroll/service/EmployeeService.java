package com.hanoigarment.payroll.service;

import com.hanoigarment.payroll.dto.EmployeeDTO;
import com.hanoigarment.payroll.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    Optional<EmployeeDTO> getEmployeeById(Integer id);
    Optional<EmployeeDTO> getEmployeeByCode(String code);
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO);
    void deleteEmployee(Integer id);
}

