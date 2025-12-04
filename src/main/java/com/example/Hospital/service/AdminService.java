package com.example.Hospital.service;

import com.example.Hospital.entity.BacSi;
import com.example.Hospital.entity.BenhAn;
import com.example.Hospital.entity.BenhNhan;
import com.example.Hospital.entity.KhoThuoc;
import com.example.Hospital.entity.NhanVien;
import com.example.Hospital.entity.PhongBan;
import com.example.Hospital.entity.Thuoc;
import com.example.Hospital.entity.Users;
import com.example.Hospital.entity.DonThuoc;
import com.example.Hospital.entity.DonThuocChiTiet;
import com.example.Hospital.entity.LabTest;
import com.example.Hospital.entity.HoaDon;
import com.example.Hospital.repository.BacSiRepository;
import com.example.Hospital.repository.BenhAnRepository;
import com.example.Hospital.repository.BenhNhanRepository;
import com.example.Hospital.repository.DatLichKhamRepository;
import com.example.Hospital.repository.DonThuocRepository;
import com.example.Hospital.repository.KhoThuocRepository;
import com.example.Hospital.repository.LabTestRepository;
import com.example.Hospital.repository.NhanVienRepository;
import com.example.Hospital.repository.PhongBanRepository;
import com.example.Hospital.repository.RolesRepository;
import com.example.Hospital.repository.ThuocRepository;
import com.example.Hospital.repository.UsersRepository;
import com.example.Hospital.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private PhongBanRepository phongBanRepository;

    @Autowired
    private DatLichKhamRepository datLichKhamRepository;

    @Autowired
    private ThuocRepository thuocRepository;

    @Autowired
    private KhoThuocRepository khoThuocRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private DonThuocRepository donThuocRepository;

    @Autowired
    private LabTestRepository labTestRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // ===================== USER MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getUsers(int page, int size) {
        Page<Users> p = usersRepository.findAll(PageRequest.of(page, size));
        List<Map<String, Object>> content = p.getContent().stream()
                .map(this::mapUser)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", p.getTotalElements());
        result.put("totalPages", p.getTotalPages());
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserById(Integer id) {
        Users u = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapUser(u);
    }

    @Transactional
    public Map<String, Object> createUser(Map<String, Object> body) {
        Users u = new Users();
        u.setUsername((String) body.get("username"));
        u.setEmail((String) body.get("email"));
        Object roleObj = body.get("role");
        if (roleObj instanceof Map roleMap && roleMap.containsKey("tenRole")) {
            String roleName = (String) roleMap.get("tenRole");
            rolesRepository.findByTenRole(roleName).ifPresent(u::setRole);
        } else if (roleObj instanceof String roleName) {
            rolesRepository.findByTenRole(roleName).ifPresent(u::setRole);
        }
        usersRepository.save(u);
        return mapUser(u);
    }

    @Transactional
    public Map<String, Object> updateUser(Integer id, Map<String, Object> body) {
        Users u = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (body.containsKey("username")) {
            u.setUsername((String) body.get("username"));
        }
        if (body.containsKey("email")) {
            u.setEmail((String) body.get("email"));
        }
        if (body.containsKey("role")) {
            Object roleObj = body.get("role");
            if (roleObj instanceof Map roleMap && roleMap.containsKey("tenRole")) {
                String roleName = (String) roleMap.get("tenRole");
                rolesRepository.findByTenRole(roleName).ifPresent(u::setRole);
            } else if (roleObj instanceof String roleName) {
                rolesRepository.findByTenRole(roleName).ifPresent(u::setRole);
            }
        }
        usersRepository.save(u);
        return mapUser(u);
    }

    @Transactional
    public Map<String, Object> toggleUserStatus(Integer id) {
        Users u = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Users.TrangThai current = u.getTrangThai();
        u.setTrangThai(current == Users.TrangThai.active ? Users.TrangThai.inactive : Users.TrangThai.active);
        usersRepository.save(u);
        return mapUser(u);
    }

    @Transactional
    public void deleteUser(Integer id) {
        usersRepository.deleteById(id);
    }

    private Map<String, Object> mapUser(Users u) {
        Map<String, Object> m = new HashMap<>();
        m.put("userId", u.getUserId());
        m.put("username", u.getUsername());
        m.put("email", u.getEmail());
        String status = u.getTrangThai() != null ? u.getTrangThai().name() : "active";
        m.put("status", status);
        Map<String, Object> role = new HashMap<>();
        if (u.getRole() != null) {
            role.put("tenRole", u.getRole().getTenRole());
        }
        m.put("role", role);
        return m;
    }

    // ===================== DOCTOR MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getDoctors(Integer page, Integer size, String search, String position, Integer phongbanId) {
        List<BacSi> all = bacSiRepository.findAll();
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            all = all.stream()
                    .filter(bs -> (bs.getHoTen() != null && bs.getHoTen().toLowerCase().contains(q))
                            || (bs.getChuyenKhoa() != null && bs.getChuyenKhoa().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }
        if (phongbanId != null) {
            all = all.stream()
                    .filter(bs -> bs.getPhongban() != null
                            && phongbanId.equals(bs.getPhongban().getPhongbanId()))
                    .collect(Collectors.toList());
        }
        int p = page != null ? page : 0;
        int s = size != null ? size : 20;
        int from = Math.max(p, 0) * Math.max(s, 1);
        int to = Math.min(from + s, all.size());
        List<BacSi> pageList = from >= all.size() ? List.of() : all.subList(from, to);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapDoctor)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalPages", s <= 0 ? 1 : (int) Math.ceil((double) all.size() / s));
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDoctorsByDate(Integer page, Integer size, String search, String position,
                                                Integer phongbanId, String startDate, String endDate) {
        return getDoctors(page, size, search, position, phongbanId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDoctorById(Integer id) {
        BacSi bs = bacSiRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapDoctor(bs);
    }

    @Transactional
    public Map<String, Object> createDoctor(Map<String, Object> body) {
        BacSi bs = new BacSi();
        bs.setHoTen((String) body.get("hoTen"));
        bs.setChuyenKhoa((String) body.get("chuyenKhoa"));
        bs.setSdt((String) body.get("sdt"));
        bs.setEmail((String) body.get("email"));
        Object depId = body.get("phongbanId");
        if (depId instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(bs::setPhongban);
        }
        bacSiRepository.save(bs);
        return mapDoctor(bs);
    }

    @Transactional
    public Map<String, Object> updateDoctor(Integer id, Map<String, Object> body) {
        BacSi bs = bacSiRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        if (body.containsKey("hoTen")) bs.setHoTen((String) body.get("hoTen"));
        if (body.containsKey("chuyenKhoa")) bs.setChuyenKhoa((String) body.get("chuyenKhoa"));
        if (body.containsKey("sdt")) bs.setSdt((String) body.get("sdt"));
        if (body.containsKey("email")) bs.setEmail((String) body.get("email"));
        if (body.containsKey("phongbanId") && body.get("phongbanId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(bs::setPhongban);
        }
        bacSiRepository.save(bs);
        return mapDoctor(bs);
    }

    @Transactional
    public void deleteDoctor(Integer id) {
        bacSiRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> countDoctors() {
        long total = bacSiRepository.count();
        Map<String, Object> m = new HashMap<>();
        m.put("total", total);
        return m;
    }

    private Map<String, Object> mapDoctor(BacSi bs) {
        Map<String, Object> m = new HashMap<>();
        m.put("bacsiId", bs.getBacsiId());
        m.put("hoTen", bs.getHoTen());
        m.put("chuyenKhoa", bs.getChuyenKhoa());
        m.put("sdt", bs.getSdt());
        m.put("email", bs.getEmail());
        m.put("chucVu", "Bác sĩ");
        if (bs.getPhongban() != null) {
            Map<String, Object> pb = new HashMap<>();
            pb.put("phongbanId", bs.getPhongban().getPhongbanId());
            pb.put("tenPhongban", bs.getPhongban().getTenPhongban());
            m.put("phongban", pb);
        } else {
            m.put("phongban", null);
        }
        int soLichKham = bs.getBacsiId() != null
                ? datLichKhamRepository.findByBacsi_BacsiId(bs.getBacsiId()).size()
                : 0;
        int soBenhNhan = (int) datLichKhamRepository.findByBacsi_BacsiId(bs.getBacsiId()).stream()
                .map(dlk -> dlk.getBenhnhan() != null ? dlk.getBenhnhan().getBenhnhanId() : null)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .count();
        m.put("soLichKham", soLichKham);
        m.put("soBenhNhan", soBenhNhan);
        return m;
    }

    // ===================== STAFF MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getStaff(Integer page, Integer size, String search,
                                        String chucVu, Integer phongbanId) {
        List<NhanVien> all = nhanVienRepository.findAll();
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            all = all.stream()
                    .filter(nv -> (nv.getHoTen() != null && nv.getHoTen().toLowerCase().contains(q))
                            || (nv.getChucVu() != null && nv.getChucVu().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }
        if (chucVu != null && !chucVu.isBlank()) {
            all = all.stream()
                    .filter(nv -> chucVu.equalsIgnoreCase(nv.getChucVu()))
                    .collect(Collectors.toList());
        }
        if (phongbanId != null) {
            all = all.stream()
                    .filter(nv -> nv.getPhongban() != null
                            && phongbanId.equals(nv.getPhongban().getPhongbanId()))
                    .collect(Collectors.toList());
        }
        int p = page != null ? page : 0;
        int s = size != null ? size : 20;
        int from = Math.max(p, 0) * Math.max(s, 1);
        int to = Math.min(from + s, all.size());
        List<NhanVien> pageList = from >= all.size() ? List.of() : all.subList(from, to);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapStaff)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalPages", s <= 0 ? 1 : (int) Math.ceil((double) all.size() / s));
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStaffByDate(Integer page, Integer size, String search, String chucVu,
                                              Integer phongbanId, String startDate, String endDate) {
        return getStaff(page, size, search, chucVu, phongbanId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStaffById(Integer id) {
        NhanVien nv = nhanVienRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
        return mapStaff(nv);
    }

    @Transactional
    public Map<String, Object> createStaff(Map<String, Object> body) {
        NhanVien nv = new NhanVien();
        String hoTen = (String) body.get("hoTen");
        if (hoTen == null) hoTen = (String) body.get("ho_ten");
        nv.setHoTen(hoTen);
        
        String chucVu = (String) body.get("chucVu");
        if (chucVu == null) chucVu = (String) body.get("chuc_vu");
        nv.setChucVu(chucVu);
        
        String sdt = (String) body.get("sdt");
        if (sdt == null) sdt = (String) body.get("soDienThoai");
        if (sdt == null) sdt = (String) body.get("so_dien_thoai");
        if (sdt == null) sdt = (String) body.get("phone");
        nv.setSdt(sdt);
        
        String ngayVaoLamStr = (String) body.get("ngayVaoLam");
        if (ngayVaoLamStr == null) ngayVaoLamStr = (String) body.get("ngay_vao_lam");
        if (ngayVaoLamStr == null) ngayVaoLamStr = (String) body.get("hireDate");
        if (ngayVaoLamStr != null && !ngayVaoLamStr.isBlank()) {
            try {
                nv.setNgayVaoLam(java.time.LocalDate.parse(ngayVaoLamStr));
            } catch (Exception ignored) {}
        }
        
        Object luongObj = body.get("luong");
        if (luongObj == null) luongObj = body.get("mucLuong");
        if (luongObj == null) luongObj = body.get("muc_luong");
        if (luongObj == null) luongObj = body.get("salary");
        if (luongObj instanceof Number n) {
            nv.setLuong(java.math.BigDecimal.valueOf(n.doubleValue()));
        }
        
        if (body.containsKey("phongbanId") && body.get("phongbanId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(nv::setPhongban);
        } else if (body.containsKey("phongBanId") && body.get("phongBanId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(nv::setPhongban);
        } else if (body.containsKey("departmentId") && body.get("departmentId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(nv::setPhongban);
        }
        nhanVienRepository.save(nv);
        return mapStaff(nv);
    }

    @Transactional
    public Map<String, Object> updateStaff(Integer id, Map<String, Object> body) {
        NhanVien nv = nhanVienRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
        if (body.containsKey("hoTen")) {
            nv.setHoTen((String) body.get("hoTen"));
        } else if (body.containsKey("ho_ten")) {
            nv.setHoTen((String) body.get("ho_ten"));
        }
        if (body.containsKey("chucVu")) {
            nv.setChucVu((String) body.get("chucVu"));
        } else if (body.containsKey("chuc_vu")) {
            nv.setChucVu((String) body.get("chuc_vu"));
        }
        if (body.containsKey("sdt")) {
            nv.setSdt((String) body.get("sdt"));
        } else if (body.containsKey("soDienThoai")) {
            nv.setSdt((String) body.get("soDienThoai"));
        } else if (body.containsKey("so_dien_thoai")) {
            nv.setSdt((String) body.get("so_dien_thoai"));
        } else if (body.containsKey("phone")) {
            nv.setSdt((String) body.get("phone"));
        }
        if (body.containsKey("ngayVaoLam")) {
            String ngayStr = (String) body.get("ngayVaoLam");
            if (ngayStr != null && !ngayStr.isBlank()) {
                try {
                    nv.setNgayVaoLam(java.time.LocalDate.parse(ngayStr));
                } catch (Exception ignored) {}
            }
        } else if (body.containsKey("ngay_vao_lam")) {
            String ngayStr = (String) body.get("ngay_vao_lam");
            if (ngayStr != null && !ngayStr.isBlank()) {
                try {
                    nv.setNgayVaoLam(java.time.LocalDate.parse(ngayStr));
                } catch (Exception ignored) {}
            }
        } else if (body.containsKey("hireDate")) {
            String ngayStr = (String) body.get("hireDate");
            if (ngayStr != null && !ngayStr.isBlank()) {
                try {
                    nv.setNgayVaoLam(java.time.LocalDate.parse(ngayStr));
                } catch (Exception ignored) {}
            }
        }
        if (body.containsKey("luong")) {
            Object luongObj = body.get("luong");
            if (luongObj instanceof Number n) {
                nv.setLuong(java.math.BigDecimal.valueOf(n.doubleValue()));
            }
        } else if (body.containsKey("mucLuong")) {
            Object luongObj = body.get("mucLuong");
            if (luongObj instanceof Number n) {
                nv.setLuong(java.math.BigDecimal.valueOf(n.doubleValue()));
            }
        } else if (body.containsKey("muc_luong")) {
            Object luongObj = body.get("muc_luong");
            if (luongObj instanceof Number n) {
                nv.setLuong(java.math.BigDecimal.valueOf(n.doubleValue()));
            }
        } else if (body.containsKey("salary")) {
            Object luongObj = body.get("salary");
            if (luongObj instanceof Number n) {
                nv.setLuong(java.math.BigDecimal.valueOf(n.doubleValue()));
            }
        }
        if (body.containsKey("phongbanId") && body.get("phongbanId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(nv::setPhongban);
        } else if (body.containsKey("phongBanId") && body.get("phongBanId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(nv::setPhongban);
        } else if (body.containsKey("departmentId") && body.get("departmentId") instanceof Number n) {
            phongBanRepository.findById(n.intValue()).ifPresent(nv::setPhongban);
        }
        nhanVienRepository.save(nv);
        return mapStaff(nv);
    }

    @Transactional
    public void deleteStaff(Integer id) {
        nhanVienRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> countStaff() {
        long total = nhanVienRepository.count();
        Map<String, Object> m = new HashMap<>();
        m.put("total", total);
        return m;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> countNurses() {
        long total = nhanVienRepository.findByChucVu("Y tá").size();
        Map<String, Object> m = new HashMap<>();
        m.put("total", total);
        return m;
    }

    private Map<String, Object> mapStaff(NhanVien nv) {
        Map<String, Object> m = new HashMap<>();
        m.put("nhanvienId", nv.getNhanvienId());
        m.put("nhanvien_id", nv.getNhanvienId());
        m.put("hoTen", nv.getHoTen());
        m.put("ho_ten", nv.getHoTen());
        m.put("chucVu", nv.getChucVu());
        m.put("chuc_vu", nv.getChucVu());
        m.put("sdt", nv.getSdt());
        m.put("soDienThoai", nv.getSdt());
        m.put("so_dien_thoai", nv.getSdt());
        if (nv.getNgayVaoLam() != null) {
            m.put("ngayVaoLam", nv.getNgayVaoLam().toString());
            m.put("ngay_vao_lam", nv.getNgayVaoLam().toString());
        } else {
            m.put("ngayVaoLam", null);
            m.put("ngay_vao_lam", null);
        }
        if (nv.getLuong() != null) {
            m.put("luong", nv.getLuong());
            m.put("mucLuong", nv.getLuong());
            m.put("muc_luong", nv.getLuong());
        } else {
            m.put("luong", null);
            m.put("mucLuong", null);
            m.put("muc_luong", null);
        }
        if (nv.getPhongban() != null) {
            Map<String, Object> pb = new HashMap<>();
            pb.put("phongbanId", nv.getPhongban().getPhongbanId());
            pb.put("tenPhongban", nv.getPhongban().getTenPhongban());
            m.put("phongban", pb);
            m.put("tenPhongBan", nv.getPhongban().getTenPhongban());
        } else {
            m.put("phongban", null);
            m.put("tenPhongBan", null);
        }
        m.put("trangThai", null);
        m.put("trang_thai", null);
        return m;
    }

    // ===================== DEPARTMENT MANAGEMENT =====================

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDepartments() {
        return phongBanRepository.findAll().stream()
                .map(this::mapDepartment)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDepartmentById(Integer id) {
        PhongBan pb = phongBanRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        return mapDepartment(pb);
    }

    @Transactional
    public Map<String, Object> createDepartment(Map<String, Object> body) {
        PhongBan pb = new PhongBan();
        String tenPhongban = (String) body.get("tenPhongban");
        if (tenPhongban == null) {
            tenPhongban = (String) body.get("tenPhongBan");
        }
        if (tenPhongban == null) {
            tenPhongban = (String) body.get("name");
        }
        pb.setTenPhongban(tenPhongban);
        pb.setMoTa((String) body.get("moTa"));
        phongBanRepository.save(pb);
        return mapDepartment(pb);
    }

    @Transactional
    public Map<String, Object> updateDepartment(Integer id, Map<String, Object> body) {
        PhongBan pb = phongBanRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        if (body.containsKey("tenPhongban")) {
            pb.setTenPhongban((String) body.get("tenPhongban"));
        } else if (body.containsKey("tenPhongBan")) {
            pb.setTenPhongban((String) body.get("tenPhongBan"));
        } else if (body.containsKey("name")) {
            pb.setTenPhongban((String) body.get("name"));
        }
        if (body.containsKey("moTa")) {
            pb.setMoTa((String) body.get("moTa"));
        }
        phongBanRepository.save(pb);
        return mapDepartment(pb);
    }

    @Transactional
    public void deleteDepartment(Integer id) {
        phongBanRepository.deleteById(id);
    }

    private Map<String, Object> mapDepartment(PhongBan pb) {
        Map<String, Object> m = new HashMap<>();
        m.put("phongbanId", pb.getPhongbanId());
        m.put("departmentId", pb.getPhongbanId());
        m.put("id", pb.getPhongbanId());
        m.put("tenPhongban", pb.getTenPhongban());
        m.put("tenPhongBan", pb.getTenPhongban());
        m.put("tenKhoa", pb.getTenPhongban());
        m.put("name", pb.getTenPhongban());
        m.put("moTa", pb.getMoTa());
        return m;
    }

    // ===================== MEDICINE MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getMedicines(Integer page, Integer size, String search,
                                             String nhaSanXuat, String nhomThuoc,
                                             String dangBaoChe, String expiringBefore,
                                             String sortField, String sortDir) {
        List<Thuoc> all = thuocRepository.findAll();

        // Apply search filter
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            all = all.stream()
                    .filter(t -> (t.getTenThuoc() != null && t.getTenThuoc().toLowerCase().contains(q))
                            || (t.getHoatChat() != null && t.getHoatChat().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }

        // Apply filters
        if (nhaSanXuat != null && !nhaSanXuat.isBlank()) {
            String q = nhaSanXuat.toLowerCase();
            all = all.stream()
                    .filter(t -> t.getNhaSanXuat() != null && t.getNhaSanXuat().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        if (dangBaoChe != null && !dangBaoChe.isBlank()) {
            all = all.stream()
                    .filter(t -> dangBaoChe.equals(t.getDangBaoChe()))
                    .collect(Collectors.toList());
        }

        if (expiringBefore != null && !expiringBefore.isBlank()) {
            try {
                java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiringBefore);
                all = all.stream()
                        .filter(t -> t.getHanSuDung() != null
                                && !t.getHanSuDung().isBefore(java.time.LocalDate.now())
                                && !t.getHanSuDung().isAfter(expiryDate))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        // Apply sorting
        String sort = sortField != null ? sortField : "thuocId";
        boolean asc = sortDir == null || "asc".equalsIgnoreCase(sortDir);
        all.sort((a, b) -> {
            int result = 0;
            switch (sort.toLowerCase()) {
                case "thuocid":
                case "id":
                    result = Integer.compare(a.getThuocId(), b.getThuocId());
                    break;
                case "tenthuoc":
                case "name":
                    result = (a.getTenThuoc() != null ? a.getTenThuoc() : "")
                            .compareToIgnoreCase(b.getTenThuoc() != null ? b.getTenThuoc() : "");
                    break;
                case "hansudung":
                case "expirydate":
                    if (a.getHanSuDung() != null && b.getHanSuDung() != null) {
                        result = a.getHanSuDung().compareTo(b.getHanSuDung());
                    } else if (a.getHanSuDung() != null) {
                        result = 1;
                    } else if (b.getHanSuDung() != null) {
                        result = -1;
                    }
                    break;
                default:
                    result = Integer.compare(a.getThuocId(), b.getThuocId());
            }
            return asc ? result : -result;
        });

        // Apply pagination
        int p = page != null ? Math.max(page, 0) : 0;
        int s = size != null ? Math.max(size, 1) : 20;
        int fromIndex = p * s;
        int toIndex = Math.min(fromIndex + s, all.size());
        List<Thuoc> pageList = fromIndex >= all.size() ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapMedicine)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", all.size());
        result.put("totalPages", s <= 0 ? 1 : (int) Math.ceil((double) all.size() / s));
        result.put("number", p);
        result.put("size", s);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMedicineStats() {
        long total = thuocRepository.count();
        long outOfStock = khoThuocRepository.countOutOfStockMedicines();
        long lowStock = khoThuocRepository.findLowStockMedicines(10).size();
        java.time.LocalDate expiryDate = java.time.LocalDate.now().plusDays(90);
        long expiringSoon = thuocRepository.findMedicinesExpiringSoon(expiryDate).size();

        Map<String, Object> m = new HashMap<>();
        m.put("total", total);
        m.put("outOfStock", outOfStock);
        m.put("lowStock", lowStock);
        m.put("expiringSoon", expiringSoon);
        return m;
    }

    @Transactional(readOnly = true)
    public List<String> getDosageForms() {
        return thuocRepository.findAllDangBaoChe();
    }

    @Transactional(readOnly = true)
    public List<String> getGroups() {
        // Return empty list - groups should come from database or be configured
        return List.of();
    }

    private Map<String, Object> mapMedicine(Thuoc t) {
        Map<String, Object> m = new HashMap<>();
        m.put("thuocId", t.getThuocId());
        m.put("id", t.getThuocId());
        m.put("maThuoc", "T" + String.format("%03d", t.getThuocId()));
        m.put("tenThuoc", t.getTenThuoc());
        m.put("name", t.getTenThuoc());
        m.put("hoatChat", t.getHoatChat());
        m.put("genericName", t.getHoatChat());
        m.put("hamLuong", t.getHamLuong());
        m.put("dangBaoChe", t.getDangBaoChe());
        m.put("dosageForm", t.getDangBaoChe());
        m.put("nhaSanXuat", t.getNhaSanXuat());
        m.put("nhaCungCap", t.getNhaSanXuat());
        m.put("supplier", t.getNhaSanXuat());
        if (t.getHanSuDung() != null) {
            m.put("hanSuDung", t.getHanSuDung().toString());
            m.put("expiryDate", t.getHanSuDung().toString());
        } else {
            m.put("hanSuDung", null);
            m.put("expiryDate", null);
        }

        // Get stock information from KhoThuoc
        java.util.Optional<KhoThuoc> khoOpt = khoThuocRepository.findByThuoc_ThuocId(t.getThuocId());
        int tonKho = khoOpt.map(KhoThuoc::getSoLuong).orElse(0);
        m.put("tonKhoHienTai", tonKho);
        m.put("currentStock", tonKho);
        m.put("tonKhoToiThieu", null);
        m.put("minStock", null);
        m.put("tonKhoToiDa", null);
        m.put("maxStock", null);

        // Get price from DonHangChiTiet (latest price)
        // This is a simplified approach - you might want to add a price field to Thuoc entity
        m.put("donGia", null);
        m.put("unitPrice", null);
        m.put("donVi", null);
        m.put("unit", null);
        m.put("trangThai", null);
        m.put("status", null);
        m.put("yeuCauKeDon", null);
        m.put("prescriptionRequired", null);

        return m;
    }

    // ===================== PATIENT MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getPatients(Integer page, Integer size, String search,
                                            String gender, String status) {
        List<BenhNhan> all = benhNhanRepository.findAll();

        // Apply search filter
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            all = all.stream()
                    .filter(bn -> (bn.getHoTen() != null && bn.getHoTen().toLowerCase().contains(q))
                            || (bn.getSdt() != null && bn.getSdt().contains(q))
                            || (bn.getEmail() != null && bn.getEmail().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }

        // Apply gender filter
        if (gender != null && !gender.isBlank()) {
            try {
                BenhNhan.GioiTinh gioiTinh = BenhNhan.GioiTinh.valueOf(gender);
                all = all.stream()
                        .filter(bn -> gioiTinh.equals(bn.getGioiTinh()))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException ignored) {
                // Invalid gender value, ignore filter
            }
        }

        // Apply status filter (dangDieuTri, khoiBenh)
        if (status != null && !status.isBlank()) {
            // Check if patient has active medical records via HoSoKham
            if ("dangDieuTri".equalsIgnoreCase(status)) {
                all = all.stream()
                        .filter(bn -> {
                            if (bn.getDanhSachHoSoKham() == null) return false;
                            // Check if patient has recent HoSoKham records (within last 30 days)
                            java.time.LocalDate thirtyDaysAgo = java.time.LocalDate.now().minusDays(30);
                            return bn.getDanhSachHoSoKham().stream()
                                    .anyMatch(hsk -> hsk.getNgayKham() != null
                                            && !hsk.getNgayKham().isBefore(thirtyDaysAgo));
                        })
                        .collect(Collectors.toList());
            } else if ("khoiBenh".equalsIgnoreCase(status)) {
                all = all.stream()
                        .filter(bn -> {
                            if (bn.getDanhSachHoSoKham() == null) return false;
                            // Check if patient has old HoSoKham records (older than 30 days) but no recent ones
                            java.time.LocalDate thirtyDaysAgo = java.time.LocalDate.now().minusDays(30);
                            boolean hasOldRecords = bn.getDanhSachHoSoKham().stream()
                                    .anyMatch(hsk -> hsk.getNgayKham() != null
                                            && hsk.getNgayKham().isBefore(thirtyDaysAgo));
                            boolean hasRecentRecords = bn.getDanhSachHoSoKham().stream()
                                    .anyMatch(hsk -> hsk.getNgayKham() != null
                                            && !hsk.getNgayKham().isBefore(thirtyDaysAgo));
                            return hasOldRecords && !hasRecentRecords;
                        })
                        .collect(Collectors.toList());
            }
        }

        // Apply pagination
        int p = page != null ? Math.max(page, 0) : 0;
        int s = size != null ? Math.max(size, 1) : 20;
        int fromIndex = p * s;
        int toIndex = Math.min(fromIndex + s, all.size());
        List<BenhNhan> pageList = fromIndex >= all.size() ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapPatient)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", all.size());
        result.put("totalPages", s <= 0 ? 1 : (int) Math.ceil((double) all.size() / s));
        result.put("number", p);
        result.put("size", s);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPatientStats() {
        long total = benhNhanRepository.count();
        long male = benhNhanRepository.countByGioiTinh(BenhNhan.GioiTinh.Nam);
        long female = benhNhanRepository.countByGioiTinh(BenhNhan.GioiTinh.Nữ);
        long other = benhNhanRepository.countByGioiTinh(BenhNhan.GioiTinh.Khác);

        // Count patients with active treatment (recent HoSoKham records within 30 days)
        java.time.LocalDate thirtyDaysAgo = java.time.LocalDate.now().minusDays(30);
        long dangDieuTri = benhNhanRepository.findAll().stream()
                .filter(bn -> {
                    if (bn.getDanhSachHoSoKham() == null) return false;
                    return bn.getDanhSachHoSoKham().stream()
                            .anyMatch(hsk -> hsk.getNgayKham() != null
                                    && !hsk.getNgayKham().isBefore(thirtyDaysAgo));
                })
                .count();

        // Count patients who recovered (have old records but no recent ones)
        long khoiBenh = benhNhanRepository.findAll().stream()
                .filter(bn -> {
                    if (bn.getDanhSachHoSoKham() == null) return false;
                    boolean hasOldRecords = bn.getDanhSachHoSoKham().stream()
                            .anyMatch(hsk -> hsk.getNgayKham() != null
                                    && hsk.getNgayKham().isBefore(thirtyDaysAgo));
                    boolean hasRecentRecords = bn.getDanhSachHoSoKham().stream()
                            .anyMatch(hsk -> hsk.getNgayKham() != null
                                    && !hsk.getNgayKham().isBefore(thirtyDaysAgo));
                    return hasOldRecords && !hasRecentRecords;
                })
                .count();

        Map<String, Object> m = new HashMap<>();
        m.put("total", total);
        m.put("male", male);
        m.put("female", female);
        m.put("other", other);
        m.put("dangDieuTri", dangDieuTri);
        m.put("khoiBenh", khoiBenh);
        return m;
    }

    private Map<String, Object> mapPatient(BenhNhan bn) {
        Map<String, Object> m = new HashMap<>();
        m.put("benhnhanId", bn.getBenhnhanId());
        m.put("id", bn.getBenhnhanId());
        m.put("hoTen", bn.getHoTen());
        m.put("name", bn.getHoTen());
        if (bn.getNgaySinh() != null) {
            m.put("ngaySinh", bn.getNgaySinh().toString());
            m.put("dateOfBirth", bn.getNgaySinh().toString());
        } else {
            m.put("ngaySinh", null);
            m.put("dateOfBirth", null);
        }
        if (bn.getGioiTinh() != null) {
            m.put("gioiTinh", bn.getGioiTinh().toString());
            m.put("gender", bn.getGioiTinh().toString());
        } else {
            m.put("gioiTinh", "Khác");
            m.put("gender", "Khác");
        }
        m.put("sdt", bn.getSdt());
        m.put("phone", bn.getSdt());
        m.put("email", bn.getEmail());
        m.put("diaChi", bn.getDiaChi());
        m.put("address", bn.getDiaChi());

        // Calculate age
        if (bn.getNgaySinh() != null) {
            int age = java.time.LocalDate.now().getYear() - bn.getNgaySinh().getYear();
            if (java.time.LocalDate.now().getDayOfYear() < bn.getNgaySinh().getDayOfYear()) {
                age--;
            }
            m.put("age", age);
        } else {
            m.put("age", null);
        }

        // Get status from medical records (HoSoKham)
        String status = null;
        if (bn.getDanhSachHoSoKham() != null && !bn.getDanhSachHoSoKham().isEmpty()) {
            // Status should come from database, not hardcoded
            status = null;
        }
        m.put("status", status);
        m.put("trangThai", status);

        // Count appointments
        int appointmentCount = bn.getDanhSachDatLich() != null ? bn.getDanhSachDatLich().size() : 0;
        m.put("appointmentCount", appointmentCount);
        m.put("soLichKham", appointmentCount);

        return m;
    }

    // ===================== PRESCRIPTION MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getPrescriptions(Integer page, Integer size, String search,
                                                 Integer doctorId, Integer patientId) {
        List<DonThuoc> all = donThuocRepository.findAll();

        // Apply search filter
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            all = all.stream()
                    .filter(dt -> {
                        if (dt.getBenhnhan() != null && dt.getBenhnhan().getHoTen() != null
                                && dt.getBenhnhan().getHoTen().toLowerCase().contains(q)) {
                            return true;
                        }
                        if (dt.getBacsi() != null && dt.getBacsi().getHoTen() != null
                                && dt.getBacsi().getHoTen().toLowerCase().contains(q)) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        // Apply doctor filter
        if (doctorId != null) {
            all = all.stream()
                    .filter(dt -> dt.getBacsi() != null && doctorId.equals(dt.getBacsi().getBacsiId()))
                    .collect(Collectors.toList());
        }

        // Apply patient filter
        if (patientId != null) {
            all = all.stream()
                    .filter(dt -> dt.getBenhnhan() != null && patientId.equals(dt.getBenhnhan().getBenhnhanId()))
                    .collect(Collectors.toList());
        }

        // Apply pagination
        int p = page != null ? Math.max(page, 0) : 0;
        int s = size != null ? Math.max(size, 1) : 20;
        int fromIndex = p * s;
        int toIndex = Math.min(fromIndex + s, all.size());
        List<DonThuoc> pageList = fromIndex >= all.size() ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapPrescription)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", all.size());
        result.put("totalPages", s <= 0 ? 1 : (int) Math.ceil((double) all.size() / s));
        result.put("number", p);
        result.put("size", s);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPrescriptionDetail(Integer id) {
        DonThuoc dt = donThuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return mapPrescriptionDetail(dt);
    }

    private Map<String, Object> mapPrescription(DonThuoc dt) {
        Map<String, Object> m = new HashMap<>();
        m.put("donthuocId", dt.getDonthuocId());
        m.put("id", dt.getDonthuocId());
        m.put("prescriptionCode", "DT" + String.format("%03d", dt.getDonthuocId()));
        if (dt.getNgayKe() != null) {
            m.put("ngayKe", dt.getNgayKe().toString());
            m.put("prescriptionDate", dt.getNgayKe().toString());
        }
        m.put("ghiChu", dt.getGhiChu());
        m.put("notes", dt.getGhiChu());

        if (dt.getBenhnhan() != null) {
            m.put("patientId", dt.getBenhnhan().getBenhnhanId());
            m.put("patientName", dt.getBenhnhan().getHoTen());
        } else {
            m.put("patientId", null);
            m.put("patientName", null);
        }

        if (dt.getBacsi() != null) {
            m.put("doctorId", dt.getBacsi().getBacsiId());
            m.put("doctorName", dt.getBacsi().getHoTen());
        } else {
            m.put("doctorId", null);
            m.put("doctorName", null);
        }

        // Calculate total amount from chi tiet
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (dt.getDanhSachChiTiet() != null) {
            for (DonThuocChiTiet ct : dt.getDanhSachChiTiet()) {
                BigDecimal gia = ct.getDonGia() != null ? ct.getDonGia() : BigDecimal.ZERO;
                int sl = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                totalAmount = totalAmount.add(gia.multiply(BigDecimal.valueOf(sl)));
            }
        }
        m.put("totalAmount", totalAmount);
        m.put("status", null); // Status should come from database

        return m;
    }

    private Map<String, Object> mapPrescriptionDetail(DonThuoc dt) {
        Map<String, Object> m = mapPrescription(dt);

        // Add medicine details
        List<Map<String, Object>> medicines = new java.util.ArrayList<>();
        if (dt.getDanhSachChiTiet() != null) {
            for (DonThuocChiTiet ct : dt.getDanhSachChiTiet()) {
                Map<String, Object> med = new HashMap<>();
                if (ct.getThuoc() != null) {
                    med.put("id", ct.getThuoc().getThuocId());
                    med.put("name", ct.getThuoc().getTenThuoc());
                }
                med.put("quantity", ct.getSoLuong());
                med.put("unit", null); // Should come from database
                med.put("unitPrice", ct.getDonGia());
                med.put("totalPrice", ct.getDonGia() != null && ct.getSoLuong() != null
                        ? ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong()))
                        : BigDecimal.ZERO);
                med.put("dosage", ct.getLieuDung());
                med.put("notes", null);
                medicines.add(med);
            }
        }
        m.put("medicines", medicines);

        return m;
    }

    // ===================== LAB TEST MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getLabTests(Integer page, Integer size, String search,
                                            Integer doctorId, Integer patientId) {
        List<LabTest> all = labTestRepository.findAll();

        // Apply search filter
        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            all = all.stream()
                    .filter(lt -> {
                        if (lt.getLoaiXetNghiem() != null && lt.getLoaiXetNghiem().toLowerCase().contains(q)) {
                            return true;
                        }
                        if (lt.getBenhnhan() != null && lt.getBenhnhan().getHoTen() != null
                                && lt.getBenhnhan().getHoTen().toLowerCase().contains(q)) {
                            return true;
                        }
                        if (lt.getBacsi() != null && lt.getBacsi().getHoTen() != null
                                && lt.getBacsi().getHoTen().toLowerCase().contains(q)) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        // Apply doctor filter
        if (doctorId != null) {
            all = all.stream()
                    .filter(lt -> lt.getBacsi() != null && doctorId.equals(lt.getBacsi().getBacsiId()))
                    .collect(Collectors.toList());
        }

        // Apply patient filter
        if (patientId != null) {
            all = all.stream()
                    .filter(lt -> lt.getBenhnhan() != null && patientId.equals(lt.getBenhnhan().getBenhnhanId()))
                    .collect(Collectors.toList());
        }

        // Apply pagination
        int p = page != null ? Math.max(page, 0) : 0;
        int s = size != null ? Math.max(size, 1) : 20;
        int fromIndex = p * s;
        int toIndex = Math.min(fromIndex + s, all.size());
        List<LabTest> pageList = fromIndex >= all.size() ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapLabTest)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", all.size());
        result.put("totalPages", s <= 0 ? 1 : (int) Math.ceil((double) all.size() / s));
        result.put("number", p);
        result.put("size", s);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getLabTestDetail(Integer id) {
        LabTest lt = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));
        return mapLabTestDetail(lt);
    }

    private Map<String, Object> mapLabTest(LabTest lt) {
        Map<String, Object> m = new HashMap<>();
        m.put("labtestId", lt.getLabtestId());
        m.put("id", lt.getLabtestId());
        m.put("testCode", "XN" + String.format("%03d", lt.getLabtestId()));
        if (lt.getNgayTest() != null) {
            m.put("ngayTest", lt.getNgayTest().toString());
            m.put("requestDate", lt.getNgayTest().toString());
        }
        m.put("loaiXetNghiem", lt.getLoaiXetNghiem());
        m.put("testType", lt.getLoaiXetNghiem());
        m.put("ketQua", lt.getKetQua());
        m.put("result", lt.getKetQua());

        if (lt.getBenhnhan() != null) {
            m.put("patientId", lt.getBenhnhan().getBenhnhanId());
            m.put("patientName", lt.getBenhnhan().getHoTen());
        } else {
            m.put("patientId", null);
            m.put("patientName", null);
        }

        if (lt.getBacsi() != null) {
            m.put("doctorId", lt.getBacsi().getBacsiId());
            m.put("doctorName", lt.getBacsi().getHoTen());
        } else {
            m.put("doctorId", null);
            m.put("doctorName", null);
        }

        // Status based on result
        String status = lt.getKetQua() != null && !lt.getKetQua().isBlank() ? "Hoàn thành" : "Đang xử lý";
        m.put("status", status);
        m.put("completedDate", lt.getKetQua() != null && !lt.getKetQua().isBlank() ? lt.getNgayTest() != null ? lt.getNgayTest().toString() : null : null);
        m.put("technician", null); // Should come from database
        m.put("notes", null); // Should come from database

        return m;
    }

    private Map<String, Object> mapLabTestDetail(LabTest lt) {
        Map<String, Object> m = mapLabTest(lt);

        // Parse test results if available
        List<Map<String, Object>> tests = new java.util.ArrayList<>();
        if (lt.getKetQua() != null && !lt.getKetQua().isBlank()) {
            // Simple parsing - can be enhanced based on actual format
            String[] lines = lt.getKetQua().split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                Map<String, Object> test = new HashMap<>();
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    test.put("name", parts[0].trim());
                    String resultPart = parts[1].trim();
                    String[] resultParts = resultPart.split("\\s+");
                    if (resultParts.length > 0) {
                        test.put("result", resultParts[0]);
                    }
                    test.put("unit", resultParts.length > 1 ? resultParts[1] : "");
                    test.put("reference", resultParts.length > 2 ? resultParts[2] : "");
                } else {
                    test.put("name", line.trim());
                    test.put("result", "");
                    test.put("unit", "");
                    test.put("reference", "");
                }
                tests.add(test);
            }
        }
        m.put("tests", tests);

        return m;
    }

    // ===================== INVOICE MANAGEMENT =====================

    @Transactional(readOnly = true)
    public Map<String, Object> getInvoices(Integer page, Integer size, String search, String status) {
        List<HoaDon> all;
        
        if (status != null && !status.isBlank()) {
            try {
                HoaDon.TrangThai trangThai = HoaDon.TrangThai.valueOf(status.toLowerCase());
                all = hoaDonRepository.findByTrangThai(trangThai);
            } catch (IllegalArgumentException e) {
                all = hoaDonRepository.findAll();
            }
        } else {
            all = hoaDonRepository.findAll();
        }

        // Filter by search term if provided
        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();
            all = all.stream()
                    .filter(hd -> {
                        if (hd.getBenhnhan() != null) {
                            return hd.getBenhnhan().getHoTen().toLowerCase().contains(searchLower) ||
                                   String.valueOf(hd.getHoadonId()).contains(searchLower);
                        }
                        return String.valueOf(hd.getHoadonId()).contains(searchLower);
                    })
                    .collect(Collectors.toList());
        }

        int totalElements = all.size();
        int pageNum = (page != null && page >= 0) ? page : 0;
        int sizeNum = (size != null && size > 0) ? size : 20;
        int fromIndex = pageNum * sizeNum;
        int toIndex = Math.min(fromIndex + sizeNum, totalElements);
        List<HoaDon> pageList = fromIndex >= totalElements ? List.of() : all.subList(fromIndex, toIndex);

        List<Map<String, Object>> content = pageList.stream()
                .map(this::mapInvoice)
                .collect(Collectors.toList());

        int totalPages = sizeNum <= 0 ? 1 : (int) Math.ceil((double) totalElements / sizeNum);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("number", pageNum);
        result.put("size", sizeNum);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getInvoiceDetail(Integer id) {
        HoaDon hd = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return mapInvoiceDetail(hd);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getInvoiceStats() {
        long totalInvoices = hoaDonRepository.count();
        long paidInvoices = hoaDonRepository.countByTrangThai(HoaDon.TrangThai.da_thanh_toan);
        long unpaidInvoices = hoaDonRepository.countByTrangThai(HoaDon.TrangThai.chua_thanh_toan);
        
        BigDecimal totalRevenue = hoaDonRepository.sumByTrangThai(HoaDon.TrangThai.da_thanh_toan);
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInvoices", totalInvoices);
        stats.put("paidInvoices", paidInvoices);
        stats.put("unpaidInvoices", unpaidInvoices);
        stats.put("totalRevenue", totalRevenue);
        return stats;
    }

    private Map<String, Object> mapInvoice(HoaDon hd) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", hd.getHoadonId());
        m.put("invoiceCode", "HD" + String.format("%03d", hd.getHoadonId()));
        m.put("invoiceDate", hd.getNgayLap() != null ? hd.getNgayLap().toString() : null);
        m.put("total", hd.getTongTien() != null ? hd.getTongTien() : BigDecimal.ZERO);
        
        // Map status
        String status = "Chưa thanh toán";
        if (hd.getTrangThai() == HoaDon.TrangThai.da_thanh_toan) {
            status = "Đã thanh toán";
        }
        m.put("status", status);
        
        // Map patient info
        if (hd.getBenhnhan() != null) {
            m.put("patientId", hd.getBenhnhan().getBenhnhanId());
            m.put("patientName", hd.getBenhnhan().getHoTen());
        }
        
        // Calculate paid and balance (simplified - assuming full payment if status is paid)
        BigDecimal total = hd.getTongTien() != null ? hd.getTongTien() : BigDecimal.ZERO;
        BigDecimal paid = hd.getTrangThai() == HoaDon.TrangThai.da_thanh_toan ? total : BigDecimal.ZERO;
        BigDecimal balance = total.subtract(paid);
        
        m.put("paid", paid);
        m.put("balance", balance);
        
        return m;
    }

    private Map<String, Object> mapInvoiceDetail(HoaDon hd) {
        Map<String, Object> m = mapInvoice(hd);
        
        // Add additional detail fields
        m.put("dueDate", hd.getNgayLap() != null ? hd.getNgayLap().plusDays(7).toString() : null);
        m.put("paymentMethod", ""); // Not stored in entity
        m.put("notes", ""); // Not stored in entity
        
        // Services list (empty for now - could be populated from related entities)
        m.put("services", List.of());
        m.put("subtotal", hd.getTongTien() != null ? hd.getTongTien() : BigDecimal.ZERO);
        m.put("discount", BigDecimal.ZERO);
        m.put("tax", BigDecimal.ZERO);
        
        return m;
    }
}
