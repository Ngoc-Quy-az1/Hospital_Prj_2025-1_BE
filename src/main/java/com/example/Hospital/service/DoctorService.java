package com.example.Hospital.service;

import com.example.Hospital.entity.BacSi;
import com.example.Hospital.entity.BenhAn;
import com.example.Hospital.entity.BenhNhan;
import com.example.Hospital.entity.CaPhauThuat;
import com.example.Hospital.entity.DatLichKham;
import com.example.Hospital.entity.DonThuoc;
import com.example.Hospital.entity.DonThuocChiTiet;
import com.example.Hospital.entity.DonHangThuoc;
import com.example.Hospital.entity.HoSoKham;
import com.example.Hospital.entity.LabTest;
import com.example.Hospital.entity.LichLamViec;
import com.example.Hospital.entity.UserSessions;
import com.example.Hospital.entity.Users;
import com.example.Hospital.entity.YeuCauPhauThuat;
import com.example.Hospital.entity.CaPhauThuat;
import com.example.Hospital.entity.DonHangChiTiet;
import com.example.Hospital.exception.ErrorCode;
import com.example.Hospital.exception.UserException;
import com.example.Hospital.repository.BacSiRepository;
import com.example.Hospital.repository.BenhAnRepository;
import com.example.Hospital.repository.BenhNhanRepository;
import com.example.Hospital.repository.DatLichKhamRepository;
import com.example.Hospital.repository.DonThuocRepository;
import com.example.Hospital.repository.DonHangThuocRepository;
import com.example.Hospital.repository.LabTestRepository;
import com.example.Hospital.repository.LichLamViecRepository;
import com.example.Hospital.repository.HoSoKhamRepository;
import com.example.Hospital.repository.ThuocRepository;
import com.example.Hospital.repository.UserSessionsRepository;
import com.example.Hospital.repository.YeuCauPhauThuatRepository;
import com.example.Hospital.repository.CaPhauThuatRepository;
import com.example.Hospital.repository.DonHangChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class DoctorService {

    @Autowired
    private UserSessionsRepository userSessionsRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private DatLichKhamRepository datLichKhamRepository;

    @Autowired
    private DonThuocRepository donThuocRepository;

    @Autowired
    private LichLamViecRepository lichLamViecRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private LabTestRepository labTestRepository;
    
    @Autowired
    private YeuCauPhauThuatRepository yeuCauPhauThuatRepository;
    
    @Autowired
    private CaPhauThuatRepository caPhauThuatRepository;
    
    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    @Autowired
    private DonHangThuocRepository donHangThuocRepository;

    @Autowired
    private ThuocRepository thuocRepository;

    @Autowired
    private HoSoKhamRepository hoSoKhamRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getProfile(String authHeader) {
        BacSi doctor = resolveDoctor(authHeader);
        Map<String, Object> profile = new HashMap<>();
        profile.put("bacsiId", doctor.getBacsiId());
        profile.put("hoTen", doctor.getHoTen());
        profile.put("email", doctor.getEmail());
        profile.put("sdt", doctor.getSdt());
        profile.put("chucVu", doctor.getChuyenKhoa());

        if (doctor.getPhongban() != null) {
            Map<String, Object> phongban = new HashMap<>();
            phongban.put("phongbanId", doctor.getPhongban().getPhongbanId());
            phongban.put("tenPhongBan", doctor.getPhongban().getTenPhongban());
            profile.put("phongban", phongban);
        }

        return profile;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStats(String authHeader, String dateStr) {
        BacSi doctor = resolveDoctor(authHeader);
        LocalDate targetDate = parseDate(dateStr);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<DatLichKham> appointments = datLichKhamRepository
                .findByBacsi_BacsiIdAndNgayGioBetween(doctor.getBacsiId(), start, end);

        long appointmentsToday = appointments.size();
        long confirmed = appointments.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_duyet)
                .count();
        long completed = appointments.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_kham)
                .count();
        long pending = appointments.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.cho_duyet)
                .count();

        long patientsToday = appointments.stream()
                .map(a -> a.getBenhnhan() != null ? a.getBenhnhan().getBenhnhanId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        List<DonThuoc> prescriptions = donThuocRepository
                .findByBacsi_BacsiIdAndNgayKe(doctor.getBacsiId(), targetDate);
        long prescriptionsToday = prescriptions.size();
        long totalMedicines = prescriptions.stream()
                .flatMap(dt -> safeStream(dt.getDanhSachChiTiet()))
                .mapToLong(ct -> ct.getSoLuong() != null ? ct.getSoLuong() : 0)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("patientsToday", patientsToday);
        stats.put("appointmentsToday", appointmentsToday);
        stats.put("prescriptionsToday", prescriptionsToday);
        // Chưa có trường giá bán, tạm dùng tổng số thuốc làm giá trị quy đổi
        stats.put("totalPrescriptionValue", totalMedicines);
        stats.put("confirmedAppointments", confirmed);
        stats.put("completedAppointments", completed);
        stats.put("pendingAppointments", pending);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAppointmentStats(String authHeader, String dateStr) {
        BacSi doctor = resolveDoctor(authHeader);
        LocalDate targetDate = parseDate(dateStr);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<DatLichKham> appointments = datLichKhamRepository
                .findByBacsi_BacsiIdAndNgayGioBetween(doctor.getBacsiId(), start, end);

        long total = appointments.size();
        long confirmed = appointments.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_duyet)
                .count();
        long completed = appointments.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_kham)
                .count();
        long pending = appointments.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.cho_duyet)
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAppointments", total);
        stats.put("confirmedAppointments", confirmed);
        stats.put("completedAppointments", completed);
        stats.put("pendingAppointments", pending);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPrescriptionStats(String authHeader, String dateStr) {
        BacSi doctor = resolveDoctor(authHeader);
        LocalDate targetDate = parseDate(dateStr);

        List<DonThuoc> prescriptions = donThuocRepository
                .findByBacsi_BacsiIdAndNgayKe(doctor.getBacsiId(), targetDate);

        long totalPrescriptions = prescriptions.size();
        long totalMedicines = prescriptions.stream()
                .flatMap(dt -> safeStream(dt.getDanhSachChiTiet()))
                .mapToLong(ct -> ct.getSoLuong() != null ? ct.getSoLuong() : 0)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPrescriptions", totalPrescriptions);
        stats.put("totalMedicines", totalMedicines);
        // Chưa có giá thuốc nên dùng tổng số thuốc như giá trị ước lượng
        stats.put("totalValue", totalMedicines);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAppointments(String authHeader,
                                               String status,
                                               String dateStr,
                                               int page,
                                               int size) {
        BacSi doctor = resolveDoctor(authHeader);
        LocalDate targetDate = parseDate(dateStr);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<DatLichKham> rawList = datLichKhamRepository
                .findByBacsi_BacsiIdAndNgayGioBetween(doctor.getBacsiId(), start, end);

        // Lọc theo status tiếng Việt nếu có
        if (status != null && !status.isBlank()) {
            DatLichKham.TrangThaiTrangThai enumStatus = mapVietnameseStatusToEnum(status);
            if (enumStatus != null) {
                rawList = rawList.stream()
                        .filter(a -> a.getTrangThai() == enumStatus)
                        .toList();
            }
        }

        int totalElements = rawList.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<DatLichKham> pageList = fromIndex >= totalElements
                ? List.of()
                : rawList.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapAppointment)
                .toList();

        int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", page);
        result.put("size", size);
        return result;
    }

    @Transactional
    public Map<String, Object> approveAppointment(String authHeader, Integer id) {
        BacSi doctor = resolveDoctor(authHeader);
        DatLichKham apt = datLichKhamRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DAT_LICH_NOT_EXISTED));
        ensureAppointmentBelongsToDoctor(apt, doctor);

        apt.setTrangThai(DatLichKham.TrangThaiTrangThai.da_duyet);
        datLichKhamRepository.save(apt);
        return mapAppointment(apt);
    }

    @Transactional
    public Map<String, Object> rejectAppointment(String authHeader, Integer id, String reason) {
        BacSi doctor = resolveDoctor(authHeader);
        DatLichKham apt = datLichKhamRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DAT_LICH_NOT_EXISTED));
        ensureAppointmentBelongsToDoctor(apt, doctor);

        apt.setTrangThai(DatLichKham.TrangThaiTrangThai.huy);
        if (reason != null && !reason.isBlank()) {
            String note = apt.getGhiChu() != null ? apt.getGhiChu() + " | " + reason : reason;
            apt.setGhiChu(note);
        }
        datLichKhamRepository.save(apt);
        return mapAppointment(apt);
    }

    @Transactional
    public Map<String, Object> completeAppointment(String authHeader, Integer id) {
        BacSi doctor = resolveDoctor(authHeader);
        DatLichKham apt = datLichKhamRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DAT_LICH_NOT_EXISTED));
        ensureAppointmentBelongsToDoctor(apt, doctor);

        apt.setTrangThai(DatLichKham.TrangThaiTrangThai.da_kham);
        datLichKhamRepository.save(apt);
        return mapAppointment(apt);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSchedule(String authHeader,
                                                 String startDateStr,
                                                 String endDateStr) {
        BacSi doctor = resolveDoctor(authHeader);
        LocalDate start = parseDate(startDateStr);
        LocalDate endParsed = parseDate(endDateStr);
        LocalDate end = endParsed.isBefore(start) ? start : endParsed;

        List<LichLamViec> all = lichLamViecRepository.findByBacsi_BacsiId(doctor.getBacsiId());
        return all.stream()
                .filter(llv -> {
                    LocalDate d = llv.getNgayBatDau();
                    if (d == null) return false;
                    return (!d.isBefore(start) && !d.isAfter(end));
                })
                .map(this::mapWorkSchedule)
                .toList();
    }

    @Transactional
    public Map<String, Object> upsertSchedule(String authHeader,
                                              Map<String, Object> body) {
        BacSi doctor = resolveDoctor(authHeader);

        String ngayLamViecStr = (String) body.get("ngayLamViec");
        if (ngayLamViecStr == null || ngayLamViecStr.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }
        LocalDate date = LocalDate.parse(ngayLamViecStr, DateTimeFormatter.ISO_DATE);
        String gioBatDau = (String) body.get("gioBatDau");
        String gioKetThuc = (String) body.get("gioKetThuc");
        String ghiChu = (String) body.get("ghiChu");

        LichLamViec llv = new LichLamViec();
        llv.setBacsi(doctor);
        llv.setNgayBatDau(date);
        llv.setNgayKetThuc(date);
        if (gioBatDau != null && gioKetThuc != null) {
            llv.setCaLam(gioBatDau + "-" + gioKetThuc);
        } else {
            llv.setCaLam("Ca làm việc");
        }
        llv.setMoTa(ghiChu);
        lichLamViecRepository.save(llv);
        return mapWorkSchedule(llv);
    }

    @Transactional
    public Map<String, Object> createMedicalRecord(String authHeader, Map<String, Object> body) {
        BacSi doctor = resolveDoctor(authHeader);

        Integer patientId = body.get("benhnhanId") != null ? ((Number) body.get("benhnhanId")).intValue() : null;
        String ngayKhamStr = (String) body.get("ngayKham");
        if (patientId == null || ngayKhamStr == null || ngayKhamStr.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }
        BenhNhan bn = benhNhanRepository.findById(patientId)
                .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED));

        HoSoKham hsk = new HoSoKham();
        hsk.setBacsi(doctor);
        hsk.setBenhnhan(bn);
        hsk.setNgayKham(LocalDate.parse(ngayKhamStr, DateTimeFormatter.ISO_DATE));
        hsk.setTrieuChung((String) body.get("trieuChung"));
        hsk.setChanDoan((String) body.get("chanDoan"));
        hsk.setHuongDieuTri((String) body.get("dieuTri"));

        hoSoKhamRepository.save(hsk);

        // Có thể tạo thêm BenhAn nếu cần, nhưng FE chỉ cần biết lưu thành công
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Lưu hồ sơ khám thành công");
        return result;
    }

    // ======================= PATIENTS & STATS =======================

    @Transactional(readOnly = true)
    public Map<String, Object> getPatientStats(String authHeader) {
        BacSi doctor = resolveDoctor(authHeader);
        List<DatLichKham> all = datLichKhamRepository.findByBacsi_BacsiId(doctor.getBacsiId());

        long totalPatients = all.stream()
                .map(a -> a.getBenhnhan() != null ? a.getBenhnhan().getBenhnhanId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        long waiting = all.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.cho_duyet)
                .count();
        long confirmed = all.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_duyet)
                .count();
        long completed = all.stream()
                .filter(a -> a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_kham)
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", totalPatients);
        stats.put("waitingForExamination", waiting);
        stats.put("currentlyExamining", confirmed);
        stats.put("completed", completed);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPatients(String authHeader,
                                           String search,
                                           String appointmentStatus,
                                           int page,
                                           int size) {
        BacSi doctor = resolveDoctor(authHeader);

        List<DatLichKham> allAppointments = datLichKhamRepository.findByBacsi_BacsiId(doctor.getBacsiId());

        // Lọc theo trạng thái (tiếng Việt) nếu có
        DatLichKham.TrangThaiTrangThai statusEnum = appointmentStatus != null && !appointmentStatus.isBlank()
                ? mapVietnameseStatusToEnum(appointmentStatus)
                : null;
        if (statusEnum != null) {
            allAppointments = allAppointments.stream()
                    .filter(a -> a.getTrangThai() == statusEnum)
                    .toList();
        }

        // Gom theo bệnh nhân, lấy lịch hẹn mới nhất
        Map<Integer, DatLichKham> latestByPatient = new HashMap<>();
        for (DatLichKham a : allAppointments) {
            if (a.getBenhnhan() == null) continue;
            Integer pid = a.getBenhnhan().getBenhnhanId();
            DatLichKham existing = latestByPatient.get(pid);
            if (existing == null || (a.getNgayGio() != null && existing.getNgayGio() != null
                    && a.getNgayGio().isAfter(existing.getNgayGio()))) {
                latestByPatient.put(pid, a);
            }
        }

        List<Map<String, Object>> allPatients = latestByPatient.values().stream()
                .map(a -> mapPatientFromAppointment(a, search))
                .filter(Objects::nonNull)
                .toList();

        int totalElements = allPatients.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<Map<String, Object>> pageContent = fromIndex >= totalElements
                ? List.of()
                : allPatients.subList(fromIndex, toIndex);

        int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", pageContent);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", page);
        result.put("size", size);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPatientMedicalHistory(String authHeader,
                                                        Integer patientId,
                                                        int page,
                                                        int size) {
        resolveDoctor(authHeader); // đảm bảo token hợp lệ
        List<BenhAn> all = benhAnRepository.findByBenhnhan_BenhnhanId(patientId);

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<BenhAn> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapBenhAn)
                .toList();

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
    public Map<String, Object> getMedicalRecords(String authHeader,
                                                 Integer patientId,
                                                 int page,
                                                 int size) {
        BacSi doctor = resolveDoctor(authHeader);
        List<HoSoKham> all = doctor.getDanhSachHoSoKham() != null
                ? doctor.getDanhSachHoSoKham()
                : List.of();

        if (patientId != null) {
            all = all.stream()
                    .filter(hs -> hs.getBenhnhan() != null &&
                            Objects.equals(hs.getBenhnhan().getBenhnhanId(), patientId))
                    .toList();
        }

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<HoSoKham> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapHoSoKham)
                .toList();

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
    public Map<String, Object> getPatientAppointments(String authHeader,
                                                      Integer patientId,
                                                      int page,
                                                      int size) {
        resolveDoctor(authHeader);
        List<DatLichKham> all = datLichKhamRepository
                .findByBenhnhan_BenhnhanId(patientId);

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<DatLichKham> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapAppointment)
                .toList();

        int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", page);
        result.put("size", size);
        return result;
    }

    // ======================= LAB TESTS & SURGERIES =======================

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLabTests(String authHeader) {
        BacSi doctor = resolveDoctor(authHeader);
        List<LabTest> tests = labTestRepository.findByBacsi_BacsiId(doctor.getBacsiId());
        return tests.stream()
                .map(this::mapLabTest)
                .toList();
    }

    @Transactional
    public Map<String, Object> createLabTest(String authHeader, Map<String, Object> body) {
        BacSi doctor = resolveDoctor(authHeader);

        Object benhnhanIdObj = body.get("benhnhanId");
        Integer benhnhanId = benhnhanIdObj instanceof Number n ? n.intValue() : null;
        String loaiXetNghiem = (String) body.get("loaiXetNghiem");
        String ngayTestStr = (String) body.get("ngayTest");
        String ghiChu = (String) body.get("ghiChu"); // hiện entity LabTest chưa có ghiChu, có thể bỏ qua

        if (benhnhanId == null || loaiXetNghiem == null || loaiXetNghiem.isBlank()
                || ngayTestStr == null || ngayTestStr.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }

        BenhNhan bn = benhNhanRepository.findById(benhnhanId)
                .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED));

        LabTest lt = new LabTest();
        lt.setBenhnhan(bn);
        lt.setBacsi(doctor);
        lt.setLoaiXetNghiem(loaiXetNghiem);
        lt.setNgayTest(LocalDate.parse(ngayTestStr, DateTimeFormatter.ISO_DATE));
        // ghiChu hiện không lưu được, nếu sau này bổ sung field có thể set vào đây

        LabTest saved = labTestRepository.save(lt);
        return mapLabTest(saved);
    }

    @Transactional
    public Map<String, Object> updateLabTestResult(String authHeader, Integer id, Map<String, Object> body) {
        BacSi doctor = resolveDoctor(authHeader);
        LabTest lt = labTestRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.LAB_TEST_NOT_EXISTED));

        if (lt.getBacsi() != null && !Objects.equals(lt.getBacsi().getBacsiId(), doctor.getBacsiId())) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }

        String ketQua = (String) body.get("ketQua");
        if (ketQua == null || ketQua.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }
        lt.setKetQua(ketQua);

        // ghiChu từ body hiện chưa lưu được vì entity LabTest chưa có field tương ứng

        LabTest saved = labTestRepository.save(lt);
        return mapLabTest(saved);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSurgeryRequests(String authHeader) {
        BacSi doctor = resolveDoctor(authHeader);
        List<YeuCauPhauThuat> list = yeuCauPhauThuatRepository.findByBacsiChiDinh_BacsiId(doctor.getBacsiId());
        return list.stream()
                .map(this::mapSurgeryRequest)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSurgerySchedule(String authHeader) {
        BacSi doctor = resolveDoctor(authHeader);
        List<CaPhauThuat> list = caPhauThuatRepository.findByBacsiChinh_BacsiId(doctor.getBacsiId());
        return list.stream()
                .map(this::mapSurgerySchedule)
                .toList();
    }

    @Transactional
    public Map<String, Object> createSurgeryRequest(String authHeader, Map<String, Object> body) {
        BacSi doctor = resolveDoctor(authHeader);

        Object benhnhanIdObj = body.get("benhnhanId");
        Integer benhnhanId = benhnhanIdObj instanceof Number n ? n.intValue() : null;
        String loaiPhauThuat = (String) body.get("loaiPhauThuat");
        String ngayDuKienStr = (String) body.get("ngayDuKien");

        if (benhnhanId == null || loaiPhauThuat == null || loaiPhauThuat.isBlank()
                || ngayDuKienStr == null || ngayDuKienStr.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }

        BenhNhan bn = benhNhanRepository.findById(benhnhanId)
                .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED));

        YeuCauPhauThuat yc = new YeuCauPhauThuat();
        yc.setBenhnhan(bn);
        yc.setBacsiChiDinh(doctor);
        yc.setLoaiPhauThuat(loaiPhauThuat);
        yc.setNgayDuKien(LocalDate.parse(ngayDuKienStr, DateTimeFormatter.ISO_DATE));
        yc.setTinhTrang(YeuCauPhauThuat.TinhTrang.cho_duyet);

        YeuCauPhauThuat saved = yeuCauPhauThuatRepository.save(yc);
        return mapSurgeryRequest(saved);
    }

    // ======================= PRESCRIPTIONS & MEDICINES =======================

    @Transactional(readOnly = true)
    public Map<String, Object> getPrescriptions(String authHeader,
                                                Integer patientId,
                                                int page,
                                                int size) {
        BacSi doctor = resolveDoctor(authHeader);
        List<DonThuoc> all = patientId != null
                ? donThuocRepository.findByBenhnhan_BenhnhanIdAndBacsi_BacsiId(patientId, doctor.getBacsiId())
                : donThuocRepository.findByBacsi_BacsiId(doctor.getBacsiId());

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<DonThuoc> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(dt -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("donthuocId", dt.getDonthuocId());
                    m.put("ngayKe", dt.getNgayKe());
                    m.put("ghiChu", dt.getGhiChu());
                    if (dt.getBenhnhan() != null) {
                        m.put("benhnhan", Map.of(
                                "benhnhanId", dt.getBenhnhan().getBenhnhanId(),
                                "hoTen", dt.getBenhnhan().getHoTen()
                        ));
                    }
                    return m;
                })
                .toList();

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
    public Map<String, Object> getPrescriptionDetail(String authHeader, Integer id) {
        BacSi doctor = resolveDoctor(authHeader);
        DonThuoc dt = donThuocRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DON_THUOC_NOT_EXISTED));
        if (dt.getBacsi() != null && !Objects.equals(dt.getBacsi().getBacsiId(), doctor.getBacsiId())) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }
        Map<String, Object> m = new HashMap<>();
        m.put("donthuocId", dt.getDonthuocId());
        m.put("ngayKe", dt.getNgayKe());
        m.put("ghiChu", dt.getGhiChu());
        if (dt.getBacsi() != null) {
            m.put("bacsi", Map.of(
                    "bacsiId", dt.getBacsi().getBacsiId(),
                    "hoTen", dt.getBacsi().getHoTen()
            ));
        }
        if (dt.getBenhnhan() != null) {
            m.put("benhnhan", Map.of(
                    "benhnhanId", dt.getBenhnhan().getBenhnhanId(),
                    "hoTen", dt.getBenhnhan().getHoTen()
            ));
        }
        List<Map<String, Object>> details = safeStream(dt.getDanhSachChiTiet())
                .map(ct -> {
                    Map<String, Object> d = new HashMap<>();
                    d.put("id", ct.getId());
                    d.put("soLuong", ct.getSoLuong());
                    d.put("lieuDung", ct.getLieuDung());
                    if (ct.getThuoc() != null) {
                        Map<String, Object> t = new HashMap<>();
                        t.put("thuocId", ct.getThuoc().getThuocId());
                        t.put("tenThuoc", ct.getThuoc().getTenThuoc());
                        t.put("hamLuong", ct.getThuoc().getHamLuong());
                        t.put("dangBaoChe", ct.getThuoc().getDangBaoChe());
                        t.put("donGia", ct.getDonGia());
                        d.put("thuoc", t);
                    }
                    return d;
                })
                .toList();
        m.put("danhSachChiTiet", details);
        return m;
    }

    @Transactional
    public Map<String, Object> createPrescription(String authHeader,
                                                  Map<String, Object> body) {
        BacSi doctor = resolveDoctor(authHeader);

        Integer patientId = body.get("benhnhanId") != null ? ((Number) body.get("benhnhanId")).intValue() : null;
        String ngayKeStr = body.get("ngayKe") != null
                ? (String) body.get("ngayKe")
                : (String) body.get("ngayKeDon");
        String ghiChu = (String) body.get("ghiChu");

        if (patientId == null || ngayKeStr == null || ngayKeStr.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }

        BenhNhan bn = benhNhanRepository.findById(patientId)
                .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED));

        DonThuoc dt = new DonThuoc();
        dt.setBacsi(doctor);
        dt.setBenhnhan(bn);
        dt.setNgayKe(LocalDate.parse(ngayKeStr, DateTimeFormatter.ISO_DATE));
        dt.setGhiChu(ghiChu);

        // Chi tiết đơn thuốc đơn giản: không bắt buộc phải tạo nếu FE không dùng
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> chiTiet = (List<Map<String, Object>>) (
                body.get("chiTiet") != null ? body.get("chiTiet") : body.get("chiTietDonThuoc")
        );
        List<DonThuocChiTiet> items = List.of();
        if (chiTiet != null) {
            items = chiTiet.stream()
                    .map(row -> {
                        DonThuocChiTiet ct = new DonThuocChiTiet();
                        ct.setDonThuoc(dt);
                        Object thuocIdObj = row.get("thuocId");
                        Integer tid = null;
                        if (thuocIdObj != null) {
                            tid = ((Number) thuocIdObj).intValue();
                            thuocRepository.findById(tid).ifPresent(ct::setThuoc);
                        }
                        Object soLuongObj = row.get("soLuong");
                        if (soLuongObj instanceof Number n) {
                            ct.setSoLuong(n.intValue());
                        }
                        String lieuDung = row.get("lieuDung") != null
                                ? (String) row.get("lieuDung")
                                : (String) row.get("cachDung");
                        ct.setLieuDung(lieuDung);

                        // Đơn giá ưu tiên lấy từ FE, nếu không có thì fallback theo đơn hàng
                        Object donGiaObj = row.get("donGia");
                        BigDecimal gia = null;
                        if (donGiaObj instanceof Number n) {
                            gia = BigDecimal.valueOf(n.doubleValue());
                        } else if (tid != null) {
                            gia = resolveMedicinePrice(tid);
                        }
                        ct.setDonGia(gia);
                        return ct;
                    })
                    .toList();
            dt.setDanhSachChiTiet(items);
        }

        // Tạo đơn hàng thuốc tương ứng để lưu tổng tiền
        BigDecimal tongTien = items.stream()
                .map(ct -> {
                    BigDecimal gia = ct.getDonGia() != null ? ct.getDonGia() : BigDecimal.ZERO;
                    int sl = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                    return gia.multiply(BigDecimal.valueOf(sl));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DonHangThuoc donHang = new DonHangThuoc();
        donHang.setBenhnhan(bn);
        donHang.setNgayDat(LocalDateTime.now());
        donHang.setTongTien(tongTien);
        donHang.setTrangThai(DonHangThuoc.TrangThai.cho_xu_ly);

        List<DonHangChiTiet> orderDetails = items.stream()
                .filter(ct -> ct.getThuoc() != null)
                .map(ct -> {
                    DonHangChiTiet dh = new DonHangChiTiet();
                    dh.setDonHangThuoc(donHang);
                    dh.setThuoc(ct.getThuoc());
                    dh.setSoLuong(ct.getSoLuong());
                    dh.setDonGia(ct.getDonGia());
                    return dh;
                })
                .toList();
        donHang.setDanhSachChiTiet(orderDetails);

        donHangThuocRepository.save(donHang);

        DonThuoc saved = donThuocRepository.save(dt);
        return getPrescriptionDetail(authHeader, saved.getDonthuocId());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMedicines(String authHeader,
                                            String search,
                                            int page,
                                            int size) {
        resolveDoctor(authHeader); // chỉ cần token
        List<com.example.Hospital.entity.Thuoc> all;
        if (search != null && !search.isBlank()) {
            all = thuocRepository.findByTenThuocContainingIgnoreCase(search);
        } else {
            all = thuocRepository.findAll();
        }

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<com.example.Hospital.entity.Thuoc> pageList = fromIndex >= totalElements
                ? List.of()
                : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(t -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("thuocId", t.getThuocId());
                    m.put("tenThuoc", t.getTenThuoc());
                    m.put("hoatChat", t.getHoatChat());
                    m.put("hamLuong", t.getHamLuong());
                    m.put("dangBaoChe", t.getDangBaoChe());
                    BigDecimal price = resolveMedicinePrice(t.getThuocId());
                    m.put("donGia", price);
                    m.put("donViTinh", null);
                    return m;
                })
                .toList();

        int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", page);
        result.put("size", size);
        return result;
    }

    // ======================= BILLS =======================

    @Transactional(readOnly = true)
    public Map<String, Object> getBills(String authHeader,
                                        Integer patientId,
                                        String status,
                                        int page,
                                        int size) {
        BacSi doctor = resolveDoctor(authHeader);

        List<DonHangThuoc> all;
        DonHangThuoc.TrangThai trangThai = null;
        if (status != null && !status.isBlank()) {
            try {
                trangThai = DonHangThuoc.TrangThai.valueOf(status);
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (patientId != null && trangThai != null) {
            all = donHangThuocRepository.findByBenhnhan_BenhnhanIdAndTrangThai(patientId, trangThai);
        } else if (patientId != null) {
            all = donHangThuocRepository.findByBenhnhan_BenhnhanId(patientId);
        } else if (trangThai != null) {
            all = donHangThuocRepository.findByTrangThai(trangThai);
        } else {
            all = donHangThuocRepository.findAll();
        }

        int totalElements = all.size();
        int fromIndex = Math.max(page, 0) * Math.max(size, 1);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<DonHangThuoc> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapBill)
                .toList();

        int totalPages = size <= 0 ? 1 : (int) Math.ceil((double) totalElements / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", page);
        result.put("size", size);
        return result;
    }

    private BacSi resolveDoctor(String authHeader) {
        Users user = resolveUser(authHeader);

        BacSi doctor = null;
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            doctor = bacSiRepository.findByEmail(user.getEmail()).orElse(null);
        }
        if (doctor == null) {
            doctor = bacSiRepository.findByUser_UserId(user.getUserId()).orElse(null);
        }
        if (doctor == null) {
            doctor = bacSiRepository.findTopByOrderByBacsiIdAsc()
                    .orElseThrow(() -> new UserException(ErrorCode.BAC_SI_NOT_EXISTED));
        }
        return doctor;
    }

    private Users resolveUser(String authHeader) {
        String token = extractToken(authHeader);
        UserSessions session = userSessionsRepository.findByAccessToken(token)
                .orElseThrow(() -> new UserException(ErrorCode.UNAUTHENTICATED));

        if (Boolean.TRUE.equals(session.getIsRevoked())) {
            throw new UserException(ErrorCode.SESSION_EXPIRED);
        }
        if (session.getExpiresAt() != null && session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UserException(ErrorCode.SESSION_EXPIRED);
        }
        Users user = session.getUser();
        if (user == null) {
            throw new UserException(ErrorCode.USER_NOT_EXISTED);
        }
        return user;
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new UserException(ErrorCode.UNAUTHENTICATED);
        }
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
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

    private Stream<DonThuocChiTiet> safeStream(List<DonThuocChiTiet> list) {
        return list != null ? list.stream() : Stream.empty();
    }

    private DatLichKham.TrangThaiTrangThai mapVietnameseStatusToEnum(String statusVi) {
        String s = statusVi.trim().toLowerCase();
        return switch (s) {
            case "chờ khám", "chờ duyệt" -> DatLichKham.TrangThaiTrangThai.cho_duyet;
            case "đang khám", "đã duyệt" -> DatLichKham.TrangThaiTrangThai.da_duyet;
            case "hoàn thành", "đã khám" -> DatLichKham.TrangThaiTrangThai.da_kham;
            case "đã hủy", "hủy" -> DatLichKham.TrangThaiTrangThai.huy;
            default -> null;
        };
    }

    private void ensureAppointmentBelongsToDoctor(DatLichKham apt, BacSi doctor) {
        if (apt.getBacsi() != null &&
                !Objects.equals(apt.getBacsi().getBacsiId(), doctor.getBacsiId())) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }
    }

    private Map<String, Object> mapAppointment(DatLichKham a) {
        Map<String, Object> m = new HashMap<>();
        m.put("datlichId", a.getDatlichId());
        m.put("ngayGio", a.getNgayGio());
        m.put("trangThai", a.getTrangThai() != null ? a.getTrangThai().name() : null);
        m.put("loaiKham", a.getLoaiKham());
        // Chưa có field riêng cho lý do khám, tạm reuse ghiChu
        m.put("lyDoKham", a.getGhiChu());
        m.put("ghiChu", a.getGhiChu());

        if (a.getBenhnhan() != null) {
            Map<String, Object> bn = new HashMap<>();
            bn.put("benhnhanId", a.getBenhnhan().getBenhnhanId());
            bn.put("hoTen", a.getBenhnhan().getHoTen());
            bn.put("ngaySinh", a.getBenhnhan().getNgaySinh());
            bn.put("gioiTinh", a.getBenhnhan().getGioiTinh() != null ? a.getBenhnhan().getGioiTinh().name() : null);
            bn.put("sdt", a.getBenhnhan().getSdt());
            m.put("benhnhan", bn);
        } else {
            m.put("benhnhan", null);
        }
        return m;
    }

    private Map<String, Object> mapWorkSchedule(LichLamViec llv) {
        Map<String, Object> m = new HashMap<>();
        m.put("lichlamviecId", llv.getLichId());
        m.put("id", llv.getLichId());
        m.put("ngayLamViec", llv.getNgayBatDau());
        m.put("ghiChu", llv.getMoTa());

        String caLam = llv.getCaLam();
        String gioBatDau = null;
        String gioKetThuc = null;
        if (caLam != null && caLam.contains("-")) {
            String[] parts = caLam.split("-");
            if (parts.length >= 2) {
                gioBatDau = parts[0].trim();
                gioKetThuc = parts[1].trim();
            }
        }
        m.put("gioBatDau", gioBatDau);
        m.put("gioKetThuc", gioKetThuc);
        return m;
    }

    private Map<String, Object> mapPatientFromAppointment(DatLichKham a, String search) {
        if (a.getBenhnhan() == null) return null;
        BenhNhan bn = a.getBenhnhan();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            boolean matches = (bn.getHoTen() != null && bn.getHoTen().toLowerCase().contains(q))
                    || (bn.getSdt() != null && bn.getSdt().toLowerCase().contains(q))
                    || (bn.getEmail() != null && bn.getEmail().toLowerCase().contains(q));
            if (!matches) return null;
        }

        String trangThaiVi;
        if (a.getTrangThai() == null || a.getTrangThai() == DatLichKham.TrangThaiTrangThai.cho_duyet) {
            trangThaiVi = "Chờ khám";
        } else if (a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_duyet) {
            trangThaiVi = "Đang khám";
        } else if (a.getTrangThai() == DatLichKham.TrangThaiTrangThai.da_kham) {
            trangThaiVi = "Hoàn thành";
        } else if (a.getTrangThai() == DatLichKham.TrangThaiTrangThai.huy) {
            trangThaiVi = "Đã hủy";
        } else {
            trangThaiVi = "Chờ khám";
        }

        Map<String, Object> m = new HashMap<>();
        m.put("benhnhanId", bn.getBenhnhanId());
        m.put("hoTen", bn.getHoTen());
        m.put("ngaySinh", bn.getNgaySinh());
        m.put("gioiTinh", bn.getGioiTinh() != null ? bn.getGioiTinh().name() : null);
        m.put("sdt", bn.getSdt());
        m.put("email", bn.getEmail());
        m.put("diaChi", bn.getDiaChi());
        m.put("trangThai", trangThaiVi);
        m.put("khoa", a.getBacsi() != null && a.getBacsi().getPhongban() != null
                ? a.getBacsi().getPhongban().getTenPhongban()
                : null);
        m.put("trieuChung", a.getGhiChu());
        if (a.getNgayGio() != null) {
            m.put("ngayHen", a.getNgayGio().toLocalDate());
            m.put("gioHen", a.getNgayGio().toLocalTime().toString());
        }
        return m;
    }

    private Map<String, Object> mapBenhAn(BenhAn ba) {
        Map<String, Object> m = new HashMap<>();
        m.put("benhanId", ba.getBenhanId());
        m.put("ghiChu", ba.getGhiChu());
        if (ba.getHoSoKham() != null) {
            HoSoKham hs = ba.getHoSoKham();
            Map<String, Object> hsMap = new HashMap<>();
            hsMap.put("hosokhamId", hs.getHosokhamId());
            hsMap.put("ngayKham", hs.getNgayKham());
            hsMap.put("chanDoan", hs.getChanDoan());
            hsMap.put("huongDieuTri", hs.getHuongDieuTri());
            m.put("hoSoKham", hsMap);
        }
        if (ba.getDonThuoc() != null) {
            m.put("donThuoc", Map.of("donthuocId", ba.getDonThuoc().getDonthuocId()));
        }
        if (ba.getLabTest() != null) {
            m.put("labTest", Map.of("labtestId", ba.getLabTest().getLabtestId()));
        }
        if (ba.getCaPhauThuat() != null) {
            CaPhauThuat ca = ba.getCaPhauThuat();
            m.put("caPhauThuat", Map.of("caId", ca.getCaId()));
        }
        return m;
    }

    private Map<String, Object> mapHoSoKham(HoSoKham hs) {
        Map<String, Object> m = new HashMap<>();
        m.put("hosokhamId", hs.getHosokhamId());
        m.put("ngayKham", hs.getNgayKham());
        m.put("trieuChung", hs.getTrieuChung());
        m.put("chanDoan", hs.getChanDoan());
        m.put("huongDieuTri", hs.getHuongDieuTri());
        return m;
    }

    private Map<String, Object> mapBill(DonHangThuoc bill) {
        Map<String, Object> m = new HashMap<>();
        m.put("donhangId", bill.getDonhangId());
        m.put("ngayDat", bill.getNgayDat());
        m.put("tongTien", bill.getTongTien());
        m.put("trangThai", bill.getTrangThai() != null ? bill.getTrangThai().name() : null);
        if (bill.getBenhnhan() != null) {
            m.put("benhnhan", Map.of(
                    "benhnhanId", bill.getBenhnhan().getBenhnhanId(),
                    "hoTen", bill.getBenhnhan().getHoTen()
            ));
        }
        return m;
    }

    /**
     * Lấy đơn giá thuốc từ dữ liệu đơn hàng (nếu có).
     * Tạm thời lấy theo bản ghi bất kỳ, thường là lần nhập gần nhất.
     */
    private BigDecimal resolveMedicinePrice(Integer thuocId) {
        if (thuocId == null) return null;
        if (donHangChiTietRepository == null) return null;
        List<DonHangChiTiet> list = donHangChiTietRepository.findByThuoc_ThuocId(thuocId);
        if (list == null || list.isEmpty()) {
            return null;
        }
        // Lấy theo phần tử cuối danh sách (giả định là mới nhất)
        DonHangChiTiet any = list.get(list.size() - 1);
        return any.getDonGia();
    }

    private Map<String, Object> mapLabTest(LabTest lt) {
        Map<String, Object> m = new HashMap<>();
        m.put("labtestId", lt.getLabtestId());
        m.put("ngayTest", lt.getNgayTest());
        m.put("loaiXetNghiem", lt.getLoaiXetNghiem());
        m.put("ketQua", lt.getKetQua());
        if (lt.getBenhnhan() != null) {
            m.put("benhnhan", Map.of(
                    "benhnhanId", lt.getBenhnhan().getBenhnhanId(),
                    "hoTen", lt.getBenhnhan().getHoTen()
            ));
        }
        return m;
    }

    private Map<String, Object> mapSurgeryRequest(YeuCauPhauThuat yc) {
        Map<String, Object> m = new HashMap<>();
        m.put("yeuCauId", yc.getYcptId());
        m.put("ngayDuKien", yc.getNgayDuKien());
        m.put("loaiPhauThuat", yc.getLoaiPhauThuat());
        m.put("tinhTrang", yc.getTinhTrang() != null ? yc.getTinhTrang().name() : null);
        if (yc.getBenhnhan() != null) {
            m.put("benhnhan", Map.of(
                    "benhnhanId", yc.getBenhnhan().getBenhnhanId(),
                    "hoTen", yc.getBenhnhan().getHoTen()
            ));
        }
        return m;
    }

    private Map<String, Object> mapSurgerySchedule(CaPhauThuat ca) {
        Map<String, Object> m = new HashMap<>();
        m.put("caId", ca.getCaId());
        m.put("ngayGio", ca.getNgayGio());
        m.put("phongPhauThuat", ca.getPhongPhauThuat());
        m.put("ketQua", ca.getKetQua());
        if (ca.getYeuCauPhauThuat() != null) {
            m.put("loaiPhauThuat", ca.getYeuCauPhauThuat().getLoaiPhauThuat());
        }
        if (ca.getBacsiChinh() != null) {
            m.put("bacsi", Map.of(
                    "bacsiId", ca.getBacsiChinh().getBacsiId(),
                    "hoTen", ca.getBacsiChinh().getHoTen()
            ));
        }
        if (ca.getYeuCauPhauThuat() != null && ca.getYeuCauPhauThuat().getBenhnhan() != null) {
            m.put("benhnhan", Map.of(
                    "benhnhanId", ca.getYeuCauPhauThuat().getBenhnhan().getBenhnhanId(),
                    "hoTen", ca.getYeuCauPhauThuat().getBenhnhan().getHoTen()
            ));
        }
        return m;
    }
}


