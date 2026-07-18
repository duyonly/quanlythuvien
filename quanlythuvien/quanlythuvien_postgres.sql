SELECT current_user;	
SELECT datname, pg_catalog.pg_get_userbyid(datdba) AS owner
FROM pg_database;
ALTER DATABASE quanlythuvien RENAME TO qlthuvien;
SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'accounts';