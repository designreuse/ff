/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table algorithm_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `algorithm_item`;

CREATE TABLE `algorithm_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `code` varchar(8) NOT NULL,
  `conditional_item_code` varchar(8) DEFAULT NULL,
  `operator` varchar(16) NOT NULL,
  `status` varchar(16) NOT NULL,
  `type` varchar(16) NOT NULL,
  `company_item` int(11) DEFAULT NULL,
  `tender_item` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6rl2vibxo61aj97ur6ieieks` (`company_item`),
  KEY `FK_4aagso9q0posdfwnkmjn6deka` (`tender_item`),
  CONSTRAINT `FK_4aagso9q0posdfwnkmjn6deka` FOREIGN KEY (`tender_item`) REFERENCES `item` (`id`),
  CONSTRAINT `FK_6rl2vibxo61aj97ur6ieieks` FOREIGN KEY (`company_item`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(16) NOT NULL,
  `text` longtext,
  `image` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jopb9qivnsxwrahkamrv98hcw` (`image`),
  CONSTRAINT `FK_jopb9qivnsxwrahkamrv98hcw` FOREIGN KEY (`image`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table business_relationship_manager
# ------------------------------------------------------------

DROP TABLE IF EXISTS `business_relationship_manager`;

CREATE TABLE `business_relationship_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(128) DEFAULT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table city
# ------------------------------------------------------------

DROP TABLE IF EXISTS `city`;

CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `development_index` varchar(32) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `county` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1oe5j7dg7jmvamcrtplpubuc2` (`county`),
  CONSTRAINT `FK_1oe5j7dg7jmvamcrtplpubuc2` FOREIGN KEY (`county`) REFERENCES `county` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `code` varchar(64) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7cs7aqu97p4ep7ocbylx8cchs` (`user`),
  CONSTRAINT `FK_7cs7aqu97p4ep7ocbylx8cchs` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table company_investment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company_investment`;

CREATE TABLE `company_investment` (
  `company_id` int(11) NOT NULL,
  `investment_id` int(11) NOT NULL,
  PRIMARY KEY (`company_id`,`investment_id`),
  KEY `FK_f7jai29d852gry6v6npfe0l14` (`investment_id`),
  KEY `FK_ibu82eokrdn99jrxpulyjf7ca` (`company_id`),
  CONSTRAINT `FK_f7jai29d852gry6v6npfe0l14` FOREIGN KEY (`investment_id`) REFERENCES `investment` (`id`),
  CONSTRAINT `FK_ibu82eokrdn99jrxpulyjf7ca` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table company_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company_item`;

CREATE TABLE `company_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `value` longtext,
  `company` int(11) DEFAULT NULL,
  `item` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_iffgmimr917ylxr63thetmbfq` (`company`),
  KEY `FK_sejipjuelub5j2wd0ee898n41` (`item`),
  CONSTRAINT `FK_iffgmimr917ylxr63thetmbfq` FOREIGN KEY (`company`) REFERENCES `company` (`id`),
  CONSTRAINT `FK_sejipjuelub5j2wd0ee898n41` FOREIGN KEY (`item`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table county
# ------------------------------------------------------------

DROP TABLE IF EXISTS `county`;

CREATE TABLE `county` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table email
# ------------------------------------------------------------

DROP TABLE IF EXISTS `email`;

CREATE TABLE `email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `subject` longtext,
  `text` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table image
# ------------------------------------------------------------

DROP TABLE IF EXISTS `image`;

CREATE TABLE `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `base64` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table impression
# ------------------------------------------------------------

DROP TABLE IF EXISTS `impression`;

CREATE TABLE `impression` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `entity_id` int(11) NOT NULL,
  `entity_type` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table investment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `investment`;

CREATE TABLE `investment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(16) NOT NULL,
  `text` longtext,
  `image` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ci9hlt21hhk0kgtxom5768fks` (`image`),
  CONSTRAINT `FK_ci9hlt21hhk0kgtxom5768fks` FOREIGN KEY (`image`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item`;

CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `code` varchar(8) NOT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `entity_type` varchar(16) NOT NULL,
  `help` longtext,
  `mandatory` tinyint(1) NOT NULL,
  `meta_tag` varchar(64) DEFAULT NULL,
  `position` int(11) NOT NULL,
  `status` varchar(16) NOT NULL,
  `text` longtext NOT NULL,
  `type` varchar(32) NOT NULL,
  `widget_item` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table item_option
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item_option`;

CREATE TABLE `item_option` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `position` int(11) NOT NULL,
  `text` longtext,
  `item` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gq6nqdsaic1mj0bpql5xhu521` (`item`),
  CONSTRAINT `FK_gq6nqdsaic1mj0bpql5xhu521` FOREIGN KEY (`item`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nkd
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nkd`;

CREATE TABLE `nkd` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `activity` varchar(8) NOT NULL,
  `activity_name` varchar(512) NOT NULL,
  `area` varchar(8) NOT NULL,
  `sector` varchar(8) NOT NULL,
  `sector_name` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `FK_fn4pldu982p9u158rpk6nho5k` (`permission_id`),
  KEY `FK_j89g87bvih4d6jbxjcssrybks` (`role_id`),
  CONSTRAINT `FK_fn4pldu982p9u158rpk6nho5k` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `FK_j89g87bvih4d6jbxjcssrybks` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tender
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tender`;

CREATE TABLE `tender` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(16) NOT NULL,
  `text` longtext,
  `image` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fund0tsyw5xr44hotbb3k86mr` (`image`),
  CONSTRAINT `FK_fund0tsyw5xr44hotbb3k86mr` FOREIGN KEY (`image`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tender_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tender_item`;

CREATE TABLE `tender_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `value` longtext,
  `item` int(11) DEFAULT NULL,
  `tender` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_h4vmvk4gxt2tsmb9qqwwkyaba` (`item`),
  KEY `FK_ohhjjdu4ltdkbxjkg3494ip2q` (`tender`),
  CONSTRAINT `FK_h4vmvk4gxt2tsmb9qqwwkyaba` FOREIGN KEY (`item`) REFERENCES `item` (`id`),
  CONSTRAINT `FK_ohhjjdu4ltdkbxjkg3494ip2q` FOREIGN KEY (`tender`) REFERENCES `tender` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `demo_user` tinyint(1) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(128) NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `last_name` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `registration_code` varchar(255) DEFAULT NULL,
  `registration_code_confirmed` datetime DEFAULT NULL,
  `registration_code_sent` datetime DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `business_relationship_manager` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5t6qrhu2fappmelw8hc6bkiky` (`business_relationship_manager`),
  CONSTRAINT `FK_5t6qrhu2fappmelw8hc6bkiky` FOREIGN KEY (`business_relationship_manager`) REFERENCES `business_relationship_manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user_email
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_email`;

CREATE TABLE `user_email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `email` int(11) DEFAULT NULL,
  `tender` int(11) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qm35y897bny0qjqutviap50yi` (`email`),
  KEY `FK_t2qs5vh123oeev7ssllttar6e` (`tender`),
  KEY `FK_dy9lb8d1fjx5npkdnna1hlg3x` (`user`),
  CONSTRAINT `FK_dy9lb8d1fjx5npkdnna1hlg3x` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_qm35y897bny0qjqutviap50yi` FOREIGN KEY (`email`) REFERENCES `email` (`id`),
  CONSTRAINT `FK_t2qs5vh123oeev7ssllttar6e` FOREIGN KEY (`tender`) REFERENCES `tender` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_group`;

CREATE TABLE `user_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user_group_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_group_user`;

CREATE TABLE `user_group_user` (
  `user_group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_group_id`,`user_id`),
  KEY `FK_phklr5y0o9nl2xt5v47gkwlko` (`user_id`),
  KEY `FK_9nu8cvmmc0olpqdulhwhe3ni9` (`user_group_id`),
  CONSTRAINT `FK_9nu8cvmmc0olpqdulhwhe3ni9` FOREIGN KEY (`user_group_id`) REFERENCES `user_group` (`id`),
  CONSTRAINT `FK_phklr5y0o9nl2xt5v47gkwlko` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;