package com.example.Hospital.controller;

import com.example.Hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    private String resolveIdentifier(HttpServletRequest request, Principal principal) {
        // Ưu tiên dùng Authorization header (Bearer token) cho thống nhất với các module khác
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank()) {
            return authHeader;
        }
        if (principal != null && principal.getName() != null) {
            return principal.getName();
        }
        String email = request.getHeader("X-User-Email");
        return email != null ? email : "";
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(HttpServletRequest request, Principal principal) {
        String id = resolveIdentifier(request, principal);
        return patientService.getProfile(id);
    }

    @PutMapping("/profile")
    public Map<String, Object> updateProfile(HttpServletRequest request,
                                             Principal principal,
                                             @RequestBody Map<String, Object> body) {
        String id = resolveIdentifier(request, principal);
        return patientService.updateProfile(id, body);
    }

    @GetMapping("/doctors")
    public Map<String, Object> getDoctors(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "6") int size) {
        return patientService.getDoctors(page, size);
    }

    @GetMapping("/appointments")
    public List<Map<String, Object>> getAppointments(HttpServletRequest request, Principal principal) {
        String id = resolveIdentifier(request, principal);
        return patientService.getAppointments(id);
    }

    @PostMapping("/appointments")
    public Map<String, Object> bookAppointment(HttpServletRequest request,
                                               Principal principal,
                                               @RequestBody Map<String, Object> body) {
        String id = resolveIdentifier(request, principal);
        return patientService.bookAppointment(id, body);
    }

    @PutMapping("/appointments/{id}")
    public void updateAppointment(@PathVariable Integer id,
                                  HttpServletRequest request,
                                  Principal principal,
                                  @RequestBody Map<String, Object> body) {
        String identifier = resolveIdentifier(request, principal);
        patientService.updateAppointment(identifier, id, body);
    }

    @PutMapping("/appointments/{id}/cancel")
    public void cancelAppointment(@PathVariable Integer id,
                                  HttpServletRequest request,
                                  Principal principal) {
        String identifier = resolveIdentifier(request, principal);
        patientService.cancelAppointment(identifier, id);
    }

    @GetMapping("/prescriptions")
    public List<Map<String, Object>> getPrescriptions(HttpServletRequest request, Principal principal) {
        String id = resolveIdentifier(request, principal);
        return patientService.getPrescriptions(id);
    }

    @GetMapping("/prescriptions/{id}")
    public Map<String, Object> getPrescriptionDetail(@PathVariable Integer id,
                                                     HttpServletRequest request,
                                                     Principal principal) {
        String identifier = resolveIdentifier(request, principal);
        return patientService.getPrescriptionDetail(id, identifier);
    }

    @GetMapping("/bills")
    public List<Map<String, Object>> getBills(HttpServletRequest request, Principal principal) {
        String id = resolveIdentifier(request, principal);
        return patientService.getBills(id);
    }

    @GetMapping("/lab-results")
    public List<Map<String, Object>> getLabResults(HttpServletRequest request, Principal principal) {
        String id = resolveIdentifier(request, principal);
        return patientService.getLabResults(id);
    }

    @GetMapping("/medical-history")
    public List<Map<String, Object>> getMedicalHistory(HttpServletRequest request, Principal principal) {
        String id = resolveIdentifier(request, principal);
        return patientService.getMedicalHistory(id);
    }
}


