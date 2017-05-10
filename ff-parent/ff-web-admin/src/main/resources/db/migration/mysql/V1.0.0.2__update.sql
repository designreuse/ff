ALTER TABLE company ADD sync_data tinyint(1) DEFAULT 0;
ALTER TABLE company ADD hide_sync_data_warning tinyint(1) DEFAULT 0;

ALTER TABLE item ADD emphasize tinyint(1) DEFAULT 0;

ALTER TABLE item_option ADD url longtext;