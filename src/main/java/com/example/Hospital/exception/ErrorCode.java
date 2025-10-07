package com.example.Hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Enum định nghĩa các mã lỗi và thông báo trong hệ thống bệnh viện
 */
public enum ErrorCode {
    // =============================
    // LỖI CHUNG
    // =============================
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_DATA(1002, "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI NGƯỜI DÙNG
    // =============================
    USER_EXISTED(1003, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "Tên đăng nhập phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    WRONG_PASSWORD(1009, "Mật khẩu không đúng", HttpStatus.BAD_REQUEST),
    USER_NOT_AUTHORIZED(1010, "Người dùng không được phép", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI VAI TRÒ VÀ PHÂN QUYỀN
    // =============================
    ROLE_EXISTED(1011, "Vai trò đã tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1012, "Vai trò không tồn tại", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1013, "Quyền đã tồn tại", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1014, "Quyền không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI PHÒNG BAN
    // =============================
    PHONG_BAN_EXISTED(1015, "Phòng ban đã tồn tại", HttpStatus.BAD_REQUEST),
    PHONG_BAN_NOT_EXISTED(1016, "Phòng ban không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI NHÂN VIÊN
    // =============================
    NHAN_VIEN_EXISTED(1017, "Nhân viên đã tồn tại", HttpStatus.BAD_REQUEST),
    NHAN_VIEN_NOT_EXISTED(1018, "Nhân viên không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_LUONG(1019, "Mức lương không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI BÁC SĨ
    // =============================
    BAC_SI_EXISTED(1020, "Bác sĩ đã tồn tại", HttpStatus.BAD_REQUEST),
    BAC_SI_NOT_EXISTED(1021, "Bác sĩ không tồn tại", HttpStatus.NOT_FOUND),
    CHUYEN_KHOA_NOT_EXISTED(1022, "Chuyên khoa không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI BỆNH NHÂN
    // =============================
    BENH_NHAN_EXISTED(1023, "Bệnh nhân đã tồn tại", HttpStatus.BAD_REQUEST),
    BENH_NHAN_NOT_EXISTED(1024, "Bệnh nhân không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_DOB(1025, "Tuổi phải ít nhất {min} tuổi", HttpStatus.BAD_REQUEST),
    INVALID_GIOI_TINH(1026, "Giới tính không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI ĐẶT LỊCH KHÁM
    // =============================
    DAT_LICH_EXISTED(1027, "Lịch khám đã tồn tại", HttpStatus.BAD_REQUEST),
    DAT_LICH_NOT_EXISTED(1028, "Lịch khám không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_DAT_LICH_TIME(1029, "Thời gian đặt lịch không hợp lệ", HttpStatus.BAD_REQUEST),
    DAT_LICH_EXPIRED(1030, "Lịch khám đã hết hạn", HttpStatus.BAD_REQUEST),
    BAC_SI_BUSY(1031, "Bác sĩ đang bận trong thời gian này", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI HỒ SƠ KHÁM
    // =============================
    HO_SO_KHAM_EXISTED(1032, "Hồ sơ khám đã tồn tại", HttpStatus.BAD_REQUEST),
    HO_SO_KHAM_NOT_EXISTED(1033, "Hồ sơ khám không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI LỊCH TRỰC BAN
    // =============================
    LICH_TRUC_EXISTED(1034, "Lịch trực đã tồn tại", HttpStatus.BAD_REQUEST),
    LICH_TRUC_NOT_EXISTED(1035, "Lịch trực không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_TRUC_TIME(1036, "Thời gian trực không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI THUỐC
    // =============================
    THUOC_EXISTED(1037, "Thuốc đã tồn tại", HttpStatus.BAD_REQUEST),
    THUOC_NOT_EXISTED(1038, "Thuốc không tồn tại", HttpStatus.NOT_FOUND),
    THUOC_HET_HAN(1039, "Thuốc đã hết hạn sử dụng", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI ĐỠN THUỐC
    // =============================
    DON_THUOC_EXISTED(1040, "Đơn thuốc đã tồn tại", HttpStatus.BAD_REQUEST),
    DON_THUOC_NOT_EXISTED(1041, "Đơn thuốc không tồn tại", HttpStatus.NOT_FOUND),
    DON_THUOC_CHI_TIET_NOT_EXISTED(1042, "Chi tiết đơn thuốc không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI KHO THUỐC
    // =============================
    KHO_THUOC_NOT_EXISTED(1043, "Kho thuốc không tồn tại", HttpStatus.NOT_FOUND),
    THUOC_KHONG_DU(1044, "Thuốc trong kho không đủ", HttpStatus.BAD_REQUEST),
    INVALID_SO_LUONG(1045, "Số lượng không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI XÉT NGHIỆM
    // =============================
    LAB_TEST_EXISTED(1046, "Xét nghiệm đã tồn tại", HttpStatus.BAD_REQUEST),
    LAB_TEST_NOT_EXISTED(1047, "Xét nghiệm không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_LAB_RESULT(1048, "Kết quả xét nghiệm không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI PHẪU THUẬT
    // =============================
    YEU_CAU_PHAU_THUAT_EXISTED(1049, "Yêu cầu phẫu thuật đã tồn tại", HttpStatus.BAD_REQUEST),
    YEU_CAU_PHAU_THUAT_NOT_EXISTED(1050, "Yêu cầu phẫu thuật không tồn tại", HttpStatus.NOT_FOUND),
    CA_PHAU_THUAT_NOT_EXISTED(1051, "Ca phẫu thuật không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_PHAU_THUAT_TIME(1052, "Thời gian phẫu thuật không hợp lệ", HttpStatus.BAD_REQUEST),
    PHONG_PHAU_THUAT_BUSY(1053, "Phòng phẫu thuật đang được sử dụng", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI BỆNH ÁN
    // =============================
    BENH_AN_EXISTED(1054, "Bệnh án đã tồn tại", HttpStatus.BAD_REQUEST),
    BENH_AN_NOT_EXISTED(1055, "Bệnh án không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI HÓA ĐƠN
    // =============================
    HOA_DON_EXISTED(1056, "Hóa đơn đã tồn tại", HttpStatus.BAD_REQUEST),
    HOA_DON_NOT_EXISTED(1057, "Hóa đơn không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_TONG_TIEN(1058, "Tổng tiền không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI BÁO CÁO
    // =============================
    BAO_CAO_EXISTED(1059, "Báo cáo đã tồn tại", HttpStatus.BAD_REQUEST),
    BAO_CAO_NOT_EXISTED(1060, "Báo cáo không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_BAO_CAO_TYPE(1061, "Loại báo cáo không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI AUDIT LOG
    // =============================
    AUDIT_LOG_NOT_EXISTED(1062, "Audit log không tồn tại", HttpStatus.NOT_FOUND),
    
    // =============================
    // LỖI ĐỠN HÀNG THUỐC ONLINE
    // =============================
    DON_HANG_THUOC_EXISTED(1063, "Đơn hàng thuốc đã tồn tại", HttpStatus.BAD_REQUEST),
    DON_HANG_THUOC_NOT_EXISTED(1064, "Đơn hàng thuốc không tồn tại", HttpStatus.NOT_FOUND),
    DON_HANG_CHI_TIET_NOT_EXISTED(1065, "Chi tiết đơn hàng không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_TRANG_THAI_DON_HANG(1066, "Trạng thái đơn hàng không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI THANH TOÁN
    // =============================
    THANH_TOAN_EXISTED(1067, "Thanh toán đã tồn tại", HttpStatus.BAD_REQUEST),
    THANH_TOAN_NOT_EXISTED(1068, "Thanh toán không tồn tại", HttpStatus.NOT_FOUND),
    LICH_SU_THANH_TOAN_NOT_EXISTED(1069, "Lịch sử thanh toán không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_PHUONG_THUC_THANH_TOAN(1070, "Phương thức thanh toán không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_LOAI_GIAO_DICH(1071, "Loại giao dịch không hợp lệ", HttpStatus.BAD_REQUEST),
    THANH_TOAN_THAT_BAI(1072, "Thanh toán thất bại", HttpStatus.BAD_REQUEST),
    
    // =============================
    // LỖI SESSION
    // =============================
    SESSION_NOT_EXISTED(1073, "Phiên đăng nhập không tồn tại", HttpStatus.NOT_FOUND),
    SESSION_EXPIRED(1074, "Phiên đăng nhập đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1075, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1076, "Token đã hết hạn", HttpStatus.UNAUTHORIZED);
    
    /**
     * Constructor cho ErrorCode
     * @param code Mã lỗi
     * @param message Thông báo lỗi
     * @param statusCode HTTP status code
     */
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
    
    // Getters
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
