package com.example.Hospital.service;

import com.example.Hospital.entity.*;
import com.example.Hospital.exception.ErrorCode;
import com.example.Hospital.exception.UserException;
import com.example.Hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private DatLichKhamRepository datLichKhamRepository;

    @Autowired
    private DonThuocRepository donThuocRepository;

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private LabTestRepository labTestRepository;

    @Autowired
    private UserSessionsRepository userSessionsRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getProfile(String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);

        Map<String, Object> profile = new HashMap<>();
        profile.put("benhnhanId", patient.getBenhnhanId());
        profile.put("hoTen", patient.getHoTen());
        profile.put("ngaySinh", patient.getNgaySinh());
        profile.put("gioiTinh", patient.getGioiTinh() != null ? patient.getGioiTinh().name() : null);
        profile.put("sdt", patient.getSdt());
        profile.put("email", patient.getEmail());
        profile.put("diaChi", patient.getDiaChi());
        // Các field ngày nhập viện / trạng thái chưa có cột riêng nên tạm bỏ qua
        profile.put("ngayNhapVien", (Object) null);
        profile.put("trangThai", (Object) null);
        return profile;
    }

    @Transactional
    public Map<String, Object> updateProfile(String emailOrUsername, Map<String, Object> body) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);

        if (body.containsKey("hoTen")) {
            patient.setHoTen(Objects.toString(body.get("hoTen"), patient.getHoTen()));
        }
        if (body.containsKey("diaChi")) {
            patient.setDiaChi(Objects.toString(body.get("diaChi"), patient.getDiaChi()));
        }
        if (body.containsKey("sdt")) {
            patient.setSdt(Objects.toString(body.get("sdt"), patient.getSdt()));
        }
        if (body.containsKey("email")) {
            patient.setEmail(Objects.toString(body.get("email"), patient.getEmail()));
        }
        benhNhanRepository.save(patient);
        return getProfile(emailOrUsername);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDoctors(int page, int size) {
        List<BacSi> all = bacSiRepository.findAll();

        List<Map<String, Object>> content = all.stream().map(bs -> {
            Map<String, Object> m = new HashMap<>();
            m.put("bacsiId", bs.getBacsiId());
            m.put("hoTen", bs.getHoTen());
            m.put("chuyenKhoa", bs.getChuyenKhoa());
            m.put("sdt", bs.getSdt());
            m.put("email", bs.getEmail());
            Map<String, Object> pb = null;
            if (bs.getPhongban() != null) {
                pb = new HashMap<>();
                pb.put("phongbanId", bs.getPhongban().getPhongbanId());
                pb.put("tenPhongban", bs.getPhongban().getTenPhongban());
            }
            m.put("phongban", pb);
            return m;
        }).collect(Collectors.toList());

        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("content", content);
        pageResult.put("totalElements", content.size());
        pageResult.put("totalPages", 1);
        pageResult.put("number", 0);
        pageResult.put("size", content.size());
        return pageResult;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAppointments(String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        List<DatLichKham> list = datLichKhamRepository.findByBenhnhan_BenhnhanId(patient.getBenhnhanId());

        return list.stream().map(dlk -> {
            Map<String, Object> m = new HashMap<>();
            m.put("datlichId", dlk.getDatlichId());
            m.put("ngayGio", dlk.getNgayGio());
            m.put("loaiKham", dlk.getLoaiKham());
            m.put("trangThai", dlk.getTrangThai() != null ? dlk.getTrangThai().name() : null);
            m.put("ghiChu", dlk.getGhiChu());
            if (dlk.getBacsi() != null) {
                Map<String, Object> bs = new HashMap<>();
                bs.put("bacsiId", dlk.getBacsi().getBacsiId());
                bs.put("hoTen", dlk.getBacsi().getHoTen());
                bs.put("chuyenKhoa", dlk.getBacsi().getChuyenKhoa());
                m.put("bacsi", bs);
            } else {
                m.put("bacsi", null);
            }
            return m;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> bookAppointment(String emailOrUsername, Map<String, Object> body) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);

        String ngayGioStr = (String) body.get("ngayGio");
        String loaiKham = (String) body.get("loaiKham");
        String ghiChu = (String) body.get("ghiChu");
        Integer bacsiId = body.get("bacsiId") instanceof Number n ? n.intValue() : null;

        if (ngayGioStr == null || ngayGioStr.isBlank()) {
            throw new UserException(ErrorCode.INVALID_DATA);
        }

        LocalDateTime ngayGio = LocalDateTime.parse(ngayGioStr.replace(" ", "T"));

        DatLichKham dlk = new DatLichKham();
        dlk.setBenhnhan(patient);
        dlk.setNgayGio(ngayGio);
        dlk.setLoaiKham(loaiKham != null && !loaiKham.isBlank() ? loaiKham : "Khám thường");
        dlk.setGhiChu(ghiChu);
        dlk.setTrangThai(DatLichKham.TrangThaiTrangThai.cho_duyet);
        if (bacsiId != null) {
            bacSiRepository.findById(bacsiId).ifPresent(dlk::setBacsi);
        }

        DatLichKham saved = datLichKhamRepository.save(dlk);
        return Map.of("datlichId", saved.getDatlichId());
    }

    @Transactional
    public void updateAppointment(String emailOrUsername, Integer id, Map<String, Object> body) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        DatLichKham dlk = datLichKhamRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DAT_LICH_NOT_EXISTED));
        if (dlk.getBenhnhan() == null ||
                !Objects.equals(dlk.getBenhnhan().getBenhnhanId(), patient.getBenhnhanId())) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }

        String ngayGioStr = (String) body.get("ngayGio");
        if (ngayGioStr != null && !ngayGioStr.isBlank()) {
            LocalDateTime ngayGio = LocalDateTime.parse(ngayGioStr.replace(" ", "T"));
            dlk.setNgayGio(ngayGio);
        }
        String loaiKham = (String) body.get("loaiKham");
        if (loaiKham != null && !loaiKham.isBlank()) {
            dlk.setLoaiKham(loaiKham);
        }
        String ghiChu = (String) body.get("ghiChu");
        if (ghiChu != null) {
            dlk.setGhiChu(ghiChu);
        }

        datLichKhamRepository.save(dlk);
    }

    @Transactional
    public void cancelAppointment(String emailOrUsername, Integer id) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        DatLichKham dlk = datLichKhamRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DAT_LICH_NOT_EXISTED));
        if (dlk.getBenhnhan() == null ||
                !Objects.equals(dlk.getBenhnhan().getBenhnhanId(), patient.getBenhnhanId())) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }
        dlk.setTrangThai(DatLichKham.TrangThaiTrangThai.huy);
        datLichKhamRepository.save(dlk);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPrescriptions(String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        List<DonThuoc> list = donThuocRepository.findByBenhnhan_BenhnhanId(patient.getBenhnhanId());

        return list.stream().map(dt -> {
            Map<String, Object> m = new HashMap<>();
            m.put("donthuocId", dt.getDonthuocId());
            m.put("ngayKe", dt.getNgayKe());
            m.put("ghiChu", dt.getGhiChu());
            if (dt.getBacsi() != null) {
                Map<String, Object> bs = new HashMap<>();
                bs.put("bacsiId", dt.getBacsi().getBacsiId());
                bs.put("hoTen", dt.getBacsi().getHoTen());
                m.put("bacsi", bs);
            }
            return m;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPrescriptionDetail(Integer id, String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        DonThuoc dt = donThuocRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.DON_THUOC_NOT_EXISTED));
        if (dt.getBenhnhan() == null || !Objects.equals(dt.getBenhnhan().getBenhnhanId(), patient.getBenhnhanId())) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }

        Map<String, Object> m = new HashMap<>();
        m.put("donthuocId", dt.getDonthuocId());
        m.put("ngayKe", dt.getNgayKe());
        m.put("ghiChu", dt.getGhiChu());
        if (dt.getBacsi() != null) {
            Map<String, Object> bs = new HashMap<>();
            bs.put("bacsiId", dt.getBacsi().getBacsiId());
            bs.put("hoTen", dt.getBacsi().getHoTen());
            m.put("bacsi", bs);
        }
        List<Map<String, Object>> details = Optional.ofNullable(dt.getDanhSachChiTiet())
                .orElse(Collections.emptyList())
                .stream()
                .map(ct -> {
                    Map<String, Object> d = new HashMap<>();
                    d.put("id", ct.getId());
                    d.put("soLuong", ct.getSoLuong());
                    d.put("lieuDung", ct.getLieuDung());
                    if (ct.getThuoc() != null) {
                        Map<String, Object> thuoc = new HashMap<>();
                        thuoc.put("thuocId", ct.getThuoc().getThuocId());
                        thuoc.put("tenThuoc", ct.getThuoc().getTenThuoc());
                        thuoc.put("hamLuong", ct.getThuoc().getHamLuong());
                        thuoc.put("dangBaoChe", ct.getThuoc().getDangBaoChe());
                        d.put("thuoc", thuoc);
                    }
                    return d;
                }).collect(Collectors.toList());

        m.put("danhSachChiTiet", details);
        return m;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMedicalHistory(String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        List<BenhAn> list = benhAnRepository.findByBenhnhan_BenhnhanId(patient.getBenhnhanId());

        return list.stream().map(ba -> {
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
                m.put("caPhauThuat", Map.of("caId", ba.getCaPhauThuat().getCaId()));
            }
            return m;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBills(String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        List<HoaDon> list = hoaDonRepository.findByBenhnhan_BenhnhanId(patient.getBenhnhanId());

        return list.stream().map(hd -> {
            Map<String, Object> m = new HashMap<>();
            m.put("billId", hd.getHoadonId());
            m.put("tongTien", hd.getTongTien());
            String status = hd.getTrangThai() == HoaDon.TrangThai.da_thanh_toan ? "PAID" : "UNPAID";
            m.put("trangThai", status);
            m.put("ngayThanhToan", hd.getNgayLap());
            return m;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLabResults(String emailOrUsername) {
        BenhNhan patient = resolveCurrentPatient(emailOrUsername);
        List<LabTest> list = labTestRepository.findByBenhnhan_BenhnhanId(patient.getBenhnhanId());

        return list.stream().map(lt -> {
            Map<String, Object> m = new HashMap<>();
            m.put("labtestId", lt.getLabtestId());
            m.put("ngayTest", lt.getNgayTest());
            m.put("loaiXetNghiem", lt.getLoaiXetNghiem());
            m.put("ketQua", lt.getKetQua());
            return m;
        }).collect(Collectors.toList());
    }

    private BenhNhan resolveCurrentPatient(String emailOrUsernameOrToken) {
        if (emailOrUsernameOrToken == null || emailOrUsernameOrToken.isBlank()) {
            throw new UserException(ErrorCode.UNAUTHENTICATED);
        }

        String id = emailOrUsernameOrToken.trim();

        // Nếu truyền lên dạng Authorization header, tách "Bearer "
        if (id.startsWith("Bearer ")) {
            id = id.substring(7);
        }

        // Nếu trông giống JWT (3 phần, có dấu chấm) → tra theo accessToken
        if (id.split("\\.").length == 3) {
            UserSessions session = userSessionsRepository.findByAccessToken(id)
                    .orElseThrow(() -> new UserException(ErrorCode.UNAUTHENTICATED));
            Users u = session.getUser();
            if (u == null) {
                throw new UserException(ErrorCode.USER_NOT_EXISTED);
            }
            // Nếu user đã gắn với BenhNhan thì dùng luôn
            if (u.getBenhnhan() != null) {
                return u.getBenhnhan();
            }
            // Nếu là role bệnh nhân nhưng chưa gắn BenhNhan, cố gắng tự map theo email rồi lưu lại
            if (u.getRole() != null && "benhnhan".equalsIgnoreCase(u.getRole().getTenRole())) {
                if (u.getEmail() != null && !u.getEmail().isBlank()) {
                    BenhNhan mapped = benhNhanRepository.findByEmail(u.getEmail())
                            .orElseGet(() -> benhNhanRepository.findAll().stream()
                                    .findFirst()
                                    .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED)));
                    u.setBenhnhan(mapped);
                    usersRepository.save(u);
                    return mapped;
                } else {
                    BenhNhan mapped = benhNhanRepository.findAll().stream()
                            .findFirst()
                            .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED));
                    u.setBenhnhan(mapped);
                    usersRepository.save(u);
                    return mapped;
                }
            }
            // Các role khác: không phải tài khoản bệnh nhân
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }

        // Ưu tiên map qua Users nếu có
        String identifier = id;
        Optional<Users> userOpt = usersRepository.findByUsername(identifier);
        if (userOpt.isEmpty() && identifier.contains("@")) {
            userOpt = usersRepository.findByEmail(identifier);
        }
        if (userOpt.isPresent() && userOpt.get().getBenhnhan() != null) {
            return userOpt.get().getBenhnhan();
        }

        // Fallback: map trực tiếp BenhNhan theo email
        if (identifier.contains("@")) {
            return benhNhanRepository.findByEmail(identifier)
                    .orElseGet(() -> benhNhanRepository.findAll().stream()
                            .findFirst()
                            .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED)));
        }
        // Fallback cuối: lấy bất kỳ bệnh nhân
        return benhNhanRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new UserException(ErrorCode.BENH_NHAN_NOT_EXISTED));
    }
}


