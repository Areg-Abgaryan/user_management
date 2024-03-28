INSERT INTO public.user_group (uuid, name, created_at, updated_at) VALUES ('1eee76f6-e63b-46b9-9265-5597cd61aaba', 'AdminGroup', 1711221201, 1711221582);
INSERT INTO public.user_group (uuid, name, created_at, updated_at) VALUES ('2eee76f6-e63b-46b9-9265-5597cd61aaba', 'UserGroup', 1711221312, 1711221582);
INSERT INTO public.user_group (uuid, name, created_at, updated_at) VALUES ('3eee76f6-e63b-46b9-9265-5597cd61aaba', 'ReadOnlyGroup', 1711221423, 1711221582);


INSERT INTO public.user (created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (1711218201, 1711221100, 'user@gmail.com', '0c6237a8-a731-4f4a-a45d-b4c5b4291741', 'User1', 1711220209, 'Useryan', '012345', '1709937187', 'A4X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'ACTIVE', 2);
INSERT INTO public.user (created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (1711218201, 1711221100, 'user1@gmail.com', '8c6237a8-a731-4f4a-a45d-b4c5b4291742', 'User2', 1711221208, 'Useryan', '123456', '1709937187', 'B5X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'UNVERIFIED', 2);
INSERT INTO public.user (created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (1711218201, 1711221100, 'user2@gmail.com', '6c6237a8-a731-4f4a-a45d-b4c5b4291743', 'User3', 1711222207, 'Ueryan', '234567', '1709937187', 'C6X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'ACTIVE', 2);
INSERT INTO public.user (created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (1711218201, 1711221100, 'user3@gmail.com', '4c6237a8-a731-4f4a-a45d-b4c5b4291744', 'User4', 1711223206, 'Useyan', '345678', '1709937187', 'D7X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'UNVERIFIED', 2);
INSERT INTO public.user (created_at, updated_at, email, uuid, first_name, last_login_at, last_name, otp, otp_creation_time, password, salt, status, user_group_id) VALUES (1711218201, 1711221100, 'user4@gmail.com', '2c6237a8-a731-4f4a-a45d-b4c5b4291745', 'User5', 1711224205, 'Useryan', '456789', '1709937187', 'E8X6T6/cGTSqRS9jvj/x1LgLnacVRKU3qN/U4mqskmg=', 'xl3h&2bWF&23GxI2', 'DELETED', 2);

INSERT INTO public.domain (code, name) VALUES ('vm', 'virtual machine');
INSERT INTO public.domain (code, name) VALUES ('cluster', 'cluster');
INSERT INTO public.domain (code, name) VALUES ('dc', 'datacenter');
INSERT INTO public.domain (code, name) VALUES ('host', 'host');


INSERT INTO public.object_group (uuid, name, created_at, updated_at) VALUES ('1aee76f6-e63b-46b9-9265-5597cd61aaba', 'VMObjectGroup', 1711221423, 1711221582);
INSERT INTO public.object_group (uuid, name, created_at, updated_at) VALUES ('2bee76f6-e63b-46b9-9265-5597cd61aabb', 'ClusterObjectGroup', 1711221423, 1711221582);
INSERT INTO public.object_group (uuid, name, created_at, updated_at) VALUES ('3bee76f6-e63b-46b9-9265-5597cd61aabc', 'DCObjectGroup', 1711221423, 1711221582);
INSERT INTO public.object_group (uuid, name, created_at, updated_at) VALUES ('4bee76f6-e63b-46b9-9265-5597cd61aabd', 'HostObjectGroup', 1711221423, 1711221582);


INSERT INTO public.permission (name, domain_id) VALUES ('create', 1);
INSERT INTO public.permission (name, domain_id) VALUES ('read', 1);
INSERT INTO public.permission (name, domain_id) VALUES ('update', 1);
INSERT INTO public.permission (name, domain_id) VALUES ('delete', 1);
INSERT INTO public.permission (name, domain_id) VALUES ('create', 2);
INSERT INTO public.permission (name, domain_id) VALUES ('read', 2);
INSERT INTO public.permission (name, domain_id) VALUES ('update', 2);
INSERT INTO public.permission (name, domain_id) VALUES ('delete', 2);
INSERT INTO public.permission (name, domain_id) VALUES ('create', 3);
INSERT INTO public.permission (name, domain_id) VALUES ('read', 3);
INSERT INTO public.permission (name, domain_id) VALUES ('update', 3);
INSERT INTO public.permission (name, domain_id) VALUES ('delete', 3);
INSERT INTO public.permission (name, domain_id) VALUES ('create', 4);
INSERT INTO public.permission (name, domain_id) VALUES ('read', 4);
INSERT INTO public.permission (name, domain_id) VALUES ('update', 4);
INSERT INTO public.permission (name, domain_id) VALUES ('delete', 4);


INSERT INTO public.role (name) VALUES ('Admin');
INSERT INTO public.role (name) VALUES ('User');
INSERT INTO public.role (name) VALUES ('ReadOnly');


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


INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 1711221423, 1711221582, '1fff76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj1', 1, 1);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 1711221423, 1711221582, '2eee76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj2', 1, 1);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 1711221423, 1711221582, '3fff76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj3', 1, 1);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('VM', 1711221423, 1711221582, '4eee76f6-e63b-46b9-9265-5597cd61aaba', 'vmObj4', 1, 1);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('CL', 1711221423, 1711221582, '5aee76f6-e63b-46b9-9265-5597cd61aaba', 'cluObj1', 2, 2);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('CL', 1711221423, 1711221582, '6bee76f6-e63b-46b9-9265-5597cd61aaba', 'cluObj2', 2, 2);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('CL', 1711221423, 1711221582, '7cee76f6-e63b-46b9-9265-5597cd61aaba', 'cluObj3', 2, 2);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('DC', 1711221423, 1711221582, '8bee76f6-e63b-46b9-9265-5597cd61aaba', 'dcObj1', 3, 3);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('DC', 1711221423, 1711221582, '9cee76f6-e63b-46b9-9265-5597cd61aaba', 'dcObj2', 3, 3);
INSERT INTO public.object (entity_type, created_at, updated_at, uuid, name, domain_id, object_group_id) VALUES ('HOST', 1711221423, 1711221582, '0cee76f6-e63b-46b9-9265-5597cd61aaba', 'hostObj1', 4, 4);


INSERT INTO public.access_control (role_id, user_group_id) VALUES (1, 1);
INSERT INTO public.access_control (role_id, user_group_id) VALUES (2, 2);
INSERT INTO public.access_control (role_id, user_group_id) VALUES (3, 3);


INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (1, 1);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (1, 2);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (1, 3);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (1, 4);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (2, 1);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (2, 2);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (2, 3);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (2, 4);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (3, 1);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (3, 2);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (3, 3);
INSERT INTO public.access_control_object_groups (access_control_id, object_group_id) VALUES (3, 4);
