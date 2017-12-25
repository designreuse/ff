INSERT INTO [dbo].[permission] (id, name) VALUES 
(9101, 'settings.roles.create'),
(9102, 'settings.roles.read'),
(9103, 'settings.roles.update'),
(9104, 'settings.roles.delete');

INSERT INTO [dbo].[role_permission] (role_id, permission_id) VALUES 
(1, 9101),
(1, 9102),
(1, 9103),
(1, 9104);