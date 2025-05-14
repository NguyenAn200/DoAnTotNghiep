package com.hanoigarment.payroll.service.Imp;

import com.google.common.base.Optional;
import com.hanoigarment.payroll.dto.DepartmentDTO;
import com.hanoigarment.payroll.dto.DepartmentRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.PositionDTO;
import com.hanoigarment.payroll.entity.Department;
import com.hanoigarment.payroll.entity.Position;
import com.hanoigarment.payroll.repository.DepartmentRepository;
import com.hanoigarment.payroll.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDTO createDepartment(DepartmentRequestDTO requestDTO) {
        Department dept = new Department();
        dept.setDepartmentName(requestDTO.getDepartmentName());
        dept.setDescription(requestDTO.getDescription());
        dept.setIsActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : Boolean.TRUE);
        Department saved = departmentRepository.save(dept);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Integer departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + departmentId));
        return mapToDTO(dept);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByIdWithPositions(Integer departmentId) {
        Optional<Department> guavaOpt = departmentRepository.findByIdWithPositions(departmentId);
        Department dept = guavaOpt.isPresent()
                ? guavaOpt.get()
                : null;
        if (dept == null) throw new RuntimeException("Department not found with id " + departmentId);
        DepartmentDTO dto = mapToDTO(dept);
        List<PositionDTO> positions = dept.getPositions().stream()
                .map(this::mapPositionToDTO)
                .collect(Collectors.toList());
        dto.setPositions(positions);
        dto.setPositionCount(positions.size());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByIdWithEmployees(Integer departmentId) {
        Optional<Department> guavaOpt = departmentRepository.findByIdWithEmployees(departmentId);
        Department dept = guavaOpt.isPresent() ? guavaOpt.get() : null;
        if (dept == null) throw new RuntimeException("Department not found with id " + departmentId);
        DepartmentDTO dto = mapToDTO(dept);
        dto.setEmployeeCount(dept.getEmployees().size());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByName(String departmentName) {
        Optional<Department> guavaOpt = departmentRepository.findByDepartmentName(departmentName);
        Department dept = guavaOpt.isPresent() ? guavaOpt.get() : null;
        if (dept == null) throw new RuntimeException("Department not found with name " + departmentName);
        return mapToDTO(dept);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> list = departmentRepository.findAll();
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllActiveDepartments() {
        List<Department> list = departmentRepository.findAllActiveDepartments();
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DepartmentDTO> getAllDepartmentsPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Department> page = departmentRepository.findAll(pageable);
        List<DepartmentDTO> dtos = page.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        return PageResponse.<DepartmentDTO>builder()
                .content(dtos)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public DepartmentDTO updateDepartment(Integer departmentId, DepartmentRequestDTO requestDTO) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + departmentId));
        dept.setDepartmentName(requestDTO.getDepartmentName());
        dept.setDescription(requestDTO.getDescription());
        if (requestDTO.getIsActive() != null) {
            dept.setIsActive(requestDTO.getIsActive());
        }
        Department updated = departmentRepository.save(dept);
        return mapToDTO(updated);
    }

    @Override
    public DepartmentDTO activateDepartment(Integer departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + departmentId));
        dept.setIsActive(true);
        Department updated = departmentRepository.save(dept);
        return mapToDTO(updated);
    }

    @Override
    public DepartmentDTO deactivateDepartment(Integer departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + departmentId));
        dept.setIsActive(false);
        Department updated = departmentRepository.save(dept);
        return mapToDTO(updated);
    }

    @Override
    public void deleteDepartment(Integer departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + departmentId));
        departmentRepository.delete(dept);
    }

    private DepartmentDTO mapToDTO(Department dept) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(dept.getDepartmentId());
        dto.setDepartmentName(dept.getDepartmentName());
        dto.setDescription(dept.getDescription());
        dto.setIsActive(dept.getIsActive());
        dto.setEmployeeCount(dept.getEmployees() != null ? dept.getEmployees().size() : 0);
        dto.setPositionCount(dept.getPositions() != null ? dept.getPositions().size() : 0);
        return dto;
    }

    private PositionDTO mapPositionToDTO(Position pos) {
        PositionDTO dto = new PositionDTO();
        dto.setPositionId(pos.getPositionId());
        dto.setPositionName(pos.getPositionName());
        dto.setDescription(pos.getDescription());
        return dto;
    }
}