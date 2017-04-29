/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table activity
# ------------------------------------------------------------

CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table algorithm_item
# ------------------------------------------------------------

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
  `organizational_unit` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5nw6y0obewou7dam5lvdw3muq` (`organizational_unit`),
  CONSTRAINT `FK_5nw6y0obewou7dam5lvdw3muq` FOREIGN KEY (`organizational_unit`) REFERENCES `organizational_unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table company
# ------------------------------------------------------------

CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `branch_office_number` varchar(255) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `company_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `other_business` longtext,
  `primary_business` varchar(255) DEFAULT NULL,
  `registration_number` varchar(255) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7cs7aqu97p4ep7ocbylx8cchs` (`user`),
  CONSTRAINT `FK_7cs7aqu97p4ep7ocbylx8cchs` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table company_item
# ------------------------------------------------------------

CREATE TABLE `company_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `currency` varchar(8) DEFAULT NULL,
  `value` longtext,
  `company` int(11) DEFAULT NULL,
  `item` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_iffgmimr917ylxr63thetmbfq` (`company`),
  KEY `FK_sejipjuelub5j2wd0ee898n41` (`item`),
  CONSTRAINT `FK_iffgmimr917ylxr63thetmbfq` FOREIGN KEY (`company`) REFERENCES `company` (`id`),
  CONSTRAINT `FK_sejipjuelub5j2wd0ee898n41` FOREIGN KEY (`item`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table contact
# ------------------------------------------------------------

CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `company_code` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `location` longtext,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `text` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table email
# ------------------------------------------------------------

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

CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `code` varchar(8) NOT NULL,
  `entity_type` varchar(16) NOT NULL,
  `help` longtext,
  `mandatory` tinyint(1) NOT NULL,
  `meta_tag` varchar(64) DEFAULT NULL,
  `position` int(11) NOT NULL,
  `status` varchar(16) NOT NULL,
  `summary_item` tinyint(1) DEFAULT NULL,
  `text` longtext NOT NULL,
  `type` varchar(32) NOT NULL,
  `widget_item` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table item_option
# ------------------------------------------------------------

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



# Dump of table organizational_unit
# ------------------------------------------------------------

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



# Dump of table permission
# ------------------------------------------------------------

CREATE TABLE `permission` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project
# ------------------------------------------------------------

CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `description` longtext,
  `name` varchar(255) NOT NULL,
  `company` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_i7cnlnqeieu2xsv9jh1od0t5n` (`company`),
  CONSTRAINT `FK_i7cnlnqeieu2xsv9jh1od0t5n` FOREIGN KEY (`company`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project_investment
# ------------------------------------------------------------

CREATE TABLE `project_investment` (
  `project_id` int(11) NOT NULL,
  `investment_id` int(11) NOT NULL,
  PRIMARY KEY (`project_id`,`investment_id`),
  KEY `FK_smxuuri2yegx8sc1xmk3afnd9` (`investment_id`),
  KEY `FK_qo5mmmxe5femovus3eyoksypy` (`project_id`),
  CONSTRAINT `FK_qo5mmmxe5femovus3eyoksypy` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FK_smxuuri2yegx8sc1xmk3afnd9` FOREIGN KEY (`investment_id`) REFERENCES `investment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project_item
# ------------------------------------------------------------

CREATE TABLE `project_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `currency` varchar(8) DEFAULT NULL,
  `value` longtext,
  `item` int(11) DEFAULT NULL,
  `project` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9wbtdxj19qmteif3v01hryu2c` (`item`),
  KEY `FK_foxoj24uhesknmmf77krud5sj` (`project`),
  CONSTRAINT `FK_9wbtdxj19qmteif3v01hryu2c` FOREIGN KEY (`item`) REFERENCES `item` (`id`),
  CONSTRAINT `FK_foxoj24uhesknmmf77krud5sj` FOREIGN KEY (`project`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table role
# ------------------------------------------------------------

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

CREATE TABLE `role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `FK_fn4pldu982p9u158rpk6nho5k` (`permission_id`),
  KEY `FK_j89g87bvih4d6jbxjcssrybks` (`role_id`),
  CONSTRAINT `FK_fn4pldu982p9u158rpk6nho5k` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `FK_j89g87bvih4d6jbxjcssrybks` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table subdivision1
# ------------------------------------------------------------

CREATE TABLE `subdivision1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `development_index` varchar(32) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table subdivision2
# ------------------------------------------------------------

CREATE TABLE `subdivision2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `development_index` varchar(32) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `subdivision1` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tonwsn904l6qsuisbu6ci8ehv` (`subdivision1`),
  CONSTRAINT `FK_tonwsn904l6qsuisbu6ci8ehv` FOREIGN KEY (`subdivision1`) REFERENCES `subdivision1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tender
# ------------------------------------------------------------

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

CREATE TABLE `tender_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `currency` varchar(8) DEFAULT NULL,
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

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(128) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_by` varchar(128) DEFAULT NULL,
  `last_modified_date` datetime NOT NULL,
  `demo_user` tinyint(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email2` varchar(255) DEFAULT NULL,
  `first_name` varchar(128) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `registration_code` varchar(255) DEFAULT NULL,
  `registration_code_confirmed` datetime DEFAULT NULL,
  `registration_code_sent` datetime DEFAULT NULL,
  `registration_type` varchar(32) DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL,
  `business_relationship_manager` int(11) DEFAULT NULL,
  `business_relationship_manager_substitute` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5t6qrhu2fappmelw8hc6bkiky` (`business_relationship_manager`),
  KEY `FK_5xd0dxshxc5f81dtl5r8cejg4` (`business_relationship_manager_substitute`),
  CONSTRAINT `FK_5t6qrhu2fappmelw8hc6bkiky` FOREIGN KEY (`business_relationship_manager`) REFERENCES `business_relationship_manager` (`id`),
  CONSTRAINT `FK_5xd0dxshxc5f81dtl5r8cejg4` FOREIGN KEY (`business_relationship_manager_substitute`) REFERENCES `business_relationship_manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user_email
# ------------------------------------------------------------

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

CREATE TABLE `user_group_user` (
  `user_group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_group_id`,`user_id`),
  KEY `FK_phklr5y0o9nl2xt5v47gkwlko` (`user_id`),
  KEY `FK_9nu8cvmmc0olpqdulhwhe3ni9` (`user_group_id`),
  CONSTRAINT `FK_9nu8cvmmc0olpqdulhwhe3ni9` FOREIGN KEY (`user_group_id`) REFERENCES `user_group` (`id`),
  CONSTRAINT `FK_phklr5y0o9nl2xt5v47gkwlko` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table zaba_mappings_location
# ------------------------------------------------------------

CREATE TABLE `zaba_mappings_location` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `subdivision2` varchar(512) DEFAULT NULL,
  `subdivision3` varchar(512) DEFAULT NULL,
  `zip_code` varchar(16) DEFAULT NULL,
  `subdivision2_development_index` varchar(16) DEFAULT NULL,
  `subdivision1` varchar(512) DEFAULT NULL,
  `subdivision1_development_index` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
