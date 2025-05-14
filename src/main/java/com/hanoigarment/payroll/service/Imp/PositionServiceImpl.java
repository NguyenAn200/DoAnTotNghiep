package com.hanoigarment.payroll.service.Imp;


import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.dto.PositionDTO;
import com.hanoigarment.payroll.dto.PositionRequestDTO;

import com.hanoigarment.payroll.entity.Department;
import com.hanoigarment.payroll.entity.Position;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.DepartmentRepository;
import com.hanoigarment.payroll.repository.PositionRepository;
import com.hanoigarment.payroll.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, DepartmentRepository departmentRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    // Utility method to convert Position entity to PositionDTO
    private PositionDTO convertToDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setPositionId(position.getPositionId());
        positionDTO.setPositionName(position.getPositionName());
        positionDTO.setBaseSalary(position.getBaseSalary());
        positionDTO.setDescription(position.getDescription());
        return positionDTO;
    }

    @Override
    public PositionDTO createPosition(PositionRequestDTO requestDTO) {
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", requestDTO.getDepartmentId()));

        Position position = new Position();
        position.setPositionName(requestDTO.getPositionName());
        position.setBaseSalary(requestDTO.getBaseSalary());
        position.setDescription(requestDTO.getDescription());
        position.setIsActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : true);
        position.setDepartment(department);

        Position savedPosition = positionRepository.save(position);
        return convertToDTO(savedPosition);
    }

    @Override
    public PositionDTO getPositionById(Integer positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        return convertToDTO(position);
    }

    @Override
    public PositionDTO getPositionByIdWithEmployees(Integer positionId) {
        Position position = positionRepository.findByIdWithEmployees(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        return convertToDTO(position);
    }

    @Override
    public PositionDTO getPositionByName(String positionName) {
        Position position = positionRepository.findByPositionName(positionName)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "name", positionName));
        return convertToDTO(position);
    }

    @Override
    public List<PositionDTO> getAllPositions() {
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PositionDTO> getAllActivePositions() {
        List<Position> positions = positionRepository.findAllActivePositions();
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PositionDTO> getPositionsByActiveFlag(Boolean isActive) {
        // nếu isActive == null, có thể trả về tất cả
        if (isActive == null) {
            return getAllPositions();
        }
        List<Position> positions = positionRepository.findByIsActive(isActive);
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public PageResponse<PositionDTO> getAllPositionsPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Position> positions = positionRepository.findAll(pageable);

        List<PositionDTO> content = positions.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PageResponse<PositionDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(content);
        pageResponse.setPageNo(positions.getNumber());
        pageResponse.setPageSize(positions.getSize());
        pageResponse.setTotalElements(positions.getTotalElements());
        pageResponse.setTotalPages(positions.getTotalPages());
        pageResponse.setLast(positions.isLast());

        return pageResponse;
    }

    @Override
    public List<PositionDTO> getPositionsByDepartmentId(Integer departmentId) {
        // Check if department exists
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        List<Position> positions = positionRepository.findByDepartmentDepartmentId(departmentId);
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PositionDTO> getActivePositionsByDepartmentId(Integer departmentId) {
        // Check if department exists
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        List<Position> positions = positionRepository.findActivePositionsByDepartmentId(departmentId);
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PositionDTO updatePosition(Integer positionId, PositionRequestDTO requestDTO) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));

        Department department = null;
        if (requestDTO.getDepartmentId() != null) {
            department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", requestDTO.getDepartmentId()));
        }

        if (requestDTO.getPositionName() != null) {
            position.setPositionName(requestDTO.getPositionName());
        }

        if (requestDTO.getBaseSalary() != null) {
            position.setBaseSalary(requestDTO.getBaseSalary());
        }

        if (requestDTO.getDescription() != null) {
            position.setDescription(requestDTO.getDescription());
        }

        if (requestDTO.getIsActive() != null) {
            position.setIsActive(requestDTO.getIsActive());
        }

        if (department != null) {
            position.setDepartment(department);
        }

        Position updatedPosition = positionRepository.save(position);
        return convertToDTO(updatedPosition);
    }

    @Override
    public PositionDTO activatePosition(Integer positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        position.setIsActive(true);
        Position savedPosition = positionRepository.save(position);
        return convertToDTO(savedPosition);
    }

    @Override
    public PositionDTO deactivatePosition(Integer positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        position.setIsActive(false);
        Position savedPosition = positionRepository.save(position);
        return convertToDTO(savedPosition);
    }

    @Override
    public void deletePosition(Integer positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", positionId));
        positionRepository.delete(position);
    }
}