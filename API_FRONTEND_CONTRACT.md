
## 2. Patient APIs (`patientAPI`)

### 2.1 `GET /api/patient/profile`
- **Header**: `Authorization: Bearer <token>`.
- **Response** (gợi ý)
  ```json
  {
    "benhnhanId": number,
    "hoTen": "string",
    "ngaySinh": "yyyy-MM-dd",
    "gioiTinh": "Nam|Nữ|Khác",
    "sdt": "string",
    "email": "string",
    "diaChi": "string",
    "ngayNhapVien": "yyyy-MM-dd",
    "trangThai": "đang điều trị|đã xuất viện|chuyển viện|tử vong"
  }
  ```

### 2.2 `PUT /api/patient/profile`
- **Body**: object có các field như trên (FE truyền full form).
- **Response**: profile sau khi cập nhật (tương tự 2.1) hoặc `{ success: true, data: { ... } }`.

### 2.3 `POST /api/patient/register`
- **Body**: dữ liệu đăng ký bệnh nhân (họ tên, SĐT, email…).
- **Response**: FE chỉ cần biết thành công/thất bại.

### 2.4 `GET /api/patient/doctors`
- **Query**:
  - `page`: number (default 0)
  - `size`: number (default 6)
- **Response**:
  ```json
  {
    "content": [
      {
        "bacsiId": number,
        "hoTen": "string",
        "chuyenKhoa": "string",
        "sdt": "string",
        "email": "string",
        "phongban": {
          "phongbanId": number,
          "tenPhongban": "string"
        }
      }
    ],
    "totalElements": number,
    "totalPages": number,
    "number": number,
    "size": number
  }
  ```

### 2.5 `GET /api/patient/appointments`
- **Response tối thiểu**: mảng hoặc `content[]` với:
  ```json
  {
    "datlichId": number,
    "ngayGio": "yyyy-MM-dd'T'HH:mm:ss",
    "loaiKham": "string",
    "trangThai": "cho_duyet|da_duyet|da_kham|huy",
    "ghiChu": "string",
    "bacsi": {
      "bacsiId": number,
      "hoTen": "string",
      "chuyenKhoa": "string"
    }
  }
  ```

### 2.6 `POST /api/patient/appointments`
- **Body** (từ form đặt lịch):
  ```json
  {
    "bacsiId": number,
    "ngayGio": "yyyy-MM-dd'T'HH:mm",
    "loaiKham": "string",
    "ghiChu": "string"
  }
  ```
- **Response**: lịch vừa tạo (format như 2.5).

### 2.7 `PUT /api/patient/appointments/{id}`
- **Body**: tương tự 2.6.
- **Response**: lịch sau khi cập nhật.

### 2.8 `PUT /api/patient/appointments/{id}/cancel`
- **Response**: object lịch với `trangThai = "huy"` hoặc `{ success: true }`.

### 2.9 `GET /api/patient/prescriptions`
- **Response**: danh sách đơn thuốc (mảng hoặc `content[]`):
  ```json
  {
    "donthuocId": number,
    "ngayKe": "yyyy-MM-dd",
    "ghiChu": "string",
    "bacsi": { "bacsiId": number, "hoTen": "string" }
  }
  ```

### 2.10 `GET /api/patient/prescriptions/{id}`
- **Response**:
  ```json
  {
    "donthuocId": number,
    "ngayKe": "yyyy-MM-dd",
    "ghiChu": "string",
    "bacsi": { "bacsiId": number, "hoTen": "string" },
    "danhSachChiTiet": [
      {
        "id": number,
        "soLuong": number,
        "lieuDung": "string",
        "thuoc": {
          "thuocId": number,
          "tenThuoc": "string",
          "hamLuong": "string",
          "dangBaoChe": "string"
        }
      }
    ]
  }
  ```

### 2.11 `GET /api/patient/bills`
### 2.12 `GET /api/patient/bills/{id}`
### 2.13 `POST /api/patient/bills/{id}/pay`
- FE chưa dùng sâu, chỉ cần:
  ```json
  {
    "billId": number,
    "tongTien": number,
    "trangThai": "UNPAID|PAID|CANCELLED",
    "ngayThanhToan": "yyyy-MM-dd'T'HH:mm:ss"
  }
  ```

### 2.14 `GET /api/patient/lab-results`
### 2.15 `GET /api/patient/lab-results/{id}`

### 2.16 `GET /api/patient/medical-history`
- FE dùng để hiển thị lịch sử khám, có thể reuse format bệnh án / hồ sơ khám:
  ```json
  [
    {
      "benhanId": number,
      "ghiChu": "string",
      "hoSoKham": {
        "hosokhamId": number,
        "ngayKham": "yyyy-MM-dd",
        "chanDoan": "string",
        "huongDieuTri": "string"
      },
      "donThuoc": { "donthuocId": number },
      "labTest": { "labtestId": number },
      "caPhauThuat": { "caId": number }
    }
  ]
  ```

---

## 3. Doctor APIs (`doctorAPI`)

### 3.1 `GET /api/doctor/profile`
- **Response FE đang dùng**:
  ```json
  {
    "bacsiId": number,
    "hoTen": "string",
    "email": "string",
    "sdt": "string",
    "chucVu": "string | null",           // hiển thị ở DoctorDashboard
    "phongban": {
      "phongbanId": number,
      "tenPhongBan": "string"
    }
  }
  ```

### 3.2 `PUT /api/doctor/profile`
- **Body**: object cập nhật các field trên.
- **Response**: profile sau khi cập nhật.

### 3.3 `GET /api/doctor/appointments`
- **Query**:
  - `status` (optional): `Chờ khám|Đang khám|Hoàn thành|Đã hủy` (FE dùng string tiếng Việt).
  - `date` (optional): `yyyy-MM-dd`.
  - `page` (default 0), `size` (default 100).
- **Response** (DoctorPatients + các page lịch hẹn khác):
  ```json
  {
    "content": [
      {
        "datlichId": number,
        "ngayGio": "yyyy-MM-dd'T'HH:mm:ss",
        "trangThai": "cho_duyet|da_duyet|da_kham|huy",
        "loaiKham": "string",
        "lyDoKham": "string",
        "ghiChu": "string",
        "benhnhan": {
          "benhnhanId": number,
          "hoTen": "string",
          "ngaySinh": "yyyy-MM-dd",
          "gioiTinh": "Nam|Nữ|Khác",
          "sdt": "string"
        }
      }
    ],
    "totalElements": number,
    "totalPages": number
  }
  ```

### 3.4 `PUT /api/doctor/appointments/{id}/approve`
### 3.5 `PUT /api/doctor/appointments/{id}/reject`
### 3.6 `PUT /api/doctor/appointments/{id}/complete`
- **Response**: lịch sau khi cập nhật trạng thái.
- FE chỉ cần `trangThai` mới hoặc `{ success: true }` rồi refetch.

### 3.7 `GET /api/doctor/prescriptions`
- **Query**:
  - `patientId` (optional)
  - `page`, `size`
- **Response**:
  ```json
  {
    "content": [
      {
        "donthuocId": number,
        "ngayKe": "yyyy-MM-dd",
        "ghiChu": "string",
        "benhnhan": { "benhnhanId": number, "hoTen": "string" }
      }
    ]
  }
  ```

### 3.8 `GET /api/doctor/prescriptions/{id}`
- Giống 2.10 (chi tiết đơn thuốc).

### 3.9 `POST /api/doctor/prescriptions`
- **Body**:
  ```json
  {
    "benhnhanId": number,
    "ngayKe": "yyyy-MM-dd",
    "ghiChu": "string",
    "chiTiet": [
      {
        "thuocId": number,
        "soLuong": number,
        "lieuDung": "string"
      }
    ]
  }
  ```
- **Response**: đơn thuốc mới (format chi tiết).

### 3.10 `GET /api/doctor/medicines`
- **Query**:
  - `search` (optional)
  - `page` (default 0), `size` (default 1000)
- **Response**: mảng hoặc `content[]` của:
  ```json
  {
    "thuocId": number,
    "tenThuoc": "string",
    "hoatChat": "string",
    "hamLuong": "string",
    "dangBaoChe": "string"
  }
  ```

### 3.11 Lab Tests (`/api/doctor/lab-tests...`)
- FE chỉ cần dữ liệu tối thiểu:
  - `getLabTests`: danh sách xét nghiệm liên quan tới bác sĩ.
  - `createLabTest`, `updateTestResult` cập nhật, FE chỉ check không lỗi.

### 3.12 Surgeries (`/api/doctor/surgeries/...`)
- `getSurgeryRequests`, `getSurgerySchedule`, `createSurgeryRequest`.
- Cấu trúc: id, ngày, loại phẫu thuật, bệnh nhân, trạng thái.

### 3.13 `GET /api/doctor/schedule`
- **Query**:
  - `startDate`, `endDate` (optional, yyyy-MM-dd)
- **Response** (DoctorWorkSchedule dùng):
  - Mảng hoặc `content[]`:
  ```json
  {
    "lichlamviecId": number,
    "ngayLamViec": "yyyy-MM-dd",
    "gioBatDau": "HH:mm",
    "gioKetThuc": "HH:mm",
    "ghiChu": "string"
  }
  ```

### 3.14 `POST /api/doctor/schedule`
- **Body**:
  ```json
  {
    "ngayLamViec": "yyyy-MM-dd",
    "gioBatDau": "HH:mm",
    "gioKetThuc": "HH:mm",
    "ghiChu": "string"
  }
  ```
- **Response**: bản ghi mới tạo.

### 3.15 `GET /api/doctor/patients/stats`
- **Response FE dùng (DoctorPatients)**:
  ```json
  {
    "totalPatients": number,
    "waitingForExamination": number,
    "currentlyExamining": number,
    "completed": number
  }
  ```

### 3.16 `GET /api/doctor/patients`
- **Query**:
  - `search` (optional)
  - `appointmentStatus` (optional: `Chờ khám|Đang khám|Hoàn thành`)
  - `page`, `size`
- **Response**:
  ```json
  {
    "content": [
      {
        "benhnhanId": number,
        "hoTen": "string",
        "ngaySinh": "yyyy-MM-dd",
        "gioiTinh": "Nam|Nữ|Khác",
        "sdt": "string",
        "email": "string",
        "diaChi": "string",
        "trangThai": "Chờ khám|Đang khám|Hoàn thành|Đã hủy",
        "khoa": "string",
        "trieuChung": "string",
        "chanDoan": "string",
        "ngayHen": "yyyy-MM-dd",
        "gioHen": "HH:mm"
      }
    ],
    "totalElements": number,
    "totalPages": number
  }
  ```

### 3.17 `GET /api/doctor/patients/{id}/medical-history`
- **Query**: `page`, `size`.
- **Response**:
  - FE parse `response.content || response.data || []`, mỗi phần tử là 1 **bệnh án/hồ sơ**:
  ```json
  {
    "benhanId": number,
    "ghiChu": "string",
    "hoSoKham": {
      "hosokhamId": number,
      "ngayKham": "yyyy-MM-dd",
      "chanDoan": "string",
      "huongDieuTri": "string"
    },
    "donThuoc": { "donthuocId": number },
    "labTest": { "labtestId": number },
    "caPhauThuat": { "caId": number }
  }
  ```

### 3.18 `GET /api/doctor/appointments` (theo patientId)
- Đã mô tả ở 3.3; DoctorPatients còn gọi lại với:
  - `patientId`, `page`, `size` để lấy lịch hẹn riêng bệnh nhân.

### 3.19 `GET /api/doctor/medical-records`
- **Query**:
  - `patientId` (optional)
  - `page`, `size`
- **Response**:
  ```json
  {
    "content": [
      {
        "hosokhamId": number,
        "ngayKham": "yyyy-MM-dd",
        "trieuChung": "string",
        "chanDoan": "string",
        "huongDieuTri": "string"
      }
    ]
  }
  ```

### 3.20 `POST /api/doctor/medical-records`
- **Body** (DoctorPatients dùng khi lưu chẩn đoán):
  ```json
  {
    "benhnhanId": number,
    "ngayKham": "yyyy-MM-dd",
    "trieuChung": "string",
    "chanDoan": "string",
    "dieuTri": "string",
    "ghiChu": "string",
    "loiKhuyen": "string"
  }
  ```
- **Response**: hồ sơ khám vừa tạo (format 3.19).

### 3.21 `GET /api/doctor/bills`
- `patientId`, `status`, `page`, `size` – xem hóa đơn của bệnh nhân theo bác sĩ.

### 3.22 Dashboard Stats

#### `GET /api/doctor/dashboard/stats`
- **Query**: `date` (yyyy-MM-dd, optional; FE gửi selectedDate).
- **Response (DoctorDashboard)**:
  ```json
  {
    "patientsToday": number,
    "appointmentsToday": number,
    "prescriptionsToday": number,
    "totalPrescriptionValue": number,
    "confirmedAppointments": number,
    "completedAppointments": number,
    "pendingAppointments": number
  }
  ```

#### `GET /api/doctor/appointments/stats`
- **Query**: `date` (optional).
- **Response**:
  ```json
  {
    "totalAppointments": number,
    "confirmedAppointments": number,
    "completedAppointments": number,
    "pendingAppointments": number
  }
  ```

#### `GET /api/doctor/prescriptions/stats`
- **Query**: `date` (optional).
- **Response**:
  ```json
  {
    "totalPrescriptions": number,
    "totalMedicines": number,
    "totalValue": number
  }
  ```

---

## 4. Admin APIs (`adminAPI`) – tóm tắt phần đang dùng nhiều

### 4.1 User Management

#### `GET /api/admin/users`
- **Query**: `page`, `size`.
- **Response FE dùng (UserManagement)**:
  ```json
  {
    "content": [
      {
        "userId": number,
        "username": "string",
        "email": "string",
        "status": "active|inactive|locked",
        "role": { "tenRole": "Admins|bacsi|benhnhan|..." }
      }
    ],
    "totalElements": number,
    "totalPages": number
  }
  ```

Các API còn lại của `users` (`GET /{id}`, `POST`, `PUT`, `toggle-status`, `DELETE`) hiện UI đang xử lý **local state**, bạn có thể thiết kế theo chuẩn REST thông thường, FE chỉ cần:
- Trả về user đầy đủ sau create/update.
- `toggle-status`: cập nhật `status`.

### 4.2 Doctor Management

#### `GET /api/admin/doctors`
#### `GET /api/admin/doctors/by-date`
- **Query**:
  - `page`, `size`
  - `search`, `position`, `phongbanId`
  - `startDate`, `endDate` → nếu có thì FE gọi `/by-date`.
- **Response (StaffManagement)**:
  ```json
  {
    "content": [
      {
        "bacsiId": number,
        "hoTen": "string",
        "chuyenKhoa": "string",
        "sdt": "string",
        "email": "string",
        "chucVu": "string",
        "phongban": { "phongbanId": number, "tenPhongban": "string" },
        "soLichKham": number,
        "soBenhNhan": number
      }
    ],
    "totalPages": number
  }
  ```

#### `GET /api/admin/doctors/{id}`, `POST /api/admin/doctors`, `PUT /api/admin/doctors/{id}`, `DELETE /api/admin/doctors/{id}`
- Chuẩn CRUD, object format như trên.

### 4.3 Staff Management (`/api/admin/staff...`)
- Tương tự doctors, nhưng entity `NhanVien`:
  - `nhanvienId`, `hoTen`, `chucVu`, `sdt`, `email`, `phongban`, v.v.

### 4.4 Department Management (`/api/admin/departments`)
- **GET**:
  - FE normalize từ:
    ```json
    {
      "phongbanId" | "departmentId" | "id": number,
      "tenPhongban" | "tenPhongBan" | "tenKhoa" | "name": "string"
    }
    ```

### 4.5 Shift Frames (`/api/admin/shift-frames`, `/api/admin/shifts/shift-frames`)
- FE ưu tiên `/api/admin/shifts/shift-frames`, fallback `/api/admin/shift-frames`.
- **Response item**:
  ```json
  {
    "id" | "khunggiotrucId" | "khungGioTrucId": number,
    "tenKhung" | "khungGioLabel": "Ca sáng",
    "gioBatDau": "HH:mm",
    "gioKetThuc": "HH:mm"
  }
  ```

### 4.6 Schedule Management (`/api/admin/shifts...`)

#### `GET /api/admin/shifts`
- **Query**:
  - `mode`: `day|week|month`
  - `date`: `yyyy-MM-dd`
  - `departmentId`, `bacsiId`, `page`, `size`, `sort`
- **Response** (ScheduleManagement):
  ```json
  {
    "content": [
      {
        "id" | "lichtrucId" | "shiftId" | "lichTrucId" | "lichtrucbanId": number,
        "ngayTruc" | "ngaytruc" | "date": "yyyy-MM-dd",
        "khungGioTrucId" | "khunggiotrucId": number,
        "gioBatDau": "HH:mm",
        "gioKetThuc": "HH:mm",
        "phongbanId": number,
        "phongbanName" | "phongBan.tenPhongBan" | "phongban.tenPhongban" | "department" | "departmentName": "string",
        "bacsiId": number,
        "bacsiName": "string",
        "xacNhan": boolean,
        "ghiChu" | "notes": "string"
      }
    ],
    "totalElements": number,
    "totalPages": number
  }
  ```

#### `GET /api/admin/shifts/{id}`
- Trả về 1 object với các field như trên.

#### `POST /api/admin/shifts`
#### `PUT /api/admin/shifts/{id}`
- **Body FE gửi (ShiftDTO)**
  ```json
  {
    "bacsiId": number,
    "phongbanId": number,
    "khungGioTrucId": number,
    "ngayTruc": "yyyy-MM-dd",
    "ghiChu": "string"
  }
  ```

#### `DELETE /api/admin/shifts/{id}`
- Xóa ca trực.

#### `PUT /api/admin/shifts/{id}/confirm?confirmed=true|false`
- Đổi trạng thái xác nhận.

#### `GET /api/admin/shifts/summary`
- **Query**: `date`, `departmentId`.
- **Response**:
  ```json
  {
    "totalShifts": number,
    "confirmedShifts": number,
    "pendingShifts": number
  }
  ```

#### `GET /api/admin/shifts/doctors`
- **Query**: `departmentId` (optional).
- **Response (ScheduleManagement)**:
  ```json
  [
    {
      "id": number,              // bacsiId
      "name": "string",          // hoTen
      "position": "string",      // 'Bác sĩ'
      "departmentId": number,
      "departmentName": "string"
    }
  ]
  ```

#### `GET /api/admin/shifts/export`
- **Query**: `date`, `mode`, `departmentId`.
- **Response**: file Excel (`Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`).

### 4.7 Medicine Management (`/api/admin/medicines...`)

#### `GET /api/admin/medicines`
- **Query**:
  - `page`, `size`
  - `search`, `nhaSanXuat`, `nhomThuoc`, `dangBaoChe`, `expiringBefore`
  - `sort`: `"field,asc|desc"` (FE truyền `sortField`, `sortDir`).
- **Response (MedicineManagement)**:
  ```json
  {
    "content": [
      {
        "thuocId": number,
        "maThuoc": "string",
        "tenThuoc": "string",
        "hoatChat": "string",
        "nhomThuoc": "string",
        "donVi": "Viên|Ống|...",
        "tonKhoHienTai": number,
        "tonKhoToiThieu": number,
        "tonKhoToiDa": number,
        "donGia": number,
        "nhaCungCap": "string",
        "nhaSanXuat": "string",
        "hanSuDung": "yyyy-MM-dd",
        "soLo": "string",
        "yeuCauKeDon": boolean,
        "trangThai": "Có sẵn|Sắp hết|Sắp hết hạn|Hết hàng|Hết hạn"
      }
    ],
    "totalElements": number,
    "totalPages": number
  }
  ```

#### `GET /api/admin/medicines/stats`
- **Response**:
  ```json
  {
    "total": number,
    "outOfStock": number,
    "lowStock": number,
    "expiringSoon": number
  }
  ```

#### `GET /api/admin/medicines/dosage-forms`
- **Response**: mảng string: `["Viên", "Ống", ...]` (hoặc `{ content: [...] }`).

#### `GET /api/admin/medicines/groups`
- **Response**: mảng string nhóm thuốc (hoặc `{ content: [...] }`).

#### `POST /api/admin/medicines`
#### `PUT /api/admin/medicines/{id}`
#### `DELETE /api/admin/medicines/{id}`
- **Body FE gửi (DTO)**:
  ```json
  {
    "tenThuoc": "string",
    "hoatChat": "string",
    "nhomThuoc": "string",
    "donVi": "string",
    "tonKhoHienTai": number,
    "tonKhoToiThieu": number,
    "tonKhoToiDa": number,
    "donGia": number,
    "nhaCungCap": "string",
    "hanSuDung": "yyyy-MM-dd",
    "soLo": "string",
    "yeuCauKeDon": boolean,
    "trangThai": "Có sẵn|Sắp hết|Sắp hết hạn|Hết hàng|Hết hạn"
  }
  ```
- **Response**: object thuốc (format như GET) để FE format lại.

### 4.8 Patient Management (Admin) (`/api/admin/patients...`)

#### `GET /api/admin/patients`
- **Query**:
  - `page`, `size`
  - `search`, `gender`, `status`, `minAge`, `maxAge`
- **Response (PatientManagement)**:
  ```json
  {
    "content": [
      {
        "benhnhanId": number,
        "hoTen": "string",
        "ngaySinh": "yyyy-MM-dd",
        "gioiTinh": "Nam|Nữ|Khác",
        "sdt": "string",
        "email": "string",
        "diaChi": "string",
        "trangThai": "đang điều trị|đã xuất viện|chuyển viện|tử vong",
        "ngayNhapVien": "yyyy-MM-dd",
        "soLanKham": number,
        "soDonThuoc": number
      }
    ],
    "totalPages": number,
    "totalElements": number
  }
  ```

#### `GET /api/admin/patients/{id}`
- Trả 1 bản ghi giống phần tử trong `content`.

#### `GET /api/admin/patients/{id}/medical-records`
- FE dùng làm danh sách bệnh án trong modal:
  ```json
  [
    {
      "benhanId": number,
      "ghiChu": "string",
      "hoSoKham": {
        "hosokhamId": number,
        "ngayKham": "yyyy-MM-dd",
        "chanDoan": "string",
        "huongDieuTri": "string"
      },
      "donThuoc": { "donthuocId": number },
      "labTest": { "labtestId": number },
      "caPhauThuat": { "caId": number }
    }
  ]
  ```

#### `POST /api/admin/patients`
#### `PUT /api/admin/patients/{id}`
#### `DELETE /api/admin/patients/{id}`
- **Body**:
  ```json
  {
    "hoTen": "string",
    "ngaySinh": "yyyy-MM-dd",
    "gioiTinh": "Nam|Nữ|Khác",
    "sdt": "string",
    "email": "string",
    "diaChi": "string",
    "ngayNhapVien": "yyyy-MM-dd",
    "trangThai": "đang điều trị|đã xuất viện|chuyển viện|tử vong"
  }
  ```

#### `GET /api/admin/patients/stats`
- **Response**:
  ```json
  {
    "total": number,
    "male": number,
    "female": number,
    "other": number,
    "dangDieuTri": number,
    "khoiBenh": number
  }
  ```

---

## 5. Các module khác (Pharmacy, Lab, Accountant, Nurse)

Các API này chủ yếu là CRUD + report; FE hiện **chưa dùng sâu** trong logic phức tạp, nên bạn có thể thiết kế:
- **Pharmacy** (`/api/pharmacy/...`): quản lý kho, đơn thuốc chờ cấp phát.
- **Lab** (`/api/lab/...`): quản lý xét nghiệm, mẫu xét nghiệm, thiết bị.
- **Accountant** (`/api/accountant/...`): hóa đơn, thanh toán, báo cáo doanh thu.
- **Nurse** (`/api/nurse/...`): bệnh nhân được phân công, phòng bệnh, chăm sóc, dấu hiệu sinh tồn.

Mẫu chung:
- Danh sách: trả `{ content: [], totalElements, totalPages }` hoặc `[]`.
- Chi tiết: trả object đầy đủ field để hiển thị.
- Tạo / cập nhật: nhận DTO tương ứng, trả lại bản ghi sau khi lưu.

---

## 6. Yêu cầu về Token & Bảo mật

- Mọi API `/api/**` (trừ `/api/auth/**`) **đều được gọi qua `apiClient`**, nên:
  - Cần kiểm tra header `Authorization: Bearer <accessToken>`.
  - Nếu token hết hạn ⇒ trả `401`, backend nên hỗ trợ `/api/auth/refresh`.
- `user` trong token nên chứa `role` để FE map quyền; hoặc FE lấy `role` từ `user` trong response login.


