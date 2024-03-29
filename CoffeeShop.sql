USE master;
GO
DROP DATABASE IF EXISTS CoffeeShop;
GO

CREATE DATABASE CoffeeShop;
GO
USE CoffeeShop;
GO

--Tạo bảng Users
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY,
    name NVARCHAR(100) NOT NULL,
    email VARCHAR(100)
        UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role TINYINT DEFAULT (0)
);
GO

CREATE PROC sp_countUsers
AS
SELECT COUNT(*) [count]
FROM Users;
GO


CREATE PROC sp_insertUser (
    @_name NVARCHAR(100),
    @_email VARCHAR(100),
    @_password VARCHAR(100),
    @_role TINYINT = 0,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT email FROM Users WHERE email = @_email)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Email đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        INSERT INTO Users
        (
            [name],
            email,
            [password],
            [role]
        )
        VALUES
        (@_name, @_email, @_password, @_role);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm người dùng thành công';

        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm không thành công: ' + ERROR_MESSAGE();

    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

-- Them user 
EXEC sp_insertUser @_name = N'Phạm Văn Tiến',
                   @_email = 'admin@gmail.com',
                   @_password = '123456',
                   @_role = 1;
EXEC sp_insertUser @_name = N'Cao Hồng Đức',
                   @_email = 'user2@gmail.com',
                   @_password = '123456',
                   @_role = 0;
EXEC sp_insertUser @_name = N'Lê Vân',
                   @_email = 'user@gmail.com',
                   @_password = '123123',
                   @_role = 0;
EXEC sp_insertUser @_name = N'Nguyễn Thái Minh',
                   @_email = 'admin1@gmail.com',
                   @_password = '123456',
				   @_role = 1;
EXEC sp_insertUser @_name = N'Lê Duy Quyết',
                   @_email = 'user1@gmail.com',
                   @_password = '123456',
                   @_role = 0;
GO

--Tạo hàm cập nhật user
CREATE PROC sp_updateUser (
	@_id INT,
    @_name NVARCHAR(100),
    @_email VARCHAR(100),
    @_password VARCHAR(100),
    @_role TINYINT = 0,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY   
	IF EXISTS (SELECT email FROM Users WHERE email = @_email AND id != @_id)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Email đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        UPDATE Users
		SET 
            [name] = @_name,
            email = @_email,
            [password] = @_password,
            [role] = @_role
		WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Cập nhật người dùng thành công';

        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật không thành công: ' + ERROR_MESSAGE();

    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_checkUser (
    @_email VARCHAR(100),
    @_password VARCHAR(100)
)
AS
BEGIN
    SELECT TOP 1 *
    FROM Users
    WHERE email = @_email
          AND [password] = @_password;
END;
GO

CREATE PROC [dbo].[sp_getAllUser] 
(
    @_name NVARCHAR(100) = NULL,
    @_email VARCHAR(100) = NULL,
    @_role TINYINT = NULL
)
AS
DECLARE @sql NVARCHAR(MAX) = 'SELECT * FROM Users WHERE 1=1';

IF (@_name IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND name LIKE N''%', @_name, '%''');

IF (@_email IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND email=''', @_email, '''');

IF (@_role IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND role=', @_role);

EXEC (@sql);
GO

--Tạo hàm xoá user
CREATE PROC sp_deleteUser (
    @_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT U.id
        FROM Users U
            JOIN Bills B
                ON B.[user_id] = U.id
        WHERE U.id = @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Người dùng đang có liên kết hoá đơn, không thể xoá';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        DELETE Users
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE TABLE Categories (
    id INT PRIMARY KEY IDENTITY,
    name NVARCHAR(100)
        UNIQUE NOT NULL,
    status BIT DEFAULT (1)
);
GO

CREATE PROC sp_insertCategory (
    @_name NVARCHAR(100),
    @_status BIT = 1,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT [name] FROM Categories WHERE [name] = @_name)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên danh mục đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        INSERT INTO Categories
        (
            [name],
            [status]
        )
        VALUES
        (@_name, @_status);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm không thành công' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

-- Thêm danh mục
EXEC sp_insertCategory @_name = N'Cà Phê Việt';
EXEC sp_insertCategory @_name = N'Cà Phê Tây';
EXEC sp_insertCategory @_name = N'Sinh Tố';
EXEC sp_insertCategory @_name = N'Trà';
EXEC sp_insertCategory @_name = N'Trái Cây';
EXEC sp_insertCategory @_name = N'Sữa Chua';
EXEC sp_insertCategory @_name = N'Khác';
GO

CREATE PROC sp_updateCategory (
    @_id INT,
    @_name NVARCHAR(100),
    @_status BIT = 1,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT [name]
        FROM Categories
        WHERE [name] = @_name
              AND id <> @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên danh mục đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        UPDATE Categories
        SET [name] = @_name,
            [status] = @_status
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Cập nhật thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật thất bại: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_deleteCategory (
    @_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT *
        FROM Categories
            JOIN Products
                ON Products.category_id = Categories.id
        WHERE Categories.id = @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Danh mục đang có sản phẩm, không thể xoá';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        DELETE Categories
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_getAllCategory (
    @_name NVARCHAR(100) = NULL,
    @_status BIT = NULL
)
AS
DECLARE @sql NVARCHAR(MAX) = 'SELECT * FROM Categories WHERE 1=1';

IF (@_name IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND name LIKE N''%', @_name, '%''');

IF (@_status IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND status=', @_status);

EXEC (@sql);
GO

CREATE TABLE Products (
    id INT PRIMARY KEY IDENTITY,
    category_id INT
        FOREIGN KEY REFERENCES Categories (id),
    name NVARCHAR(100)
        UNIQUE NOT NULL,
    price INT
        DEFAULT (0),
    status BIT
        DEFAULT (1)
);

GO

CREATE PROC sp_insertProduct (
    @_category_id INT,
    @_name NVARCHAR(100),
    @_price INT,
    @_status BIT = 1,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT [name] FROM Products WHERE [name] = @_name)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên sản phẩm đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        INSERT INTO Products
        (
            category_id,
            [name],
            price,
            [status]
        )
        VALUES
        (@_category_id, @_name, @_price, @_status);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm sản phẩm thành công';

        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm thất bại: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

EXEC sp_insertProduct 1, N'Bạc xỉu', 35000;
EXEC sp_insertProduct 1, N'Cà phê sữa tươi', 35000;
EXEC sp_insertProduct 1, N'Cà phê đen', 29000;
EXEC sp_insertProduct 1, N'Cà phê nâu', 35000;
EXEC sp_insertProduct 1, N'Cà phê sữa tươi', 35000;
EXEC sp_insertProduct 2, N'Latte', 45000;
EXEC sp_insertProduct 2, N'Cappuchino', 45000;
EXEC sp_insertProduct 2, N'Espresso', 30000;
EXEC sp_insertProduct 3, N'Sinh tố bơ', 59000;
EXEC sp_insertProduct 3, N'Sinh tố xoài', 5000;
EXEC sp_insertProduct 4, N'Trà cam quế', 45000;
EXEC sp_insertProduct 4, N'Trà đào chanh leo', 45000;
EXEC sp_insertProduct 4, N'Trà quất mật ong', 45000;
EXEC sp_insertProduct 4, N'Trà lip ton', 25000;
EXEC sp_insertProduct 4, N'Trà mạn', 35000;
EXEC sp_insertProduct 5, N'Cóc xanh (theo mùa)', 55000;
EXEC sp_insertProduct 5, N'Chanh tươi', 39000;
EXEC sp_insertProduct 5, N'Dưa hấu', 49000;
EXEC sp_insertProduct 5, N'Chanh leo', 49000;
EXEC sp_insertProduct 5, N'Cam tươi', 65000;
EXEC sp_insertProduct 5, N'Ổi', 45000;
EXEC sp_insertProduct 5, N'Xoài xanh', 45000;
EXEC sp_insertProduct 6, N'Sữa chua dầm đá', 35000;
EXEC sp_insertProduct 6, N'Sữa chua ca cao', 40000;
EXEC sp_insertProduct 6, N'Sữa chua cà phê', 40000;
EXEC sp_insertProduct 6, N'Sữa chua trái cây', 55000;
EXEC sp_insertProduct 7, N'Hạt hướng dương', 25000;
EXEC sp_insertProduct 7, N'Lạc rang', 25000;
EXEC sp_insertProduct 7, N'Ngô cay', 25000;
EXEC sp_insertProduct 7, N'Bánh đậu xanh & Kẹo lạc', 25000;
EXEC sp_insertProduct 7, N'Bánh sừng bò chấm sữa', 25000;
EXEC sp_insertProduct 7, N'Thịt bò khô', 40000;
GO

CREATE PROC sp_getAllProduct
(
    @_name NVARCHAR(100) = NULL,
    @_category_id INT = NULL,
    @_fromPrice INT = NULL,
    @_toPrice INT = NULL,
    @_status BIT = NULL
)
AS
DECLARE @sql NVARCHAR(MAX)
    = '
		SELECT p.*, c.[name] category_name
		FROM Products p
		JOIN Categories c
        ON c.id = p.category_id WHERE 1=1 AND c.status = 1';

IF (@_name IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND p.name LIKE N''%', @_name, '%''');

IF (@_category_id IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND p.category_id=', @_category_id);

IF (@_fromPrice IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND p.price>=', @_fromPrice);

IF (@_toPrice IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND p.price<=', @_toPrice);

IF (@_status IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND p.status=', @_status);

EXEC(@sql)
GO

CREATE PROC sp_updateProduct
(
    @_id INT,
    @_category_id INT,
    @_name NVARCHAR(100),
    @_price INT,
    @_status BIT = 1,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT [name]
        FROM Products
        WHERE [name] = @_name
              AND id != @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên sản phẩm đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        UPDATE Products
        SET category_id = @_category_id,
            [name] = @_name,
            [price] = @_price,
            [status] = @_status
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Sửa đổi sản phẩm thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật thất bại: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_deleteProduct
(
    @_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT *
        FROM Products p
            JOIN BillDetail o
                ON o.product_id = p.id
        WHERE p.id = @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Sản phẩm đang có liên kết hoá đơn, không thể xoá';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        DELETE Products
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE TABLE Areas
(
    id INT PRIMARY KEY IDENTITY,
    [name] NVARCHAR(100) UNIQUE NOT NULL
);
GO

CREATE PROC sp_getAllArea
AS
SELECT *
FROM Areas;
GO

CREATE PROC sp_insertArea
(
    @_name NVARCHAR(100),
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT [name] FROM Areas WHERE [name] = @_name)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên khu vực đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        INSERT INTO Areas ([name]) VALUES (@_name);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm khu vực thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm thất bại: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

EXEC sp_insertArea @_name = N'Tầng 1';
EXEC sp_insertArea @_name = N'Tầng 2';
EXEC sp_insertArea @_name = N'Tầng 3';
GO

CREATE PROC sp_updateArea
(
    @_id INT,
    @_name NVARCHAR(100),
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT [name] FROM Areas WHERE [name] = @_name AND id != @_id)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên khu vực đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        UPDATE Areas
        SET [name] = @_name
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Sửa khu vực thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật thất bại: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_findAreaByName
(@_name NVARCHAR(100))
AS
SELECT *
FROM Areas
WHERE [name] = @_name;
GO

CREATE PROC sp_deleteArea
(
    @_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT *
        FROM Areas
            JOIN [Tables]
                ON [Tables].area_id = Areas.id
        WHERE Areas.id = @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Khu vực đang có bàn, không thể xoá!';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        DELETE Areas
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE TABLE [Tables]
(
    id INT PRIMARY KEY IDENTITY,
    area_id INT
        FOREIGN KEY REFERENCES Areas (id),
    [name] NVARCHAR(100) UNIQUE NOT NULL
);

GO

CREATE PROC sp_getAllTable
(
    @_id INT = NULL,
    @_area_id INT = NULL,
    @_name NVARCHAR(100) = NULL
)
AS
DECLARE @sql NVARCHAR(MAX) = '
		SELECT *
		FROM Tables WHERE 1=1';

IF (@_id IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND id = ', @_id);

IF (@_area_id IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND area_id = ', @_area_id);

IF (@_name IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND name = N''', @_name, '''');

EXEC (@sql);
GO

CREATE PROC sp_insertTable
(
    @_area_id INT,
    @_name NVARCHAR(100),
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT [name] FROM [Tables] WHERE [name] = @_name)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên bàn đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        INSERT INTO Tables
        (
            area_id,
            [name]
        )
        VALUES
        (@_area_id, @_name);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm bàn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

EXEC sp_insertTable 1, N'Bàn 1';
EXEC sp_insertTable 1, N'Bàn 2';
EXEC sp_insertTable 1, N'Bàn 3';
EXEC sp_insertTable 1, N'Bàn 4';
EXEC sp_insertTable 1, N'Bàn 5';
EXEC sp_insertTable 2, N'Bàn 6';
EXEC sp_insertTable 2, N'Bàn 7';
EXEC sp_insertTable 2, N'Bàn 8';
EXEC sp_insertTable 2, N'Bàn 9';
EXEC sp_insertTable 3, N'Bàn 10';
EXEC sp_insertTable 3, N'Bàn 11';
EXEC sp_insertTable 3, N'Bàn 12';
GO

CREATE PROC sp_updateTable
(
    @_id INT,
    @_area_id INT,
    @_name NVARCHAR(100),
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS (SELECT [name] FROM [Tables] WHERE [name] = @_name AND id != @_id)
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Tên bàn đã tồn tại, vui lòng nhập lại';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        UPDATE Tables
        SET area_id = @_area_id,
            [name] = @_name
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Cập nhật bàn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_deleteTable
(
    @_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT B.*
        FROM [Tables] T
            JOIN Bills B
                ON T.id = B.table_id
        WHERE T.id = @_id
    )
    BEGIN
        SET @_outStt = 0;
        SET @_outMsg = N'Bàn đang có hoá đơn, không thể xoá';
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        DELETE [Tables]
        WHERE id = @_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE TABLE Bills
(
    id INT PRIMARY KEY IDENTITY,
    user_id INT NOT NULL
        FOREIGN KEY REFERENCES Users (id),
    table_id INT NOT NULL
        FOREIGN KEY REFERENCES Tables (id),
    total_price INT,
    status BIT DEFAULT (1),
    created_at SMALLDATETIME DEFAULT (GETDATE()),
    table_name NVARCHAR(100) NULL
);
GO

--tạo một trigger tự động update table_name theo table_id khi tạo Bill mới
CREATE TRIGGER [trg_SetBillTableName]
ON [Bills]
AFTER INSERT
AS
BEGIN
    UPDATE B
    SET B.[table_name] = T.[name]
    FROM [dbo].[Bills] B
    JOIN [dbo].[Tables] T ON B.[table_id] = T.[id]
    WHERE B.[id] IN (SELECT [id] FROM inserted);
END;
GO

CREATE PROC sp_insertBill
(
    @_user_id INT,
    @_table_id INT,
    @_total_price INT = 0,
    @_status BIT = 1,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    BEGIN
        BEGIN TRAN;
        INSERT INTO Bills
        (
            [user_id],
            table_id,
            total_price,
            [status]
        )
        VALUES
        (@_user_id, @_table_id, @_total_price, @_status);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm hoá đơn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_updateBill
(
    @_bill_id INT,
    @_user_id INT,
    @_table_id INT,
    @_total_price INT = 0,
    @_status BIT = 1,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    BEGIN
        BEGIN TRAN;
        UPDATE Bills
        SET [user_id] = @_user_id,
            table_id = @_table_id,
            total_price = @_total_price,
            [status] = @_status
        WHERE id = @_bill_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Cập nhật hoá đơn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_getAllBill
(
    @_id INT = NULL,
    @_user_id INT = NULL,
    @_table_id INT = NULL,
    @_status BIT = NULL,
	@_table_name nvarchar(100) = NULL,
	@_time_start varchar(10) = NULL,
	@_time_end varchar(10) = NULL
)
AS
DECLARE @sql NVARCHAR(MAX)
    = N'
		SELECT B.*, U.name user_name
		FROM Bills B
		LEFT JOIN Users U
        ON U.id = B.user_id
		WHERE 1=1';

IF (@_id IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND B.id = ', @_id);

IF (@_user_id IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND B.user_id = ', @_user_id);

IF (@_table_id IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND B.table_id = ', @_table_id);

IF (@_status IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND B.status = ', @_status);

IF (@_table_name IS NOT NULL)
    SET @sql = CONCAT(@sql, N' AND B.table_name = N''', @_table_name, N'''');

IF (@_time_start IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND CONVERT(DATE, B.created_at) >= ''', @_time_start, '''');

IF (@_time_end IS NOT NULL)
    SET @sql = CONCAT(@sql, ' AND CONVERT(DATE, B.created_at) <= ''', @_time_end, '''');

EXEC (@sql);
GO

CREATE PROC sp_deleteBill
(
    @_bill_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    BEGIN
        BEGIN TRAN;
        IF EXISTS (SELECT bill_id FROM BillDetail WHERE bill_id = @_bill_id)
        BEGIN
            DELETE FROM BillDetail
            WHERE bill_id = @_bill_id;
        END;

        DELETE FROM Bills
        WHERE id = @_bill_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá hoá đơn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_getBillByTableId
(
    @_table_id INT,
    @_status BIT = 1
)
AS
SELECT TOP 1
       B.*,
       U.name [user_name],
       T.name table_name
FROM Bills B
    LEFT JOIN Users U
        ON U.id = B.[user_id]
    LEFT JOIN Tables T
        ON T.id = B.table_id
WHERE B.table_id = @_table_id
      AND B.[status] = @_status
ORDER BY B.created_at DESC;
GO

CREATE TABLE BillDetail
(
    bill_id INT NOT NULL
        FOREIGN KEY REFERENCES Bills (id),
    product_id INT NOT NULL
        FOREIGN KEY REFERENCES Products (id),
    amount INT
        DEFAULT (1)
);
GO

CREATE PROC sp_insertBillDetail
(
    @_bill_id INT,
    @_product_id INT,
    @_amount INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    IF EXISTS
    (
        SELECT *
        FROM BillDetail
        WHERE bill_id = @_bill_id
              AND product_id = @_product_id
    )
    BEGIN
        BEGIN TRAN;
        UPDATE BillDetail
        SET amount += @_amount
        WHERE bill_id = @_bill_id
              AND product_id = @_product_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Cập nhật chi tiết hoá đơn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
    ELSE
    BEGIN
        BEGIN TRAN;
        INSERT INTO BillDetail
        (
            bill_id,
            product_id,
            amount
        )
        VALUES
        (@_bill_id, @_product_id, @_amount);

        SET @_outStt = 1;
        SET @_outMsg = N'Thêm chi tiết hoá đơn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Thêm không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_updateBillDetail
(
    @_bill_id INT,
    @_product_id INT,
    @_amount INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN 
TRY
        BEGIN TRAN;
        UPDATE BillDetail
        SET amount = @_amount
        WHERE bill_id = @_bill_id
              AND product_id = @_product_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Cập nhật chi tiết hoá đơn thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    --END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Cập nhật không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_deleteBillDetail
(
    @_bill_id INT,
    @_product_id INT,
    @_outStt BIT = 1 OUTPUT,
    @_outMsg NVARCHAR(200) = '' OUTPUT
)
AS
BEGIN TRY
    BEGIN
        BEGIN TRAN;
        DELETE BillDetail
        WHERE bill_id = @_bill_id
              AND product_id = @_product_id;

        SET @_outStt = 1;
        SET @_outMsg = N'Xoá thành công';
        IF @@TRANCOUNT > 0
            COMMIT TRAN;
    END;
END TRY
BEGIN CATCH
    SET @_outStt = 0;
    SET @_outMsg = N'Xoá không thành công: ' + ERROR_MESSAGE();
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN;
END CATCH;
GO

CREATE PROC sp_getBillDetailByBillId
(@_bill_id INT)
AS
SELECT BD.*,
       P.name 'product_name',
       P.price 'product_price'
FROM BillDetail BD
    LEFT JOIN Products P
        ON P.id = BD.product_id
WHERE bill_id = @_bill_id;
GO

CREATE PROC sp_countCategories
AS
SELECT COUNT(*) [count]
FROM Categories;
GO

CREATE PROC sp_countProducts
AS
SELECT COUNT(*) [count]
FROM Products;
GO

CREATE PROC sp_countAreas
AS
SELECT COUNT(*) [count]
FROM Areas;
GO

CREATE PROC sp_countTables
AS
SELECT COUNT(*) [count]
FROM Tables;
GO

CREATE PROC sp_countBills
AS
SELECT COUNT(*) [count]
FROM Bills;