package com.hanoigarment.payroll.service.Imp;

import com.hanoigarment.payroll.dto.DepartmentDTO;
import com.hanoigarment.payroll.entity.Department;
import com.hanoigarment.payroll.repository.DepartmentRepository;
import com.hanoigarment.payroll.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    public Optional<DepartmentDTO> getDepartmentById(Integer id) {
        return departmentRepository.findById(id).map(this::toDTO);
    }
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department e = toEntity(dto);
        return toDTO(departmentRepository.save(e));
    }
    @Override
    public DepartmentDTO updateDepartment(Integer id, DepartmentDTO dto) {
        Department e = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        e.setDepartmentName(dto.getDepartmentName()); e.setDescription(dto.getDescription()); e.setIsActive(dto.getIsActive());
        return toDTO(departmentRepository.save(e));
    }
    @Override
    public void deleteDepartment(Integer id) { departmentRepository.deleteById(id); }
    private DepartmentDTO toDTO(Department e) {
        return new DepartmentDTO(e.getDepartmentId(), e.getDepartmentName(), e.getDescription(), e.getIsActive());
    }
    private Department toEntity(DepartmentDTO dto) {
        Department e = new Department(); e.setDepartmentName(dto.getDepartmentName()); e.setDescription(dto.getDescription()); e.setIsActive(dto.getIsActive()); return e;
    }
}