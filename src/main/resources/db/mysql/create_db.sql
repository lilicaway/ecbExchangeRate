
-- mysql-5.7.11

CREATE SCHEMA `ecb_conversion` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `ecb_conversion`.`rates` (
  `date` DATE NOT NULL,
  `currency` CHAR(3) NOT NULL,
  `rate` DECIMAL(12,8) NULL COMMENT 'The maximum number of decimals we see in data is 5.\nThe maximum number of digits we see is 7.\nTo be conservative, we use DECIMAL(12,8).',
  PRIMARY KEY (`date`, `currency`));

ALTER TABLE `ecb_conversion`.`rates` 
CHANGE COLUMN `rate` `rate` DECIMAL(20,8) NULL DEFAULT NULL COMMENT 'The maximum number of decimals we see in data is 5.\nThe maximum number of digits we see is 7.\nTo be conservative, we use DECIMAL(12,8).' ;

