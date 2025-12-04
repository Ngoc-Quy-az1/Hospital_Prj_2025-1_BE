package com.example.Hospital.service;

import com.example.Hospital.entity.BacSi;
import com.example.Hospital.entity.KhungGioTrucBan;
import com.example.Hospital.entity.LichTrucBan;
import com.example.Hospital.entity.PhongBan;
import com.example.Hospital.exception.ErrorCode;
import com.example.Hospital.exception.UserException;
import com.example.Hospital.repository.BacSiRepository;
import com.example.Hospital.repository.KhungGioTrucBanRepository;
import com.example.Hospital.repository.LichTrucBanRepository;
import com.example.Hospital.repository.PhongBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminShiftService {

    @Autowired
    private LichTrucBanRepository lichTrucBanRepository;

    @Autowired
    private KhungGioTrucBanRepository khungGioTrucBanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private PhongBanRepository phongBanRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getSchedules(String mode,
                                            String dateStr,
                                            Integer departmentId,
                                            int page,
                                            int size) {
        LocalDate date = parseDate(dateStr);
        LocalDate start;
        LocalDate end;

        if ("month".equalsIgnoreCase(mode)) {
            start = date.withDayOfMonth(1);
            end = start.plusMonths(1).minusDays(1);
        } else if ("week".equalsIgnoreCase(mode)) {
            start = date.minusDays(date.getDayOfWeek().getValue() % 7);
            end = start.plusDays(6);
        } else {
            start = date;
            end = date;
        }

        List<LichTrucBan> all = lichTrucBanRepository.findByNgayTrucBetween(start, end);

        if (departmentId != null) {
            all = all.stream()
                    .filter(l -> l.getPhongban() != null &&
                            departmentId.equals(l.getPhongban().getPhongbanId()))
                    .collect(Collectors.toList());
        }

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<LichTrucBan> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapSchedule)
                .collect(Collectors.toList());

        int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", page);
        result.put("size", size);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSummary(String dateStr, Integer departmentId) {
        LocalDate date = parseDate(dateStr);
        List<LichTrucBan> list;
        if (departmentId != null) {
            list = lichTrucBanRepository.findByPhongban_PhongbanIdAndNgayTruc(departmentId, date);
        } else {
            list = lichTrucBanRepository.findByNgayTruc(date);
        }

        long total = list.size();
        long confirmed = total; // hiện chưa có trạng thái xác nhận, tạm coi tất cả đã xác nhận
        long pending = 0;

        Map<String, Object> result = new HashMap<>();
        result.put("totalShifts", total);
        result.put("confirmedShifts", confirmed);
        result.put("pendingShifts", pending);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getShiftFrames() {
        return khungGioTrucBanRepository.findAll().stream()
                .map(frame -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", frame.getKhunggiotrucId());
                    m.put("khunggiotrucId", frame.getKhunggiotrucId());
                    m.put("khungGioTrucId", frame.getKhunggiotrucId());
                    
                    // Format time properly
                    String timeStr = null;
                    if (frame.getKhunggiotruc() != null) {
                        timeStr = frame.getKhunggiotruc().toString();
                        // Convert LocalTime to HH:mm format if needed
                        if (timeStr.length() > 5) {
                            timeStr = timeStr.substring(0, 5);
                        }
                    }
                    
                    // Since entity only has one time field, use it as start time
                    // End time would need to be calculated or stored separately
                    m.put("gioBatDau", timeStr);
                    m.put("startTime", timeStr);
                    m.put("gioKetThuc", null); // Entity doesn't have end time field
                    m.put("endTime", null);
                    
                    // Generate a name based on time
                    String tenKhung = timeStr != null 
                            ? String.format("Ca trực %s", timeStr)
                            : "Ca trực";
                    m.put("tenKhung", tenKhung);
                    m.put("khungGioLabel", tenKhung);
                    
                    return m;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDoctors() {
        return bacSiRepository.findAll().stream()
                .map(bs -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", bs.getBacsiId());
                    m.put("name", bs.getHoTen());
                    if (bs.getPhongban() != null) {
                        m.put("departmentId", bs.getPhongban().getPhongbanId());
                        m.put("departmentName", bs.getPhongban().getTenPhongban());
                    }
                    m.put("position", "Bác sĩ");
                    return m;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getScheduleById(Integer id) {
        LichTrucBan lich = lichTrucBanRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.LICH_TRUC_NOT_EXISTED));
        return mapSchedule(lich);
    }

    @Transactional
    public Map<String, Object> createSchedule(Map<String, Object> body) {
        LichTrucBan lich = buildOrUpdateEntity(null, body);
        lichTrucBanRepository.save(lich);
        return mapSchedule(lich);
    }

    @Transactional
    public Map<String, Object> updateSchedule(Integer id, Map<String, Object> body) {
        LichTrucBan lich = lichTrucBanRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.LICH_TRUC_NOT_EXISTED));
        buildOrUpdateEntity(lich, body);
        lichTrucBanRepository.save(lich);
        return mapSchedule(lich);
    }

    @Transactional
    public void deleteSchedule(Integer id) {
        if (!lichTrucBanRepository.existsById(id)) {
            throw new UserException(ErrorCode.LICH_TRUC_NOT_EXISTED);
        }
        lichTrucBanRepository.deleteById(id);
    }

    @Transactional
    public Map<String, Object> confirmShift(Integer id, boolean confirmed) {
        // Hiện entity chưa có cờ xác nhận, chỉ trả lại lịch để FE refetch
        LichTrucBan lich = lichTrucBanRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.LICH_TRUC_NOT_EXISTED));
        return mapSchedule(lich);
    }

    private LichTrucBan buildOrUpdateEntity(LichTrucBan lich, Map<String, Object> body) {
        if (lich == null) {
            lich = new LichTrucBan();
        }

        String ngayTrucStr = (String) body.get("ngayTruc");
        if (ngayTrucStr != null && !ngayTrucStr.isBlank()) {
            lich.setNgayTruc(LocalDate.parse(ngayTrucStr, DateTimeFormatter.ISO_DATE));
        }

        Object phongbanIdObj = body.get("phongbanId");
        if (phongbanIdObj != null) {
            Integer phongbanId = toInteger(phongbanIdObj);
            if (phongbanId != null) {
                Optional<PhongBan> pbOpt = phongBanRepository.findById(phongbanId);
                pbOpt.ifPresent(lich::setPhongban);
            }
        }

        Object bacsiIdObj = body.get("bacsiId");
        if (bacsiIdObj != null) {
            Integer bacsiId = toInteger(bacsiIdObj);
            if (bacsiId != null) {
                Optional<BacSi> bsOpt = bacSiRepository.findById(bacsiId);
                bsOpt.ifPresent(lich::setBacsi);
            }
        }

        Object khungIdObj = body.get("khungGioTrucId");
        if (khungIdObj != null) {
            Integer khungId = toInteger(khungIdObj);
            if (khungId != null) {
                Optional<KhungGioTrucBan> khungOpt = khungGioTrucBanRepository.findById(khungId);
                khungOpt.ifPresent(lich::setKhungGioTruc);
            }
        }

        String ghiChu = (String) body.get("ghiChu");
        if (ghiChu != null) {
            lich.setGhiChu(ghiChu);
        }

        return lich;
    }

    private Map<String, Object> mapSchedule(LichTrucBan lich) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", lich.getLichtrucId());
        m.put("lichtrucId", lich.getLichtrucId());
        m.put("ngayTruc", lich.getNgayTruc());
        m.put("date", lich.getNgayTruc());
        m.put("ghiChu", lich.getGhiChu());
        m.put("notes", lich.getGhiChu());

        if (lich.getBacsi() != null) {
            BacSi bs = lich.getBacsi();
            m.put("bacsiId", bs.getBacsiId());
            m.put("bacsiName", bs.getHoTen());
        }
        if (lich.getPhongban() != null) {
            PhongBan pb = lich.getPhongban();
            m.put("phongbanId", pb.getPhongbanId());
            m.put("phongbanName", pb.getTenPhongban());
        }
        if (lich.getKhungGioTruc() != null) {
            KhungGioTrucBan khung = lich.getKhungGioTruc();
            m.put("khungGioTrucId", khung.getKhunggiotrucId());
            String label = khung.getKhunggiotruc() != null ? khung.getKhunggiotruc().toString() : "Ca trực";
            m.put("khungGioLabel", label);
            m.put("gioBatDau", khung.getKhunggiotruc() != null ? khung.getKhunggiotruc().toString() : null);
            m.put("gioKetThuc", null);
        }
        return m;
    }

    private LocalDate parseDate(String dateStr) {
        try {
            if (dateStr != null && !dateStr.isBlank()) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
            }
        } catch (Exception ignored) {
        }
        return LocalDate.now();
    }

    private Integer toInteger(Object value) {
        if (value instanceof Integer i) {
            return i;
        }
        if (value instanceof Number n) {
            return n.intValue();
        }
        if (value instanceof String s && !s.isBlank()) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}


