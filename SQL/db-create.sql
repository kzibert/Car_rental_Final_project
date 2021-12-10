-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema CarRental
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema CarRental
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `CarRental` DEFAULT CHARACTER SET utf8 ;
USE `CarRental` ;

-- -----------------------------------------------------
-- Table `CarRental`.`brand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`brand` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`brand` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idcar_brand_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `brand_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CarRental`.`car`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`car` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`car` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `brand_id` INT NOT NULL,
  `model` VARCHAR(255) NOT NULL,
  `quality_class` VARCHAR(45) NOT NULL,
  `price` DECIMAL(9) NOT NULL,
  `car_status` VARCHAR(45) NOT NULL COMMENT 'Car status: Available; Damaged.',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_car_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_cars_car_brand1_idx` (`brand_id` ASC) VISIBLE,
  CONSTRAINT `fk_car_brand1`
    FOREIGN KEY (`brand_id`)
    REFERENCES `CarRental`.`brand` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `CarRental`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`role` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `role_name_UNIQUE` (`name` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `CarRental`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`user` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `status` TINYINT(1) NOT NULL COMMENT 'User status: Unblocked; Blocked.',
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `roles_id` INT NOT NULL,
  `block_comments` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_user_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_users_roles1_idx` (`roles_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `CarRental`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `CarRental`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`orders` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rent_start` DATE NOT NULL,
  `rent_end` DATE NOT NULL,
  `passport` VARCHAR(255) NOT NULL,
  `driver` TINYINT(1) NOT NULL,
  `user_id` INT NOT NULL,
  `car_id` INT NOT NULL,
  `cancel_comments` VARCHAR(255) NULL,
  `status` VARCHAR(45) NOT NULL COMMENT 'Order status: Order received; Order Approved; Order cancelled.',
  PRIMARY KEY (`id`),
  INDEX `fk_order_user_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_order_car1_idx` (`car_id` ASC) VISIBLE,
  UNIQUE INDEX `id_order_UNIQUE` (`id` ASC) VISIBLE,
  CONSTRAINT `fk_order_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `CarRental`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_car1`
    FOREIGN KEY (`car_id`)
    REFERENCES `CarRental`.`car` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `CarRental`.`order_receipt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`order_receipt` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`order_receipt` (
  `cost` INT NOT NULL,
  `payment_status` TINYINT(1) NOT NULL COMMENT 'Payment status: Not paid; Paid.',
  `payment_date` DATE NULL,
  `order_id` INT NOT NULL,
  INDEX `fk_order_receipt_order1_idx` (`order_id` ASC) VISIBLE,
  PRIMARY KEY (`order_id`),
  CONSTRAINT `fk_order_receipt_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `CarRental`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CarRental`.`damage_receipt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CarRental`.`damage_receipt` ;

CREATE TABLE IF NOT EXISTS `CarRental`.`damage_receipt` (
  `cost` INT NOT NULL,
  `payment_status` TINYINT(1) NOT NULL COMMENT 'Damage payment status: Not paid; Paid.',
  `payment_date` DATE NULL,
  `order_id` INT NOT NULL,
  INDEX `fk_damage_receipt_order1_idx` (`order_id` ASC) VISIBLE,
  PRIMARY KEY (`order_id`),
  CONSTRAINT `fk_damage_receipt_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `CarRental`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- Insert roles:
INSERT INTO `role` VALUES (1, 'Admin');
INSERT INTO `role` VALUES (2, 'Manager');
INSERT INTO `role` VALUES (3, 'User');

-- Insert users:
INSERT INTO `user` VALUES (default,'admin@gmail.com','adminpass','0','Евгений','Иванов', '1', NULL);
INSERT INTO `user` VALUES (default,'sid@gmail.com','sidorpass','1','Peter','Sidorov','3','Bad behavior');
INSERT INTO `user` VALUES (default,'manager@gmail.com','managerpass','0','Nick','Fury','2',NULL);
INSERT INTO `user` VALUES (default,'jack@gmail.com','jackpass','0','Jack','Torov','3',NULL);
INSERT INTO `user` VALUES (default,'user@gmail.com','userpass','0','Ivan','Petrov','3', NULL);

-- Insert brands:
INSERT INTO `brand` VALUES (1,'BMW');
INSERT INTO `brand` VALUES (2,'Volkswagen');
INSERT INTO `brand` VALUES (3,'Ford');
INSERT INTO `brand` VALUES (4,'Toyota');
INSERT INTO `brand` VALUES (5,'Mercedes');

-- Insert cars:
INSERT INTO `car` VALUES (default,'2','Passat B7','C','350','Available');
INSERT INTO `car` VALUES (default,'3','Focus','A','200','Available');
INSERT INTO `car` VALUES (default,'5','AMG','E','450','Damaged');
INSERT INTO `car` VALUES (default,'1','X5','C','300','Available');
INSERT INTO `car` VALUES (default,'4','Prius','C','150','Available');
INSERT INTO `car` VALUES (default,'3','Kuga','B','230','Available');
INSERT INTO `car` VALUES (default,'4','Corolla','B','250','Available');
