USE htqlnv;
GO

-- =============================================
-- 1. Thêm cột updated_date vào MonthlySalary nếu chưa có
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.columns 
               WHERE object_id = OBJECT_ID('MonthlySalary') 
               AND name = 'updated_date')
BEGIN
    ALTER TABLE MonthlySalary ADD updated_date DATETIME NULL;
    PRINT 'Đã thêm cột updated_date vào bảng MonthlySalary';
END
GO

-- =============================================
-- 2. Thêm cột salary_coefficient vào MonthlySalary nếu chưa có
-- =============================================
IF NOT EXISTS (SELECT * FROM sys.columns 
               WHERE object_id = OBJECT_ID('MonthlySalary') 
               AND name = 'salary_coefficient')
BEGIN
    ALTER TABLE MonthlySalary ADD salary_coefficient DECIMAL(5,2) DEFAULT 1.0;
    PRINT 'Đã thêm cột salary_coefficient vào bảng MonthlySalary';
END
ELSE
BEGIN
    PRINT 'Cột salary_coefficient đã tồn tại trong bảng MonthlySalary';
END
GO

-- =============================================
-- 3. Tạo bảng BaseSalary nếu chưa có
-- =============================================
IF OBJECT_ID('BaseSalary', 'U') IS NULL
BEGIN
    CREATE TABLE BaseSalary (
        id                  INT IDENTITY(1,1) PRIMARY KEY,
        employee_id         INT           NOT NULL,
        base_salary         DECIMAL(18,2) DEFAULT 0,
        salary_coefficient  DECIMAL(5,2)  DEFAULT 1.0,
        allowance           DECIMAL(18,2) DEFAULT 0,
        effective_date      DATE          NOT NULL DEFAULT GETDATE(),
        status              NVARCHAR(20)  DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE
        notes               NVARCHAR(MAX),
        created_at          DATETIME      DEFAULT GETDATE(),
        CONSTRAINT fk_bs_employee FOREIGN KEY (employee_id) REFERENCES employees(id),
        CONSTRAINT uq_bs_employee_active UNIQUE (employee_id, status)  
    );
    PRINT 'Đã tạo bảng BaseSalary';
END
GO

-- =============================================
-- 4. Đổi tên bảng contracts -> Contracts nếu cần
--    (đảm bảo tên khớp với stored procedure)
-- =============================================
IF OBJECT_ID('contracts', 'U') IS NOT NULL 
   AND OBJECT_ID('Contracts', 'U') IS NULL
BEGIN
    EXEC sp_rename 'contracts', 'Contracts';
    PRINT 'Đã đổi tên bảng contracts -> Contracts';
END
GO

-- =============================================
-- 5. Đổi tên bảng attendance -> Attendance nếu cần
-- =============================================
IF OBJECT_ID('attendance', 'U') IS NOT NULL 
   AND OBJECT_ID('Attendance', 'U') IS NULL
BEGIN
    EXEC sp_rename 'attendance', 'Attendance';
    PRINT 'Đã đổi tên bảng attendance -> Attendance';
END
GO

-- =============================================
-- 6. Tạo/Cập nhật Stored Procedure tính lương
-- =============================================
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
    
    DECLARE @base_salary         DECIMAL(18,2) = 0;
    DECLARE @salary_coefficient  DECIMAL(5,2)  = 1.0;
    DECLARE @allowance           DECIMAL(18,2) = 0;
    DECLARE @overtime_pay        DECIMAL(18,2) = 0;
    DECLARE @bonus               DECIMAL(18,2) = 0;
    DECLARE @other_income        DECIMAL(18,2) = 0;
    DECLARE @late_deduction      DECIMAL(18,2) = 0;
    DECLARE @absent_deduction    DECIMAL(18,2) = 0;
    DECLARE @insurance_deduction DECIMAL(18,2) = 0;
    DECLARE @tax_deduction       DECIMAL(18,2) = 0;
    DECLARE @other_deduction     DECIMAL(18,2) = 0;
    DECLARE @gross_salary        DECIMAL(18,2) = 0;
    DECLARE @total_deduction     DECIMAL(18,2) = 0;
    DECLARE @net_salary          DECIMAL(18,2) = 0;
    DECLARE @working_days        INT           = 0;
    DECLARE @standard_days       INT           = 26;
    DECLARE @overtime_hours      DECIMAL(5,2)  = 0;
    DECLARE @late_count          INT           = 0;
    DECLARE @absent_days         INT           = 0;
    DECLARE @taxable_income      DECIMAL(18,2) = 0;

    -- -----------------------------------------------
    -- Bước 1: Lấy lương cơ bản từ BaseSalary
    -- -----------------------------------------------
    SELECT TOP 1
        @base_salary        = ISNULL(base_salary, 0),
        @salary_coefficient = ISNULL(salary_coefficient, 1.0),
        @allowance          = ISNULL(allowance, 0)
    FROM BaseSalary
    WHERE employee_id = @employee_id 
      AND status = 'ACTIVE'
    ORDER BY effective_date DESC;

    -- -----------------------------------------------
    -- Bước 2: Nếu không có BaseSalary -> lấy từ Contracts
    -- -----------------------------------------------
    IF @base_salary = 0
    BEGIN
        SELECT TOP 1
            @base_salary        = ISNULL(salary, 0),
            @salary_coefficient = ISNULL(salary_coefficient, 1.0),
            @allowance          = ISNULL(allowance, 0)
        FROM Contracts
        WHERE employee_id = @employee_id 
          AND status = 'ACTIVE'
        ORDER BY start_date DESC;
    END

    -- -----------------------------------------------
    -- Bước 3: Thống kê chấm công trong tháng
    -- -----------------------------------------------
    SELECT 
        @working_days   = COUNT(CASE WHEN status IN ('PRESENT', 'LATE') THEN 1 END),
        @overtime_hours = ISNULL(SUM(overtime_hours), 0),
        @late_count     = COUNT(CASE WHEN status = 'LATE'   THEN 1 END),
        @absent_days    = COUNT(CASE WHEN status = 'ABSENT' THEN 1 END)
    FROM Attendance
    WHERE employee_id = @employee_id 
      AND MONTH(attendance_date) = @month 
      AND YEAR(attendance_date)  = @year;

    -- -----------------------------------------------
    -- Bước 4: Tính lương tăng ca
    -- Công thức: (Lương CB × Hệ số) / (Ngày chuẩn × 8h) × 1.5 × Giờ tăng ca
    -- -----------------------------------------------
    SET @overtime_pay = (@base_salary * @salary_coefficient) 
                        / (@standard_days * 8.0) 
                        * 1.5 
                        * @overtime_hours;

    -- -----------------------------------------------
    -- Bước 5: Tính khấu trừ đi muộn (50,000đ/lần)
    -- -----------------------------------------------
    SET @late_deduction = @late_count * 50000;

    -- -----------------------------------------------
    -- Bước 6: Tính khấu trừ ngày vắng
    -- Công thức: (Lương CB × Hệ số) / Ngày chuẩn × Số ngày vắng
    -- -----------------------------------------------
    SET @absent_deduction = (@base_salary * @salary_coefficient) 
                            / @standard_days 
                            * @absent_days;

    -- -----------------------------------------------
    -- Bước 7: Tính bảo hiểm (10.5% của Lương CB × Hệ số)
    --   BHXH: 8%, BHYT: 1.5%, BHTN: 1% = 10.5%
    -- -----------------------------------------------
    SET @insurance_deduction = (@base_salary * @salary_coefficient) * 0.105;

    -- -----------------------------------------------
    -- Bước 8: Tính thuế TNCN
    -- Thu nhập chịu thuế = Gross - Bảo hiểm - Giảm trừ bản thân (11 triệu)
    -- -----------------------------------------------
    SET @taxable_income = (@base_salary * @salary_coefficient) 
                          + @allowance 
                          + @overtime_pay 
                          + @bonus 
                          + @other_income 
                          - @insurance_deduction 
                          - 11000000;

    SET @tax_deduction = CASE WHEN @taxable_income > 0 
                              THEN @taxable_income * 0.10 
                              ELSE 0 END;

    -- -----------------------------------------------
    -- Bước 9: Tính Gross Salary và Net Salary
    -- Gross = (Lương CB × Hệ số) + Phụ cấp + Tăng ca + Thưởng + Thu nhập khác
    -- Net   = Gross - Tổng khấu trừ
    -- -----------------------------------------------
    SET @gross_salary    = (@base_salary * @salary_coefficient) 
                           + @allowance 
                           + @overtime_pay 
                           + @bonus 
                           + @other_income;

    SET @total_deduction = @late_deduction 
                           + @absent_deduction 
                           + @insurance_deduction 
                           + @tax_deduction 
                           + @other_deduction;

    SET @net_salary = @gross_salary - @total_deduction;

    -- -----------------------------------------------
    -- Bước 10: Insert hoặc Update MonthlySalary
    -- -----------------------------------------------
    IF EXISTS (
        SELECT 1 FROM MonthlySalary 
        WHERE employee_id  = @employee_id 
          AND salary_month = @month 
          AND salary_year  = @year
    )
    BEGIN
        UPDATE MonthlySalary SET
            base_salary         = @base_salary,
            salary_coefficient  = @salary_coefficient,
            allowance           = @allowance,
            overtime_pay        = @overtime_pay,
            bonus               = @bonus,
            other_income        = @other_income,
            late_deduction      = @late_deduction,
            absent_deduction    = @absent_deduction,
            insurance_deduction = @insurance_deduction,
            tax_deduction       = @tax_deduction,
            other_deduction     = @other_deduction,
            gross_salary        = @gross_salary,
            total_deduction     = @total_deduction,
            net_salary          = @net_salary,
            working_days        = @working_days,
            standard_days       = @standard_days,
            overtime_hours      = @overtime_hours,
            status              = 'PENDING',
            updated_date        = GETDATE()
        WHERE employee_id  = @employee_id
          AND salary_month = @month
          AND salary_year  = @year;

        PRINT 'Đã cập nhật lương tháng ' + CAST(@month AS NVARCHAR) 
              + '/' + CAST(@year AS NVARCHAR) 
              + ' cho nhân viên ID: ' + CAST(@employee_id AS NVARCHAR);
    END
    ELSE
    BEGIN
        INSERT INTO MonthlySalary (
            employee_id, salary_month, salary_year,
            base_salary, salary_coefficient, allowance,
            overtime_pay, bonus, other_income,
            late_deduction, absent_deduction, insurance_deduction,
            tax_deduction, other_deduction,
            gross_salary, total_deduction, net_salary,
            working_days, standard_days, overtime_hours,
            status, created_at, updated_date
        )
        VALUES (
            @employee_id, @month, @year,
            @base_salary, @salary_coefficient, @allowance,
            @overtime_pay, @bonus, @other_income,
            @late_deduction, @absent_deduction, @insurance_deduction,
            @tax_deduction, @other_deduction,
            @gross_salary, @total_deduction, @net_salary,
            @working_days, @standard_days, @overtime_hours,
            'PENDING', GETDATE(), GETDATE()
        );

        PRINT 'Đã tạo bảng lương tháng ' + CAST(@month AS NVARCHAR) 
              + '/' + CAST(@year AS NVARCHAR) 
              + ' cho nhân viên ID: ' + CAST(@employee_id AS NVARCHAR);
    END

    RETURN 0;
END
GO

-- =============================================
-- 7. Stored Procedure tính lương cho toàn bộ nhân viên
-- =============================================
IF OBJECT_ID('sp_CalculateAllSalaries', 'P') IS NOT NULL
    DROP PROCEDURE sp_CalculateAllSalaries;
GO

CREATE PROCEDURE sp_CalculateAllSalaries
    @month INT,
    @year  INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @employee_id INT;
    DECLARE @error_count INT = 0;
    DECLARE @success_count INT = 0;

    DECLARE emp_cursor CURSOR FOR
        SELECT id FROM employees WHERE status = 'WORKING';

    OPEN emp_cursor;
    FETCH NEXT FROM emp_cursor INTO @employee_id;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        BEGIN TRY
            EXEC sp_CalculateMonthlySalary 
                @employee_id = @employee_id,
                @month       = @month,
                @year        = @year;
            SET @success_count = @success_count + 1;
        END TRY
        BEGIN CATCH
            PRINT 'Lỗi khi tính lương nhân viên ID: ' + CAST(@employee_id AS NVARCHAR)
                  + ' - ' + ERROR_MESSAGE();
            SET @error_count = @error_count + 1;
        END CATCH

        FETCH NEXT FROM emp_cursor INTO @employee_id;
    END

    CLOSE emp_cursor;
    DEALLOCATE emp_cursor;

    PRINT '========================================';
    PRINT 'Hoàn thành tính lương tháng ' + CAST(@month AS NVARCHAR) + '/' + CAST(@year AS NVARCHAR);
    PRINT 'Thành công: ' + CAST(@success_count AS NVARCHAR) + ' nhân viên';
    PRINT 'Thất bại  : ' + CAST(@error_count   AS NVARCHAR) + ' nhân viên';
    PRINT '========================================';
END
GO

PRINT '==========================================';
PRINT 'Cập nhật thành công!';
PRINT 'Đã tạo/cập nhật các đối tượng:';
PRINT '  + Bảng BaseSalary';
PRINT '  + Cột updated_date trong MonthlySalary';
PRINT '  + Cột salary_coefficient trong MonthlySalary';
PRINT '  + SP sp_CalculateMonthlySalary';
PRINT '  + SP sp_CalculateAllSalaries';
PRINT '';
PRINT 'Công thức tính lương:';
PRINT '  Gross  = (Lương CB × Hệ số) + Phụ cấp + Tăng ca + Thưởng';
PRINT '  Net    = Gross - BHXH(10.5%) - Thuế TNCN(10%) - Khấu trừ khác';
PRINT '==========================================';
GO

-- =============================================
-- 8. Ví dụ sử dụng
-- =============================================
-- Tính lương 1 nhân viên:
-- EXEC sp_CalculateMonthlySalary @employee_id=1, @month=2, @year=2026;

-- Tính lương toàn bộ nhân viên:
-- EXEC sp_CalculateAllSalaries @month=2, @year=2026;