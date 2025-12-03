# 🚀 Quick Start - Deploy to Render

## Các bước deploy nhanh:

1. **Push code lên GitHub** (nếu chưa có)
   ```bash
   git add .
   git commit -m "Add Render deployment configuration"
   git push origin main
   ```

2. **Tạo service trên Render**:
   - Vào [Render Dashboard](https://dashboard.render.com)
   - Click **"New +"** → **"Blueprint"**
   - Kết nối repository GitHub
   - Render sẽ tự động detect `render.yaml`

3. **Cấu hình Environment Variables** trong Render Dashboard:
   ```
   DB_HOST=your-database-host
   DB_PORT=3306
   DB_NAME=your-database-name
   DB_USERNAME=your-username
   DB_PASSWORD=your-password
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-password
   ```

4. **Deploy và kiểm tra**:
   - Render sẽ tự động build và deploy
   - Kiểm tra logs trong Render Dashboard
   - Test API endpoint: `https://your-app.onrender.com/actuator/health`

## 📚 Chi tiết đầy đủ

Xem file [RENDER_DEPLOYMENT.md](./RENDER_DEPLOYMENT.md) để biết hướng dẫn chi tiết.

## ⚠️ Lưu ý

- Free plan sẽ sleep sau 15 phút không có traffic
- Database cần cho phép kết nối từ Render IP
- Không commit secret vào git - luôn dùng Environment Variables

