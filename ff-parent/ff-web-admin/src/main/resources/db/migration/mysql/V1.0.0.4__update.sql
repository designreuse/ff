CREATE TABLE `organizational_unit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `code` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `business_relationship_manager` DROP COLUMN `org_unit`;

ALTER TABLE `business_relationship_manager` ADD COLUMN `organizational_unit` int(11) DEFAULT NULL, ADD FOREIGN KEY (`organizational_unit`) REFERENCES `organizational_unit` (`id`);