package com.hanoigarment.payroll.service.Imp;

import com.google.common.base.Optional;
import com.hanoigarment.payroll.dto.AttendanceDTO;
import com.hanoigarment.payroll.dto.AttendanceRequestDTO;
import com.hanoigarment.payroll.dto.PageResponse;
import com.hanoigarment.payroll.entity.Attendance;
import com.hanoigarment.payroll.entity.Employee;
import com.hanoigarment.payroll.exception.ResourceNotFoundException;
import com.hanoigarment.payroll.repository.AttendanceRepository;
import com.hanoigarment.payroll.service.AttendanceService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repo;

    public AttendanceServiceImpl(AttendanceRepository repo) {
        this.repo = repo;
    }

    @Override
    public AttendanceDTO createAttendance(AttendanceRequestDTO req) {
        Attendance e = new Attendance();

        Employee emp = new Employee();
        emp.setEmployeeId(req.getEmployeeId());
        e.setEmployee(emp);

        e.setWorkDate(req.getWorkDate());
        e.setCheckIn(req.getCheckIn());
        e.setCheckOut(req.getCheckOut());
        e.setWorkingHours(req.getWorkingHours());
        e.setOvertimeHours(req.getOvertimeHours());
        e.setStatus(req.getStatus());
        e.setNote(req.getNote());

        Attendance saved = repo.save(e);
        return toDto(saved);
    }

    @Override
    public List<AttendanceDTO> createBulkAttendance(List<AttendanceRequestDTO> reqs) {
        List<Attendance> entities = reqs.stream().map(r -> {
            Attendance a = new Attendance();
            Employee emp = new Employee();
            emp.setEmployeeId(r.getEmployeeId());
            a.setEmployee(emp);
            a.setWorkDate(r.getWorkDate());
            a.setCheckIn(r.getCheckIn());
            a.setCheckOut(r.getCheckOut());
            a.setWorkingHours(r.getWorkingHours());
            a.setOvertimeHours(r.getOvertimeHours());
            a.setStatus(r.getStatus());
            a.setNote(r.getNote());
            return a;
        }).collect(Collectors.toList());

        return repo.saveAll(entities).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDTO getAttendanceById(Integer attendanceId) {
        Attendance e = repo.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "attendanceId", attendanceId));
        return toDto(e);
    }

    @Override
    public AttendanceDTO getAttendanceByEmployeeAndDate(Integer employeeId, LocalDate workDate) {
        Optional<Attendance> optionalAttendance = repo.findByEmployeeEmployeeIdAndWorkDate(employeeId, workDate);
        if (!optionalAttendance.isPresent()) {
            throw new ResourceNotFoundException("Attendance", "employeeId+workDate", employeeId + "/" + workDate);
        }
        return toDto(optionalAttendance.get());
    }


    @Override
    public List<AttendanceDTO> getAllAttendances() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AttendanceDTO> getAllAttendancesPaged(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<Attendance> page = repo.findAll(PageRequest.of(pageNo, pageSize, sort));
        List<AttendanceDTO> dtos = page.getContent().stream()
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
    public List<AttendanceDTO> getAttendancesByEmployeeId(Integer employeeId) {
        return repo.findByEmployeeEmployeeId(employeeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate) {
        return repo.findByWorkDateBetween(startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAttendancesByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        return repo.findAttendancesByEmployeeAndDateRange(employeeId, startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countPresentDaysByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        return repo.countPresentDaysByEmployeeAndPeriod(employeeId, startDate, endDate);
    }

    @Override
    public BigDecimal sumWorkingHoursByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = repo.sumWorkingHoursByEmployeeAndPeriod(employeeId, startDate, endDate);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal sumOvertimeHoursByEmployeeAndPeriod(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = repo.sumOvertimeHoursByEmployeeAndPeriod(employeeId, startDate, endDate);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public Map<Integer, BigDecimal> getWorkingDaysSummaryByDepartment(Integer departmentId, LocalDate startDate, LocalDate endDate) {
        // Currently not supported–return empty map or implement custom query
        return Collections.emptyMap();
    }

    @Override
    public AttendanceDTO updateAttendance(Integer attendanceId, AttendanceRequestDTO req) {
        Attendance e = repo.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "attendanceId", attendanceId));

        if (req.getWorkDate() != null)   e.setWorkDate(req.getWorkDate());
        if (req.getCheckIn()  != null)   e.setCheckIn(req.getCheckIn());
        if (req.getCheckOut() != null)   e.setCheckOut(req.getCheckOut());
        if (req.getWorkingHours() != null) e.setWorkingHours(req.getWorkingHours());
        if (req.getOvertimeHours()!= null) e.setOvertimeHours(req.getOvertimeHours());
        if (req.getStatus()    != null)   e.setStatus(req.getStatus());
        if (req.getNote()      != null)   e.setNote(req.getNote());

        return toDto(repo.save(e));
    }

    @Override
    public void deleteAttendance(Integer attendanceId) {
        if (!repo.existsById(attendanceId)) {
            throw new ResourceNotFoundException("Attendance", "attendanceId", attendanceId);
        }
        repo.deleteById(attendanceId);
    }

    @Override
    public void generateMonthlyAttendanceReport(Integer month, Integer year) {
        // TODO: implement report generation
    }

    private AttendanceDTO toDto(Attendance e) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setAttendanceId(e.getAttendanceId());
        dto.setEmployeeId(e.getEmployee().getEmployeeId());
        dto.setEmployeeCode(e.getEmployee().getEmployeeCode());
        dto.setWorkDate(e.getWorkDate());
        dto.setCheckIn(e.getCheckIn());
        dto.setCheckOut(e.getCheckOut());
        dto.setWorkingHours(e.getWorkingHours());
        dto.setOvertimeHours(e.getOvertimeHours());
        dto.setStatus(e.getStatus());
        dto.setNote(e.getNote());
        return dto;
    }
}