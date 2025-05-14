package com.hanoigarment.payroll.service.Imp;

import com.hanoigarment.payroll.dto.EmployeeDTO;
import com.hanoigarment.payroll.dto.EmployeeRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.Department;
import com.hanoigarment.payroll.entity.Employee;
import com.hanoigarment.payroll.entity.Position;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.DepartmentRepository;
import com.hanoigarment.payroll.repository.EmployeeRepository;
import com.hanoigarment.payroll.repository.PositionRepository;
import com.hanoigarment.payroll.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    private EmployeeDTO mapToDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(e.getEmployeeId());
        dto.setEmployeeCode(e.getEmployeeCode());
        dto.setFullName(e.getFullName());
        dto.setDateOfBirth(e.getDateOfBirth());
        dto.setGender(e.getGender());
        dto.setIdCard(e.getIdCard());
        dto.setJoinDate(e.getJoinDate());
        dto.setAddress(e.getAddress());
        dto.setPhone(e.getPhone());
        dto.setEmail(e.getEmail());
        dto.setBankAccount(e.getBankAccount());
        dto.setBankName(e.getBankName());
        dto.setIsActive(e.getIsActive());
        Department dept = e.getDepartment();
        if (dept != null) {
            dto.setDepartmentId(dept.getDepartmentId());
            dto.setDepartmentName(dept.getDepartmentName());
        }
        Position pos = e.getPosition();
        if (pos != null) {
            dto.setPositionId(pos.getPositionId());
            dto.setPositionName(pos.getPositionName());
        }
        return dto;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeRequestDTO req) {
        Department dept = departmentRepository.findById(req.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartmentId()));
        Position pos = positionRepository.findById(req.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Position", "id", req.getPositionId()));

        Employee e = new Employee();
        e.setEmployeeCode(req.getEmployeeCode());
        e.setFullName(req.getFullName());
        e.setDateOfBirth(req.getDateOfBirth());
        e.setGender(req.getGender());
        e.setIdCard(req.getIdCard());
        e.setJoinDate(req.getJoinDate());
        e.setAddress(req.getAddress());
        e.setPhone(req.getPhone());
        e.setEmail(req.getEmail());
        e.setBankAccount(req.getBankAccount());
        e.setBankName(req.getBankName());
        e.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        e.setDepartment(dept);
        e.setPosition(pos);

        return mapToDTO(employeeRepository.save(e));
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToDTO(e);
    }

    @Override
    public EmployeeDTO getEmployeeByIdWithAttendances(Integer id) {
        Employee e = employeeRepository.findByIdWithAttendances(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToDTO(e);
    }

    @Override
    public EmployeeDTO getEmployeeByIdWithSalaries(Integer id) {
        Employee e = employeeRepository.findByIdWithSalaries(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToDTO(e);
    }

    @Override
    public EmployeeDTO getEmployeeByIdWithAdvances(Integer id) {
        Employee e = employeeRepository.findByIdWithAdvances(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToDTO(e);
    }

    @Override
    public EmployeeDTO getEmployeeByEmployeeCode(String code) {
        Employee e = employeeRepository.findByEmployeeCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "code", code));
        return mapToDTO(e);
    }

    @Override
    public EmployeeDTO getEmployeeByIdCard(String idCard) {
        Employee e = employeeRepository.findByIdCard(idCard)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "idCard", idCard));
        return mapToDTO(e);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getAllActiveEmployees() {
        return employeeRepository.findAllActiveEmployees()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<EmployeeDTO> getAllEmployeesPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pg = PageRequest.of(pageNo, pageSize, sort);
        Page<Employee> page = employeeRepository.findAll(pg);

        List<EmployeeDTO> content = page.getContent()
                .stream().map(this::mapToDTO).collect(Collectors.toList());

        PageResponse<EmployeeDTO> resp = new PageResponse<>();
        resp.setContent(content);
        resp.setPageNo(page.getNumber());
        resp.setPageSize(page.getSize());
        resp.setTotalElements(page.getTotalElements());
        resp.setTotalPages(page.getTotalPages());
        resp.setLast(page.isLast());
        return resp;
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartmentId(Integer deptId) {
        if (!departmentRepository.existsById(deptId)) {
            throw new ResourceNotFoundException("Department", "id", deptId);
        }
        return employeeRepository.findByDepartmentDepartmentId(deptId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPositionId(Integer posId) {
        if (!positionRepository.existsById(posId)) {
            throw new ResourceNotFoundException("Position", "id", posId);
        }
        return employeeRepository.findByPositionPositionId(posId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByNameContaining(String name) {
        return employeeRepository.findByFullNameContainingIgnoreCase(name)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByJoinDateRange(LocalDate start, LocalDate end) {
        return employeeRepository.findByJoinDateBetween(start, end)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getEmployeeCountByDepartment() {
        return employeeRepository.findAll()
                .stream()
                .filter(e -> e.getDepartment() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getDepartment().getDepartmentName(),
                        Collectors.counting()
                ));
    }

    @Override
    public Map<String, Long> getEmployeeCountByPosition() {
        return employeeRepository.findAll()
                .stream()
                .filter(e -> e.getPosition() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getPosition().getPositionName(),
                        Collectors.counting()
                ));
    }

    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeRequestDTO req) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        if (req.getEmployeeCode() != null) e.setEmployeeCode(req.getEmployeeCode());
        if (req.getFullName()      != null) e.setFullName(req.getFullName());
        if (req.getDateOfBirth()   != null) e.setDateOfBirth(req.getDateOfBirth());
        if (req.getGender()        != null) e.setGender(req.getGender());
        if (req.getIdCard()        != null) e.setIdCard(req.getIdCard());
        if (req.getJoinDate()      != null) e.setJoinDate(req.getJoinDate());
        if (req.getAddress()       != null) e.setAddress(req.getAddress());
        if (req.getPhone()         != null) e.setPhone(req.getPhone());
        if (req.getEmail()         != null) e.setEmail(req.getEmail());
        if (req.getBankAccount()   != null) e.setBankAccount(req.getBankAccount());
        if (req.getBankName()      != null) e.setBankName(req.getBankName());
        if (req.getIsActive()      != null) e.setIsActive(req.getIsActive());

        if (req.getDepartmentId() != null) {
            Department d = departmentRepository.findById(req.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartmentId()));
            e.setDepartment(d);
        }
        if (req.getPositionId() != null) {
            Position p = positionRepository.findById(req.getPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Position", "id", req.getPositionId()));
            e.setPosition(p);
        }

        return mapToDTO(employeeRepository.save(e));
    }

    @Override
    public EmployeeDTO activateEmployee(Integer id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        e.setIsActive(true);
        return mapToDTO(employeeRepository.save(e));
    }

    @Override
    public EmployeeDTO deactivateEmployee(Integer id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        e.setIsActive(false);
        return mapToDTO(employeeRepository.save(e));
    }

    @Override
    public void deleteEmployee(Integer id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(e);
    }
}
