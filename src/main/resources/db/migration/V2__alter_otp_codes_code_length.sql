-- Tăng độ dài cột code và email trong bảng otp_codes để tránh lỗi Data truncation
ALTER TABLE otp_codes 
    MODIFY COLUMN code VARCHAR(255) NOT NULL;

ALTER TABLE otp_codes 
    MODIFY COLUMN email VARCHAR(100);


