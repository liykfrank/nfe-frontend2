/*
 * Example data for development.
 */
DELETE FROM bsplink_file;

INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (1, 'ILei8385_20180128_file1', 'fileType1', 1001, '2018-01-01T00:00:00Z', 'DELETED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (2, 'ILei8385_20180128_file2', 'fileType2', 1002, '2018-01-02T00:00:00Z', 'UNREAD');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (3, 'ILei8385_20180128_file3', 'fileType3', 1003, '2018-01-03T00:00:00Z', 'DOWNLOADED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (4, 'ILei8385_20180128_file4', 'fileType4', 1004, '2018-01-04T00:00:00Z', 'DOWNLOADED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (5, 'ILei8385_20180128_file5', 'fileType5', 1005, '2018-01-05T00:00:00Z', 'UNREAD');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (6, 'ILei8385_20180128_file6', 'fileType6', 1006, '2018-01-06T00:00:00Z', 'DELETED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (7, 'ILei8385_20180128_file7', 'fileType7', 1007, '2018-01-07T00:00:00Z', 'UNREAD');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (8, 'ILei8385_20180128_file8', 'fileType8', 1008, '2018-01-08T00:00:00Z', 'DOWNLOADED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (9, 'ILei8385_20180128_file9', 'fileType9', 1009, '2018-01-09T00:00:00Z', 'DOWNLOADED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (10, 'ILei8385_20180128_file10', 'fileType10', 1010, '2018-01-10T00:00:00Z', 'UNREAD');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (11, 'ILei8385_20180128_603', 'fileType11', 1010, '2018-01-10T00:00:00Z', 'UNREAD');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (12, 'ILei8385_20180128_604', 'fileType12', 1010, '2018-01-10T00:00:00Z', 'DELETED');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (13, 'CA288385_20180126_CONVPROCESS_203', 'fileType13', 1010, '2018-01-10T00:00:00Z', 'UNREAD');
INSERT INTO bsplink_file (id, name, type, bytes, upload_date_time, status) VALUES (14, 'ESe62203_20180506.zip', 'fileType14', 1010, '2018-01-10T00:00:00Z', 'UNREAD');

ALTER SEQUENCE hibernate_sequence RESTART WITH 15;
