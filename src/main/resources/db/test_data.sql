INSERT INTO public.user_group (id, uuid, name, created_at, updated_at) VALUES (1,'1eee76f6-e63b-46b9-9265-5597cd61aaba', 'AdminGroup', 1711221201, 1711221582);
INSERT INTO public.user_group (id, uuid, name, created_at, updated_at) VALUES (2,'2eee76f6-e63b-46b9-9265-5597cd61aaba', 'UserGroup', 1711221312, 1711221582);
INSERT INTO public.user_group (id, uuid, name, created_at, updated_at) VALUES (3,'3eee76f6-e63b-46b9-9265-5597cd61aaba', 'ReadOnlyGroup', 1711221423, 1711221582);


INSERT INTO public.user (id, created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (1, 1711218201, 1711221100, 'user@gmail.com', '9c6237a8-a731-4f4a-a45d-b4c5b4291741', 'User', 1711221201, 'Useryan', '12345', '1709937187', 'R5X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'ACTIVE', 2);
INSERT INTO public.user (id, created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (2, 1711218201, 1711221100, 'user1@gmail.com', '9c6237a8-a731-4f4a-a45d-b4c5b4291742', 'User1', 1711221201, 'Useryan', '12345', '1709937187', 'R5X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'UNVERIFIED', 2);
INSERT INTO public.user (id, created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (3, 1711218201, 1711221100, 'user2@gmail.com', '9c6237a8-a731-4f4a-a45d-b4c5b4291743', 'User2', 1711221201, 'Ueryan', '12345', '1709937187', 'R5X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'ACTIVE', 2);
INSERT INTO public.user (id, created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (4, 1711218201, 1711221100, 'user3@gmail.com', '9c6237a8-a731-4f4a-a45d-b4c5b4291744', 'User3', 1711221201, 'Useyan', '12345', '1709937187', 'R5X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'UNVERIFIED', 2);
INSERT INTO public.user (id, created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (5, 1711218201, 1711221100, 'user4@gmail.com', '9c6237a8-a731-4f4a-a45d-b4c5b4291745', 'User4', 1711221201, 'Usryan', '12345', '1709937187', 'R5X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'DELETED', 2);

INSERT INTO public.domain (id, code, name) VALUES (1, 'vm', 'virtual machine');
INSERT INTO public.domain (id, code, name) VALUES (2, 'cluster', 'cluster');
INSERT INTO public.domain (id, code, name) VALUES (3, 'dc', 'datacenter');
INSERT INTO public.domain (id, code, name) VALUES (4, 'host', 'host');


INSERT INTO public.object_group (id, uuid, name, created_at, updated_at) VALUES (1, '1aee76f6-e63b-46b9-9265-5597cd61aaba', 'VMObjectGroup', 1711221423, 1711221582);
INSERT INTO public.object_group (id, uuid, name, created_at, updated_at) VALUES (2, '2bee76f6-e63b-46b9-9265-5597cd61aabb', 'ClusterObjectGroup', 1711221423, 1711221582);
INSERT INTO public.object_group (id, uuid, name, created_at, updated_at) VALUES (3, '3bee76f6-e63b-46b9-9265-5597cd61aabc', 'DCObjectGroup', 1711221423, 1711221582);
INSERT INTO public.object_group (id, uuid, name, created_at, updated_at) VALUES (4, '4bee76f6-e63b-46b9-9265-5597cd61aabd', 'HostObjectGroup', 1711221423, 1711221582);


INSERT INTO public.permission (id, name, domain_id) VALUES (1, 'create', 1);
INSERT INTO public.permission (id, name, domain_id) VALUES (2, 'read', 1);
INSERT INTO public.permission (id, name, domain_id) VALUES (3, 'update', 1);
INSERT INTO public.permission (id, name, domain_id) VALUES (4, 'delete', 1);
INSERT INTO public.permission (id, name, domain_id) VALUES (5, 'create', 2);
INSERT INTO public.permission (id, name, domain_id) VALUES (6, 'read', 2);
INSERT INTO public.permission (id, name, domain_id) VALUES (7, 'update', 2);
INSERT INTO public.permission (id, name, domain_id) VALUES (8, 'delete', 2);
INSERT INTO public.permission (id, name, domain_id) VALUES (9, 'create', 3);
INSERT INTO public.permission (id, name, domain_id) VALUES (10, 'read', 3);
INSERT INTO public.permission (id, name, domain_id) VALUES (11, 'update', 3);
INSERT INTO public.permission (id, name, domain_id) VALUES (12, 'delete', 3);
INSERT INTO public.permission (id, name, domain_id) VALUES (13, 'create', 4);
INSERT INTO public.permission (id, name, domain_id) VALUES (14, 'read', 4);
INSERT INTO public.permission (id, name, domain_id) VALUES (15, 'update', 4);
INSERT INTO public.permission (id, name, domain_id) VALUES (16, 'delete', 4);


INSERT INTO public.role (id, name) VALUES (1, 'Admin');
INSERT INTO public.role (id, name) VALUES (2, 'User');
INSERT INTO public.role (id, name) VALUES (3, 'ReadOnly');


INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 3);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 4);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 5);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 6);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 7);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 8);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 9);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 10);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 11);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 12);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 13);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 14);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 15);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (1, 16);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 1);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 2);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 3);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 4);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 5);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 6);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 7);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 8);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 9);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 10);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 11);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 12);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 13);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 14);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 15);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (2, 16);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (3, 2);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (3, 6);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (3, 10);
INSERT INTO public.role_permission (role_id, permission_id) VALUES (3, 14);


INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 1, 1711221423, 1711221582, '1fff76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj1', 1, 1);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 2, 1711221423, 1711221582, '2eee76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj2', 1, 1);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 3, 1711221423, 1711221582, '3fff76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj3', 1, 1);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 4, 1711221423, 1711221582, '4eee76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj4', 1, 1);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('CL', 5, 1711221423, 1711221582, '5aee76f6-e63b-46b9-9265-5597cd61aaba', 'cluObj1', 2, 2);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('CL', 6, 1711221423, 1711221582, '6bee76f6-e63b-46b9-9265-5597cd61aaba', 'cluObj2', 2, 2);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('CL', 7, 1711221423, 1711221582, '7cee76f6-e63b-46b9-9265-5597cd61aaba', 'cluObj3', 2, 2);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('DC', 8, 1711221423, 1711221582, '8bee76f6-e63b-46b9-9265-5597cd61aaba', 'dcObj1', 3, 3);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('DC', 9, 1711221423, 1711221582, '9cee76f6-e63b-46b9-9265-5597cd61aaba', 'dcObj2', 3, 3);
INSERT INTO public.object (entity_type, id, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('HOST', 10, 1711221423, 1711221582, '0cee76f6-e63b-46b9-9265-5597cd61aaba', 'hostObj1', 4, 4);


INSERT INTO public.access_control (id, role_id, user_group_id) VALUES (1, 1, 1);
INSERT INTO public.access_control (id, role_id, user_group_id) VALUES (2, 2, 2);
INSERT INTO public.access_control (id, role_id, user_group_id) VALUES (3, 3, 3);


INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (1, 1);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (1, 2);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (1, 3);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (1, 4);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (2, 1);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (2, 2);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (2, 3);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (2, 4);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (3, 1);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (3, 2);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (3, 3);
INSERT INTO public.access_control_object_groups (id, object_group_id) VALUES (3, 4);
