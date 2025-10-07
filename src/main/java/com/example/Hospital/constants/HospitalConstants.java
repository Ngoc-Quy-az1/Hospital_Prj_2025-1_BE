package com.example.Hospital.constants;

/**
 * Class tổng hợp tất cả các constants trong hệ thống bệnh viện
 * Đây là nơi tập trung các giá trị constant để dễ quản lý và sử dụng
 */
public final class HospitalConstants {
    
    // =============================
    // GIỚI TÍNH
    // =============================
    public static final String GIOI_TINH_NAM = "Nam";
    public static final String GIOI_TINH_NU = "Nữ";
    public static final String GIOI_TINH_KHAC = "Khác";
    
    // =============================
    // TRẠNG THÁI ĐẶT LỊCH KHÁM
    // =============================
    public static final String DAT_LICH_CHO_DUYET = "cho_duyet";
    public static final String DAT_LICH_DA_DUYET = "da_duyet";
    public static final String DAT_LICH_DA_KHAM = "da_kham";
    public static final String DAT_LICH_HUY = "huy";
    
    // =============================
    // TRẠNG THÁI PHẪU THUẬT
    // =============================
    public static final String PHAU_THUAT_CHO_DUYET = "cho_duyet";
    public static final String PHAU_THUAT_DA_DUYET = "da_duyet";
    public static final String PHAU_THUAT_TU_CHOI = "tu_choi";
    public static final String PHAU_THUAT_HOAN_THANH = "hoan_thanh";
    
    // =============================
    // TRẠNG THÁI HÓA ĐƠN
    // =============================
    public static final String HOA_DON_CHUA_THANH_TOAN = "chua_thanh_toan";
    public static final String HOA_DON_DA_THANH_TOAN = "da_thanh_toan";
    
    // =============================
    // LOẠI BÁO CÁO Y TẾ
    // =============================
    public static final String BAO_CAO_HOAT_DONG = "hoat_dong";
    public static final String BAO_CAO_TAI_CHINH = "tai_chinh";
    public static final String BAO_CAO_NHAN_SU = "nhan_su";
    public static final String BAO_CAO_BENH_NHAN = "benh_nhan";
    public static final String BAO_CAO_PHAU_THUAT = "phau_thuat";
    
    // =============================
    // TRẠNG THÁI NGƯỜI DÙNG
    // =============================
    public static final String USER_ACTIVE = "active";
    public static final String USER_INACTIVE = "inactive";
    
    // =============================
    // TRẠNG THÁI ĐƠN HÀNG THUỐC
    // =============================
    public static final String DON_HANG_CHO_XU_LY = "cho_xu_ly";
    public static final String DON_HANG_DANG_GIAO = "dang_giao";
    public static final String DON_HANG_DA_GIAO = "da_giao";
    public static final String DON_HANG_HUY = "huy";
    
    // =============================
    // LOẠI GIAO DỊCH THANH TOÁN
    // =============================
    public static final String GIAO_DICH_VIEN_PHI = "vien_phi";
    public static final String GIAO_DICH_THUOC_ONLINE = "thuoc_online";
    
    // =============================
    // PHƯƠNG THỨC THANH TOÁN
    // =============================
    public static final String THANH_TOAN_TIEN_MAT = "tien_mat";
    public static final String THANH_TOAN_THE_TIN_DUNG = "the_tin_dung";
    public static final String THANH_TOAN_CHUYEN_KHOAN = "chuyen_khoan";
    public static final String THANH_TOAN_VI_DIEN_TU = "vi_dien_tu";
    
    // =============================
    // TRẠNG THÁI THANH TOÁN
    // =============================
    public static final String THANH_TOAN_CHO_XU_LY = "cho_xu_ly";
    public static final String THANH_TOAN_THANH_CONG = "thanh_cong";
    public static final String THANH_TOAN_THAT_BAI = "that_bai";
    
    // =============================
    // VAI TRÒ NGƯỜI DÙNG
    // =============================
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_DOCTOR = "doctor";
    public static final String ROLE_PATIENT = "patient";
    public static final String ROLE_ACCOUNTANT = "accountant";
    public static final String ROLE_DIRECTOR = "director";
    
    // =============================
    // THÔNG BÁO VÀ MESSAGES
    // =============================
    public static final String SUCCESS_MESSAGE = "Thành công";
    public static final String ERROR_MESSAGE = "Có lỗi xảy ra";
    public static final String VALIDATION_ERROR = "Dữ liệu không hợp lệ";
    public static final String NOT_FOUND_MESSAGE = "Không tìm thấy dữ liệu";
    
    // =============================
    // ĐỊNH DẠNG NGÀY THÁNG
    // =============================
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    
    // =============================
    // GIỚI HẠN DỮ LIỆU
    // =============================
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 50;
    
    private HospitalConstants() {
        // Private constructor để ngăn việc khởi tạo class
        throw new UnsupportedOperationException("Utility class");
    }
}





