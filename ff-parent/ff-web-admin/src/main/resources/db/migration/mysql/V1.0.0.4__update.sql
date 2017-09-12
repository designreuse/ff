CREATE TABLE `gfi_sync` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cnt_nok` int(11) DEFAULT NULL,
  `cnt_ok` int(11) DEFAULT NULL,
  `cnt_total` int(11) DEFAULT NULL,
  `end_time` datetime NOT NULL,
  `start_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

CREATE TABLE `gfi_sync_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_data` longtext,
  `error` longtext,
  `gfi_sync` int(11) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pw4o66i1tf7my1xlaufd2aaoh` (`gfi_sync`),
  KEY `FK_ko46kbt3t7e2tvqkjurkkv4s2` (`user`),
  CONSTRAINT `FK_ko46kbt3t7e2tvqkjurkkv4s2` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_pw4o66i1tf7my1xlaufd2aaoh` FOREIGN KEY (`gfi_sync`) REFERENCES `gfi_sync` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;