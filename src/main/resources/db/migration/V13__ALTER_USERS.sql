ALTER TABLE tab_users
ADD COLUMN full_Name VARCHAR(250) AFTER name,
ADD COLUMN funct VARCHAR(250) AFTER full_Name,
ADD COLUMN photo VARCHAR(255) DEFAULT '/images/account.png' AFTER funct,
ADD COLUMN sex CHAR(1) CHECK (sex IN ('M', 'F')) AFTER photo;
