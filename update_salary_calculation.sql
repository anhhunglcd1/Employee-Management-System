-- Cập nhật Stored Procedure để tính lương với hệ số lương
-- Chạy script này để cập nhật công thức tính lương

USE HRMS_Database;
GO

-- 1. Thêm cột salary_coefficient vào bảng MonthlySalary nếu chưa có
IF NOT EXISTS (SELECT * FROM sys.columns 
               WHERE object_id = OBJECT_ID('MonthlySalary') 
               AND name = 'salary_coefficient')
BEGIN
    ALTER TABLE MonthlySalary 
    ADD salary_coefficient DECIMAL(5,2) DEFAULT 1.0;
    
    PRINT 'Đã thêm cột salary_coefficient vào bảng MonthlySalary';
END
ELSE
BEGIN
    PRINT 'Cột salary_coefficient đã tồn tại trong bảng MonthlySalary';
END
GO

-- 2. Drop và tạo lại stored procedure với công thức mới
IF OBJECT_ID('sp_CalculateMonthlySalary', 'P') IS NOT NULL
    DROP PROCEDURE sp_CalculateMonthlySalary;
GO

CREATE PROCEDURE sp_CalculateMonthlySalary
    @employee_id INT,
    @month INT,
    @year INT
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @base_salary DECIMAL(18,2);
    DECLARE @salary_coefficient DECIMAL(5,2);
    DECLARE @allowance DECIMAL(18,2);
    DECLARE @overtime_pay DECIMAL(18,2);
    DECLARE @bonus DECIMAL(18,2);
    DECLARE @other_income DECIMAL(18,2);
    DECLARE @late_deduction DECIMAL(18,2);
    DECLARE @absent_deduction DECIMAL(18,2);
    DECLARE @insurance_deduction DECIMAL(18,2);
    DECLARE @tax_deduction DECIMAL(18,2);
    DECLARE @other_deduction DECIMAL(18,2);
    DECLARE @gross_salary DECIMAL(18,2);
    DECLARE @total_deduction DECIMAL(18,2);
    DECLARE @net_salary DECIMAL(18,2);
    DECLARE @working_days INT;
    DECLARE @standard_days INT;
    DECLARE @overtime_hours DECIMAL(5,2);
    
    -- Lấy thông tin lương cơ bản, hệ số và phụ cấp từ BaseSalary
    SELECT 
        @base_salary = ISNULL(base_salary, 0),
        @salary_coefficient = ISNULL(salary_coefficient, 1.0),
        @allowance = ISNULL(allowance, 0)
    FROM BaseSalary
    WHERE employee_id = @employee_id AND status = 'ACTIVE';
    
    -- Nếu không tìm thấy, lấy từ hợp đồng hiện tại
    IF @base_salary IS NULL OR @base_salary = 0
    BEGIN
        SELECT TOP 1
            @base_salary = ISNULL(salary, 0),
            @salary_coefficient = ISNULL(salary_coefficient, 1.0),
            @allowance = ISNULL(allowance, 0)
        FROM Contracts
        WHERE employee_id = @employee_id 
            AND status = 'ACTIVE'
        ORDER BY start_date DESC;
    END
    
    -- Set giá trị mặc định nếu vẫn NULL
    SET @base_salary = ISNULL(@base_salary, 0);
    SET @salary_coefficient = ISNULL(@salary_coefficient, 1.0);
    SET @allowance = ISNULL(@allowance, 0);
    
    -- Tính số ngày làm việc và ngày chuẩn
    SET @standard_days = 26;
    
    -- Thống kê chấm công
    SELECT 
        @working_days = COUNT(CASE WHEN status IN ('PRESENT', 'LATE') THEN 1 END),
        @overtime_hours = ISNULL(SUM(overtime_hours), 0)
    FROM Attendance
    WHERE employee_id = @employee_id 
        AND MONTH(attendance_date) = @month 
        AND YEAR(attendance_date) = @year;
    
    SET @working_days = ISNULL(@working_days, 0);
    
    -- Tính lương tăng ca
    -- Công thức: (Lương CB * Hệ số) / (Ngày chuẩn * 8 giờ) * 1.5 * Số giờ tăng ca
    SET @overtime_pay = (@base_salary * @salary_coefficient) / (@standard_days * 8.0) * 1.5 * @overtime_hours;
    
    -- Lấy thưởng
    SET @bonus = 0;
    SET @other_income = 0;
    
    -- Tính khấu trừ đi muộn (50k mỗi lần)
    DECLARE @late_count INT;
    SELECT @late_count = COUNT(*)
    FROM Attendance
    WHERE employee_id = @employee_id
        AND MONTH(attendance_date) = @month
        AND YEAR(attendance_date) = @year
        AND status = 'LATE';
    
    SET @late_deduction = ISNULL(@late_count, 0) * 50000;
    
    -- Tính khấu trừ vắng mặt
    DECLARE @absent_days INT;
    SELECT @absent_days = COUNT(*)
    FROM Attendance
    WHERE employee_id = @employee_id
        AND MONTH(attendance_date) = @month
        AND YEAR(attendance_date) = @year
        AND status = 'ABSENT';
    
    -- Trừ lương theo ngày vắng: (Lương CB * Hệ số) / Ngày chuẩn * Số ngày vắng
    SET @absent_deduction = (@base_salary * @salary_coefficient) / @standard_days * ISNULL(@absent_days, 0);
    
    -- Tính bảo hiểm (10.5% của Lương CB * Hệ số)
    SET @insurance_deduction = (@base_salary * @salary_coefficient) * 0.105;
    
    -- Tính thuế thu nhập cá nhân
    DECLARE @taxable_income DECIMAL(18,2);
    SET @taxable_income = (@base_salary * @salary_coefficient) + @allowance + @overtime_pay + @bonus + @other_income - @insurance_deduction - 11000000;
    
    IF @taxable_income > 0
        SET @tax_deduction = @taxable_income * 0.10;
    ELSE
        SET @tax_deduction = 0;
    
    SET @other_deduction = 0;
    
    -- Tính tổng thu nhập (Gross Salary)
    -- QUAN TRỌNG: Lương cơ bản NHÂN với hệ số lương
    SET @gross_salary = (@base_salary * @salary_coefficient) + @allowance + @overtime_pay + @bonus + @other_income;
    
    -- Tính tổng khấu trừ
    SET @total_deduction = @late_deduction + @absent_deduction + @insurance_deduction + @tax_deduction + @other_deduction;
    
    -- Tính lương thực lãnh
    SET @net_salary = @gross_salary - @total_deduction;
    
    -- Kiểm tra và Insert/Update
    IF EXISTS (SELECT 1 FROM MonthlySalary 
               WHERE employee_id = @employee_id 
                 AND salary_month = @month 
                 AND salary_year = @year)
    BEGIN
        UPDATE MonthlySalary SET
            base_salary = @base_salary,
            salary_coefficient = @salary_coefficient,
            allowance = @allowance,
            overtime_pay = @overtime_pay,
            bonus = @bonus,
            other_income = @other_income,
            late_deduction = @late_deduction,
            absent_deduction = @absent_deduction,
            insurance_deduction = @insurance_deduction,
            tax_deduction = @tax_deduction,
            other_deduction = @other_deduction,
            gross_salary = @gross_salary,
            total_deduction = @total_deduction,
            net_salary = @net_salary,
            working_days = @working_days,
            standard_days = @standard_days,
            overtime_hours = @overtime_hours,
            status = 'PENDING',
            updated_date = GETDATE()
        WHERE employee_id = @employee_id
          AND salary_month = @month
          AND salary_year = @year;
    END
    ELSE
    BEGIN
        INSERT INTO MonthlySalary (
            employee_id, salary_month, salary_year,
            base_salary, salary_coefficient, allowance, overtime_pay, bonus, other_income,
            late_deduction, absent_deduction, insurance_deduction, tax_deduction, other_deduction,
            gross_salary, total_deduction, net_salary,
            working_days, standard_days, overtime_hours, status
        )
        VALUES (
            @employee_id, @month, @year,
            @base_salary, @salary_coefficient, @allowance, @overtime_pay, @bonus, @other_income,
            @late_deduction, @absent_deduction, @insurance_deduction, @tax_deduction, @other_deduction,
            @gross_salary, @total_deduction, @net_salary,
            @working_days, @standard_days, @overtime_hours, 'PENDING'
        );
    END
    
    RETURN 0;
END
GO

PRINT '==========================================';
PRINT 'Cập nhật thành công!';
PRINT 'Stored Procedure sp_CalculateMonthlySalary đã được cập nhật';
PRINT 'Công thức mới: Lương Gross = (Lương CB × Hệ Số) + Phụ Cấp + Tăng Ca + Thưởng';
PRINT 'Hệ số mặc định: 1.0';
PRINT '==========================================';
GO
