CREATE TABLE resources (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(128) NOT NULL,
  created timestamp NULL DEFAULT now(),
  modified timestamp DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;