ALTER TABLE `contact` DROP COLUMN `channel`;
ALTER TABLE `contact` DROP COLUMN `topic`;
ALTER TABLE `contact` DROP COLUMN `type`;

ALTER TABLE `contact` ADD COLUMN `location` longtext;