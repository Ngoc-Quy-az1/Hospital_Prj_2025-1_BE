package com.example.Hospital.exception;

/**
 * Utility class để tạo exception một cách dễ dàng
 */
public class ExceptionUtils {
    
    /**
     * Tạo AppException với ErrorCode
     */
    public static AppException throwAppException(ErrorCode errorCode) {
        return new AppException(errorCode);
    }
    
    /**
     * Tạo AppException với ErrorCode và message tùy chỉnh
     */
    public static AppException throwAppException(ErrorCode errorCode, String message) {
        return new AppException(errorCode, message);
    }
    
    /**
     * Tạo UserException
     */
    public static UserException throwUserException(ErrorCode errorCode) {
        return new UserException(errorCode);
    }
    
    /**
     * Tạo UserException với message tùy chỉnh
     */
    public static UserException throwUserException(ErrorCode errorCode, String message) {
        return new UserException(errorCode, message);
    }
    
    /**
     * Tạo PatientException
     */
    public static PatientException throwPatientException(ErrorCode errorCode) {
        return new PatientException(errorCode);
    }
    
    /**
     * Tạo PatientException với message tùy chỉnh
     */
    public static PatientException throwPatientException(ErrorCode errorCode, String message) {
        return new PatientException(errorCode, message);
    }
    
    /**
     * Tạo DoctorException
     */
    public static DoctorException throwDoctorException(ErrorCode errorCode) {
        return new DoctorException(errorCode);
    }
    
    /**
     * Tạo DoctorException với message tùy chỉnh
     */
    public static DoctorException throwDoctorException(ErrorCode errorCode, String message) {
        return new DoctorException(errorCode, message);
    }
    
    /**
     * Tạo AppointmentException
     */
    public static AppointmentException throwAppointmentException(ErrorCode errorCode) {
        return new AppointmentException(errorCode);
    }
    
    /**
     * Tạo AppointmentException với message tùy chỉnh
     */
    public static AppointmentException throwAppointmentException(ErrorCode errorCode, String message) {
        return new AppointmentException(errorCode, message);
    }
    
    /**
     * Tạo MedicineException
     */
    public static MedicineException throwMedicineException(ErrorCode errorCode) {
        return new MedicineException(errorCode);
    }
    
    /**
     * Tạo MedicineException với message tùy chỉnh
     */
    public static MedicineException throwMedicineException(ErrorCode errorCode, String message) {
        return new MedicineException(errorCode, message);
    }
    
    /**
     * Tạo PaymentException
     */
    public static PaymentException throwPaymentException(ErrorCode errorCode) {
        return new PaymentException(errorCode);
    }
    
    /**
     * Tạo PaymentException với message tùy chỉnh
     */
    public static PaymentException throwPaymentException(ErrorCode errorCode, String message) {
        return new PaymentException(errorCode, message);
    }
    
    /**
     * Tạo SurgeryException
     */
    public static SurgeryException throwSurgeryException(ErrorCode errorCode) {
        return new SurgeryException(errorCode);
    }
    
    /**
     * Tạo SurgeryException với message tùy chỉnh
     */
    public static SurgeryException throwSurgeryException(ErrorCode errorCode, String message) {
        return new SurgeryException(errorCode, message);
    }
    
    private ExceptionUtils() {
        // Private constructor để ngăn khởi tạo
    }
}





