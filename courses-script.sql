 -- =============================================
-- Author: Josué Sangabriel Alarcón.
-- =============================================

-- -----------------------------------------------------
-- Schema courses-demo
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `courses-demo` ;

-- -----------------------------------------------------
-- Schema courses-demo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `courses-demo` DEFAULT CHARACTER SET utf8 ;
USE `courses-demo` ;

-- -----------------------------------------------------
-- Table `courses-demo`.`teachers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courses-demo`.`Teachers` ;

CREATE TABLE IF NOT EXISTS `courses-demo`.`Teachers` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `email` VARCHAR(500) NOT NULL unique,
 `password` VARCHAR(255) NOT NULL,
 `name` VARCHAR(500) NOT NULL,
 `studyGrade` VARCHAR(100) NOT NULL,
 `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
 `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`));
 
 -- -----------------------------------------------------
-- Table `courses-demo`.`courses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courses-demo`.`Courses` ;

CREATE TABLE IF NOT EXISTS `courses-demo`.`Courses` (
 `id` INT NOT NULL AUTO_INCREMENT,
 `nrc` VARCHAR(100) NOT NULL unique,
 `name` VARCHAR(500) NOT NULL,
 `courseType` VARCHAR(100) NOT NULL,
 `idTeacher` INT NOT NULL,
 `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
 `updatedAt` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 FOREIGN KEY (`idTeacher`) REFERENCES `courses-demo`.`Teachers` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE,
 PRIMARY KEY (`id`));
 