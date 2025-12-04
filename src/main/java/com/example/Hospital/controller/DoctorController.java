package com.example.Hospital.controller;

import com.example.Hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/profile")
    public Map<String, Object> getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getProfile(authHeader);
    }

    @GetMapping("/dashboard/stats")
    public Map<String, Object> getDashboardStats(
            HttpServletRequest request,
            @RequestParam(required = false) String date) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getDashboardStats(authHeader, date);
    }

    @GetMapping("/appointments")
    public Map<String, Object> getAppointments(
            HttpServletRequest request,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getAppointments(authHeader, status, date, page, size);
    }

    @GetMapping("/appointments/stats")
    public Map<String, Object> getAppointmentStats(
            HttpServletRequest request,
            @RequestParam(required = false) String date) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getAppointmentStats(authHeader, date);
    }

    @GetMapping("/prescriptions/stats")
    public Map<String, Object> getPrescriptionStats(
            HttpServletRequest request,
            @RequestParam(required = false) String date) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPrescriptionStats(authHeader, date);
    }

    @GetMapping("/prescriptions")
    public Map<String, Object> getPrescriptions(
            HttpServletRequest request,
            @RequestParam(required = false) Integer patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPrescriptions(authHeader, patientId, page, size);
    }

    @GetMapping("/prescriptions/{id}")
    public Map<String, Object> getPrescriptionDetail(
            HttpServletRequest request,
            @PathVariable Integer id) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPrescriptionDetail(authHeader, id);
    }

    @PostMapping("/prescriptions")
    public Map<String, Object> createPrescription(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.createPrescription(authHeader, body);
    }

    @GetMapping("/medicines")
    public Map<String, Object> getMedicines(
            HttpServletRequest request,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getMedicines(authHeader, search, page, size);
    }

    @GetMapping("/bills")
    public Map<String, Object> getBills(
            HttpServletRequest request,
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getBills(authHeader, patientId, status, page, size);
    }

    @PutMapping("/appointments/{id}/approve")
    public Map<String, Object> approveAppointment(
            HttpServletRequest request,
            @PathVariable Integer id) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.approveAppointment(authHeader, id);
    }

    @PutMapping("/appointments/{id}/reject")
    public Map<String, Object> rejectAppointment(
            HttpServletRequest request,
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        String reason = body != null ? (String) body.getOrDefault("reason", "") : "";
        return doctorService.rejectAppointment(authHeader, id, reason);
    }

    @PutMapping("/appointments/{id}/complete")
    public Map<String, Object> completeAppointment(
            HttpServletRequest request,
            @PathVariable Integer id) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.completeAppointment(authHeader, id);
    }

    @GetMapping("/schedule")
    public Object getSchedule(
            HttpServletRequest request,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getSchedule(authHeader, startDate, endDate);
    }

    @PutMapping("/schedule")
    public Map<String, Object> updateSchedule(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.upsertSchedule(authHeader, body);
    }

    // ===== Patients & stats =====

    @GetMapping("/patients/stats")
    public Map<String, Object> getPatientStats(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPatientStats(authHeader);
    }

    @GetMapping("/patients")
    public Map<String, Object> getPatients(
            HttpServletRequest request,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String appointmentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPatients(authHeader, search, appointmentStatus, page, size);
    }

    @GetMapping("/patients/{id}/medical-history")
    public Map<String, Object> getPatientMedicalHistory(
            HttpServletRequest request,
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPatientMedicalHistory(authHeader, id, page, size);
    }

    @GetMapping("/medical-records")
    public Map<String, Object> getMedicalRecords(
            HttpServletRequest request,
            @RequestParam(required = false) Integer patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getMedicalRecords(authHeader, patientId, page, size);
    }

    @PostMapping("/medical-records")
    public Map<String, Object> createMedicalRecord(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.createMedicalRecord(authHeader, body);
    }

    @GetMapping("/appointments/by-patient")
    public Map<String, Object> getAppointmentsByPatient(
            HttpServletRequest request,
            @RequestParam Integer patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getPatientAppointments(authHeader, patientId, page, size);
    }

    // ===== Lab tests & surgeries =====

    @GetMapping("/lab-tests")
    public Object getLabTests(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getLabTests(authHeader);
    }

    @PostMapping("/lab-tests")
    public Map<String, Object> createLabTest(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.createLabTest(authHeader, body);
    }

    @PutMapping("/lab-tests/{id}/results")
    public Map<String, Object> updateLabTestResult(
            HttpServletRequest request,
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.updateLabTestResult(authHeader, id, body);
    }

    @GetMapping("/surgeries/requests")
    public Object getSurgeryRequests(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getSurgeryRequests(authHeader);
    }

    @PostMapping("/surgeries/request")
    public Map<String, Object> createSurgeryRequest(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.createSurgeryRequest(authHeader, body);
    }

    @GetMapping("/surgeries/schedule")
    public Object getSurgerySchedule(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return doctorService.getSurgerySchedule(authHeader);
    }
}



