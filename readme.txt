
### table user ###

INSERT INTO your_table_name (
    id, create_by, create_date, delete_by, delete_date, 
    first_name, last_name, pass_word, role_id, status, 
    update_by, update_date, user_name
) VALUES
(1, NULL, '2024-11-03 16:21:42', NULL, NULL, 
 'Phort', 'Sophea', 'lxb0Yhu20Qd4B6SP7FX6UQ==', 1, 1, 
 NULL, '2024-11-03 22:27:28', 'sophea.phort'),
(3, NULL, '2024-11-03 16:23:35', NULL, NULL, 
 'Khim', 'keorathanak', 'lxb0Yhu20Qd4B6SP7FX6UQ==', 2, 0, 
 NULL, '2024-11-03 22:54:35', 'rathanak.com');



### table role ###

INSERT INTO table_name (id, description, name, create_by, create_date, delete_by, delete_date, status, update_by, update_date)
VALUES
(1, 'Can access erverything', 'Admin', NULL, '2024-10-29 16:12:54', NULL, NULL, 1, NULL, '2024-10-29 16:14:23'),
(2, 'User Can use only alitte', 'User', NULL, '2024-10-29 16:15:01', NULL, NULL, 1, NULL, NULL),
(3, 'data', 'testing', NULL, '2024-11-03 14:18:14', NULL, NULL, 0, NULL, NULL);
