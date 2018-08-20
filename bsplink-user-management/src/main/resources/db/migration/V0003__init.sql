
insert into bsplink_user.bsplink_option (id) values ('AdmIssue');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('AdmIssue', 'AIRLINE');

insert into bsplink_user.bsplink_option (id) values ('AcmIssue');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('AcmIssue', 'AIRLINE');

insert into bsplink_user.bsplink_option (id) values ('IndirectRefundIssue');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('IndirectRefundIssue', 'AGENT');


insert into bsplink_user.bsplink_option (id) values ('FileDownload');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileDownload', 'AGENT');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileDownload', 'AIRLINE');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileDownload', 'BSP');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileDownload', 'DPC');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileDownload', 'GDS');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileDownload', 'THIRDPARTY');

insert into bsplink_user.bsplink_option (id) values ('FileUpload');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileUpload', 'AGENT');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileUpload', 'AIRLINE');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileUpload', 'BSP');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileUpload', 'DPC');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileUpload', 'GDS');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileUpload', 'THIRDPARTY');

insert into bsplink_user.bsplink_option (id) values ('FileQuery');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('FileQuery', 'BSP');

insert into bsplink_user.bsplink_option (id) values ('SftpMaintenance');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('SftpMaintenance', 'AIRLINE');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('SftpMaintenance', 'BSP');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('SftpMaintenance', 'GDS');

insert into bsplink_user.bsplink_option (id) values ('UserMaintenance');
insert into bsplink_user.bsplink_option_user_types (bsplink_option_id, user_types) values ('UserMaintenance', 'BSP');
