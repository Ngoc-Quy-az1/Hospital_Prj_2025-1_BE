# Hướng dẫn Deploy Backend lên Render

## 📋 Tổng quan

Hướng dẫn này sẽ giúp bạn deploy ứng dụng Spring Boot Hospital Management System lên Render.

## 🚀 Bước 1: Chuẩn bị Repository

1. Đảm bảo code đã được push lên GitHub
2. Kiểm tra các file cần thiết:
   - `render.yaml` ✅
   - `pom.xml` ✅
   - `src/main/resources/application-prod.properties` ✅

## 🔧 Bước 2: Tạo Service trên Render

### Cách 1: Sử dụng render.yaml (Khuyến nghị)

1. Đăng nhập vào [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"Blueprint"**
3. Kết nối repository GitHub của bạn
4. Render sẽ tự động detect file `render.yaml` và tạo service

### Cách 2: Tạo thủ công

1. Đăng nhập vào [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"Web Service"**
3. Kết nối repository GitHub của bạn
4. Cấu hình như sau:
   - **Name**: `hospital-backend`
   - **Runtime**: `Java`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/Hospital-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`
   - **Plan**: Free (hoặc Starter nếu cần)

## ⚙️ Bước 3: Cấu hình Environment Variables

Trong Render Dashboard, vào **Environment** tab và thêm các biến sau:

### Database Configuration
```
DB_HOST=your-database-host
DB_PORT=3306
DB_NAME=your-database-name
DB_USERNAME=your-username
DB_PASSWORD=your-password
```

### Mail Configuration
```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### Application Configuration (Optional)
```
APP_NAME=Hospital
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8090
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
LOGGING_LEVEL_ROOT=INFO
```

## 🗄️ Bước 4: Tạo Database (Nếu chưa có)

1. Trong Render Dashboard, click **"New +"** → **"PostgreSQL"** hoặc **"MySQL"**
2. Chọn plan (Free hoặc Starter)
3. Sau khi tạo xong, copy connection string và cập nhật vào Environment Variables

**Lưu ý**: Nếu bạn đã có database từ Aiven hoặc nơi khác, có thể bỏ qua bước này.

## 🔐 Bước 5: Cấu hình Security

1. Đảm bảo database cho phép kết nối từ Render IP
2. Kiểm tra firewall settings của database
3. Nếu dùng Aiven, thêm Render IP vào whitelist

## 🚀 Bước 6: Deploy

1. Click **"Manual Deploy"** → **"Deploy latest commit"**
2. Render sẽ tự động:
   - Build ứng dụng với Maven
   - Chạy tests (nếu có)
   - Start ứng dụng với Spring Boot
3. Kiểm tra logs để đảm bảo không có lỗi

## 📊 Bước 7: Kiểm tra Deployment

1. Sau khi deploy thành công, bạn sẽ nhận được URL: `https://hospital-backend.onrender.com`
2. Kiểm tra health endpoint: `https://hospital-backend.onrender.com/actuator/health`
3. Kiểm tra logs trong Render Dashboard

## 🔍 Troubleshooting

### Lỗi Build
- Kiểm tra Java version (cần Java 21)
- Kiểm tra Maven wrapper có hoạt động không
- Xem build logs trong Render Dashboard

### Lỗi Database Connection
- Kiểm tra Environment Variables đã đúng chưa
- Kiểm tra database có cho phép kết nối từ bên ngoài không
- Kiểm tra firewall settings

### Lỗi Port
- Render tự động set biến môi trường `PORT`
- Ứng dụng sẽ tự động sử dụng port này
- Không cần set `SERVER_PORT` nếu dùng `PORT`

### Application không start
- Kiểm tra logs trong Render Dashboard
- Đảm bảo `application-prod.properties` đã được cấu hình đúng
- Kiểm tra tất cả Environment Variables đã được set

## 📝 Lưu ý quan trọng

1. **Free Plan Limitations**:
   - Service sẽ sleep sau 15 phút không có traffic
   - Lần request đầu tiên sau khi sleep sẽ mất ~30 giây để wake up
   - Nên upgrade lên Starter plan cho production

2. **Database**:
   - Nếu dùng Render PostgreSQL/MySQL free plan, có giới hạn về storage và connections
   - Nên dùng database riêng (Aiven, AWS RDS, etc.) cho production

3. **Environment Variables**:
   - Không commit các giá trị thực vào git
   - Luôn sử dụng Environment Variables trong Render Dashboard

4. **Logs**:
   - Logs chỉ lưu trong 7 ngày (free plan)
   - Nên setup logging service riêng cho production

## 🔗 Useful Links

- [Render Documentation](https://render.com/docs)
- [Spring Boot on Render](https://render.com/docs/deploy-spring-boot)
- [Environment Variables](https://render.com/docs/environment-variables)

## ✅ Checklist trước khi Deploy

- [ ] Code đã được push lên GitHub
- [ ] `render.yaml` đã được tạo
- [ ] `application-prod.properties` đã được cấu hình
- [ ] Database đã được setup và accessible
- [ ] Environment Variables đã được set trong Render Dashboard
- [ ] Database firewall đã cho phép Render IP
- [ ] Đã test build local với `./mvnw clean package`

