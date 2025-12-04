package com.example.Hospital.controller;

import com.example.Hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ===================== USER MANAGEMENT =====================

    @GetMapping("/users")
    public Map<String, Object> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return adminService.getUsers(page, size);
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> getUserById(@PathVariable Integer id) {
        return adminService.getUserById(id);
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> body) {
        return adminService.createUser(body);
    }

    @PutMapping("/users/{id}")
    public Map<String, Object> updateUser(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return adminService.updateUser(id, body);
    }

    @PutMapping("/users/{id}/toggle-status")
    public Map<String, Object> toggleUserStatus(@PathVariable Integer id) {
        return adminService.toggleUserStatus(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        adminService.deleteUser(id);
    }

    // ===================== DOCTOR MANAGEMENT =====================

    @GetMapping("/doctors")
    public Map<String, Object> getDoctors(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) Integer phongbanId) {
        return adminService.getDoctors(page, size, search, position, phongbanId);
    }

    @GetMapping("/doctors/by-date")
    public Map<String, Object> getDoctorsByDate(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) Integer phongbanId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return adminService.getDoctorsByDate(page, size, search, position, phongbanId, startDate, endDate);
    }

    @GetMapping("/doctors/{id}")
    public Map<String, Object> getDoctorById(@PathVariable Integer id) {
        return adminService.getDoctorById(id);
    }

    @PostMapping("/doctors")
    public Map<String, Object> createDoctor(@RequestBody Map<String, Object> body) {
        return adminService.createDoctor(body);
    }

    @PutMapping("/doctors/{id}")
    public Map<String, Object> updateDoctor(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return adminService.updateDoctor(id, body);
    }

    @DeleteMapping("/doctors/{id}")
    public void deleteDoctor(@PathVariable Integer id) {
        adminService.deleteDoctor(id);
    }

    @GetMapping("/doctors/count")
    public Map<String, Object> countDoctors() {
        return adminService.countDoctors();
    }

    // ===================== STAFF MANAGEMENT =====================

    @GetMapping("/staff")
    public Map<String, Object> getStaff(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String chucVu,
            @RequestParam(required = false) Integer phongbanId) {
        return adminService.getStaff(page, size, search, chucVu, phongbanId);
    }

    @GetMapping("/staff/by-date")
    public Map<String, Object> getStaffByDate(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String chucVu,
            @RequestParam(required = false) Integer phongbanId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return adminService.getStaffByDate(page, size, search, chucVu, phongbanId, startDate, endDate);
    }

    @GetMapping("/staff/{id}")
    public Map<String, Object> getStaffById(@PathVariable Integer id) {
        return adminService.getStaffById(id);
    }

    @PostMapping("/staff")
    public Map<String, Object> createStaff(@RequestBody Map<String, Object> body) {
        return adminService.createStaff(body);
    }

    @PutMapping("/staff/{id}")
    public Map<String, Object> updateStaff(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return adminService.updateStaff(id, body);
    }

    @DeleteMapping("/staff/{id}")
    public void deleteStaff(@PathVariable Integer id) {
        adminService.deleteStaff(id);
    }

    @GetMapping("/staff/count")
    public Map<String, Object> countStaff() {
        return adminService.countStaff();
    }

    @GetMapping("/nurses/count")
    public Map<String, Object> countNurses() {
        return adminService.countNurses();
    }

    // ===================== DEPARTMENT MANAGEMENT =====================

    @GetMapping("/departments")
    public List<Map<String, Object>> getDepartments() {
        return adminService.getDepartments();
    }

    @GetMapping("/departments/{id}")
    public Map<String, Object> getDepartmentById(@PathVariable Integer id) {
        return adminService.getDepartmentById(id);
    }

    @PostMapping("/departments")
    public Map<String, Object> createDepartment(@RequestBody Map<String, Object> body) {
        return adminService.createDepartment(body);
    }

    @PutMapping("/departments/{id}")
    public Map<String, Object> updateDepartment(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return adminService.updateDepartment(id, body);
    }

    @DeleteMapping("/departments/{id}")
    public void deleteDepartment(@PathVariable Integer id) {
        adminService.deleteDepartment(id);
    }

    // ===================== MEDICINE MANAGEMENT =====================

    @GetMapping("/medicines")
    public Map<String, Object> getMedicines(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String nhaSanXuat,
            @RequestParam(required = false) String nhomThuoc,
            @RequestParam(required = false) String dangBaoChe,
            @RequestParam(required = false) String expiringBefore,
            @RequestParam(required = false) String sort) {
        String sortField = "thuocId";
        String sortDir = "asc";
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            if (parts.length > 0) {
                sortField = parts[0].trim();
            }
            if (parts.length > 1) {
                sortDir = parts[1].trim();
            }
        }
        return adminService.getMedicines(page, size, search, nhaSanXuat, nhomThuoc, dangBaoChe, expiringBefore, sortField, sortDir);
    }

    @GetMapping("/medicines/stats")
    public Map<String, Object> getMedicineStats() {
        return adminService.getMedicineStats();
    }

    @GetMapping("/medicines/dosage-forms")
    public List<String> getDosageForms() {
        return adminService.getDosageForms();
    }

    @GetMapping("/medicines/groups")
    public List<String> getGroups() {
        return adminService.getGroups();
    }

    // ===================== PATIENT MANAGEMENT =====================

    @GetMapping("/patients")
    public Map<String, Object> getPatients(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String status) {
        return adminService.getPatients(page, size, search, gender, status);
    }

    @GetMapping("/patients/stats")
    public Map<String, Object> getPatientStats() {
        return adminService.getPatientStats();
    }

    // ===================== PRESCRIPTION MANAGEMENT =====================

    @GetMapping("/prescriptions")
    public Map<String, Object> getPrescriptions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer patientId) {
        return adminService.getPrescriptions(page, size, search, doctorId, patientId);
    }

    @GetMapping("/prescriptions/{id}")
    public Map<String, Object> getPrescriptionDetail(@PathVariable Integer id) {
        return adminService.getPrescriptionDetail(id);
    }

    // ===================== LAB TEST MANAGEMENT =====================

    @GetMapping("/lab-tests")
    public Map<String, Object> getLabTests(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer patientId) {
        return adminService.getLabTests(page, size, search, doctorId, patientId);
    }

    @GetMapping("/lab-tests/{id}")
    public Map<String, Object> getLabTestDetail(@PathVariable Integer id) {
        return adminService.getLabTestDetail(id);
    }

    // ===================== INVOICE MANAGEMENT =====================

    @GetMapping("/invoices")
    public Map<String, Object> getInvoices(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return adminService.getInvoices(page, size, search, status);
    }

    @GetMapping("/invoices/{id}")
    public Map<String, Object> getInvoiceDetail(@PathVariable Integer id) {
        return adminService.getInvoiceDetail(id);
    }

    @GetMapping("/invoices/stats")
    public Map<String, Object> getInvoiceStats() {
        return adminService.getInvoiceStats();
    }
}


