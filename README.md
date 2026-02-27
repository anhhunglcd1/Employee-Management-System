# HỆ THỐNG QUẢN LÝ NHÂN SỰ
### 🏢 🚀 Giới thiệu dự án
Quản lý nhân sự xây dựng bằng **Java Swing** và **Microsoft SQL Server** (JDBC).  
Phục vụ các nghiệp vụ cơ bản của phòng nhân sự: quản lý nhân viên, phòng ban - chức vụ, chấm công, tính lương, nghỉ phép, hợp đồng.
### 👨‍💻👩‍💻 Thành viên & phân công công việc

| Thành viên | Công việc chính phụ trách |
|---|---|
| 🧑‍✈️ **[Họ tên Thành viên 1]** | **Nhóm chức năng 1:** Quản lý **Nhân viên** (CRUD, tìm kiếm/lọc); Quản lý **Phòng ban & Chức vụ** (CRUD, liên kết nhân viên); tham gia thiết kế & xây dựng giao diện các màn hình thuộc nhóm 1. |
| 👨‍💻 **[Họ tên Thành viên 2]** | **Nhóm chức năng 1:** **Chấm công** theo ngày (thêm/sửa/xóa, lọc theo ngày/phòng ban); **Database** (thiết kế CSDL/ERD, tạo bảng, PK/FK, dữ liệu mẫu, script SQL); hỗ trợ kết nối JDBC/DAO. |
| 👨‍💻 **[Họ tên Thành viên 3]** | **Nhóm chức năng 2:** **Lương** (tính lương theo tháng Gross/Net, lưu bảng lương, trạng thái); thiết kế & triển khai giao diện + xử lý nghiệp vụ module lương. |
| 👨‍💻 **[Họ tên Thành viên 4]** | **Nhóm chức năng 2:** **Nghỉ phép** (tạo đơn, duyệt/từ chối, quản lý trạng thái); **Hợp đồng** (tạo/cập nhật, theo dõi hết hạn); **Báo cáo** (tổng hợp nội dung, hình/bảng, đánh giá). |

### 📘 🛠️ Hướng dẫn setup
Tải và cài đặt SQL Server (SQL server 2019 trở lên hoặc link ở dưới SQL Server 2025 Express)
```bash
https://go.microsoft.com/fwlink/p/?linkid=2216019&clcid=0x409&culture=en-us&country=us
```

<img width="1466" height="527" alt="SQL" src="https://github.com/user-attachments/assets/a077e1c4-bc81-4838-9858-8274b70e3c48" />

Bấm vào file đã tải chọn basic -> Accept -> Install .Sau khi cài đặt xong chọn Install ssms và tải ssms. Tải xong khi bấm vào chọn Install để cài đặt.
Mở SQL Server Management Studio 22 sau khi cài đặt xong.

<img width="1720" height="933" alt="image" src="https://github.com/user-attachments/assets/c4983e18-14ce-481c-ace4-8c4011864933" />

Chọn Browse -> Local -> (tên máy)\SQLEXPRESS 

<img width="1716" height="930" alt="image" src="https://github.com/user-attachments/assets/39fd6b73-afdd-40a6-9e36-d29b1e122d4b" />

Bấm Connect -> (tên máy)\SQLEXPRESS (chuột phải) -> Properties -> Security -> Chọn SQL Server and Windows Authentication mode -> ok

Tiếp theo chọn như hình bên dưới (sa) ->(Chuột phải vào) sa -> Properties

<img width="1719" height="932" alt="image" src="https://github.com/user-attachments/assets/9fc60ccd-4a6c-4228-96c3-b33d025f744b" />

Phần General đặt password and confirm password: mkdc@2025. Bỏ chọn Enforce password policy

<img width="1719" height="932" alt="image" src="https://github.com/user-attachments/assets/3b0ec7c9-3e96-41d0-8042-55ba55153afa" />

Phần Status -> Login chọn Enabled rồi bấm ok

<img width="1717" height="931" alt="image" src="https://github.com/user-attachments/assets/a173ded2-b911-4ca9-a605-fa8ead85dd7b" />

Vào phần tìm kiếm Window tìm SQL Server 2025 Configuration Manager

<img width="945" height="711" alt="image" src="https://github.com/user-attachments/assets/6877f2b3-ec3e-4919-ad65-5098269e5e8a" />

SQL Server Configuration Manager -> SQL Server Network Configuration -> protocols for SQLEXPRESS -> TCP/IP

<img width="944" height="713" alt="image" src="https://github.com/user-attachments/assets/94438fd1-c21b-476b-addf-346b2ef85c7b" />

(Chuột phải) TCP/IP -> Enable -> (Chuột phải) TCP/IP ->  Properties -> IP Addresses (dòng cuối cùng IPALL) như hình

<img width="412" height="473" alt="image" src="https://github.com/user-attachments/assets/1d39d28e-97e9-46a1-8c4c-66d65c9b0dce" />

Chọn SQL Server Services -> SQL Server (SQLEXPRESS) -> (Chuột phải) restart

<img width="945" height="712" alt="image" src="https://github.com/user-attachments/assets/3f592948-cc1c-41d0-9709-47dd0cf6964f" />

Vào lại SQL Server Management Studio 22 đăng nhập bằng sa và password: mkdc@2025 -> Connect

<img width="1718" height="929" alt="image" src="https://github.com/user-attachments/assets/77bfdf87-601d-4085-aa6a-051e00bd6ed1" />

Mở file lib + database trong project -> mở file htql.sql -> Execute

<img width="1717" height="929" alt="image" src="https://github.com/user-attachments/assets/f7ecaad3-f2b1-42cc-bd4b-5cc1721b5999" />

Vào netbean mở file project add thư viện mssql-jdbc-13.2.1.jre11.jar trong file file lib + database trong project
Và chạy run (jdk 17)





























