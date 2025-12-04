package com.example.Hospital.controller;

import com.example.Hospital.service.AdminShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/shifts")
public class AdminShiftController {

    @Autowired
    private AdminShiftService adminShiftService;

    @GetMapping
    public Map<String, Object> getSchedules(@RequestParam(required = false) String mode,
                                            @RequestParam(required = false) String date,
                                            @RequestParam(required = false) Integer departmentId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "100") int size) {
        return adminShiftService.getSchedules(mode, date, departmentId, page, size);
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary(@RequestParam(required = false) String date,
                                          @RequestParam(required = false) Integer departmentId) {
        return adminShiftService.getSummary(date, departmentId);
    }

    @GetMapping("/shift-frames")
    public List<Map<String, Object>> getShiftFramesFromShifts() {
        return adminShiftService.getShiftFrames();
    }

    @GetMapping("/doctors")
    public List<Map<String, Object>> getShiftDoctors() {
        return adminShiftService.getDoctors();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getScheduleById(@PathVariable Integer id) {
        return adminShiftService.getScheduleById(id);
    }

    @PostMapping
    public Map<String, Object> createSchedule(@RequestBody Map<String, Object> body) {
        return adminShiftService.createSchedule(body);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateSchedule(@PathVariable Integer id,
                                              @RequestBody Map<String, Object> body) {
        return adminShiftService.updateSchedule(id, body);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Integer id) {
        adminShiftService.deleteSchedule(id);
    }

    @PutMapping("/{id}/confirm")
    public Map<String, Object> confirmShift(@PathVariable Integer id,
                                            @RequestParam(defaultValue = "true") boolean confirmed) {
        return adminShiftService.confirmShift(id, confirmed);
    }
}


