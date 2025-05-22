-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema DB_PASSWORD_REDACTEDtrip
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema DB_PASSWORD_REDACTEDtrip
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tripdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `tripdb` ;

-- -----------------------------------------------------
-- Table `DB_PASSWORD_REDACTEDtrip`.`sidos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tripdb`.`sidos` ;

CREATE TABLE IF NOT EXISTS `tripdb`.`sidos` (
  `no` int NOT NULL AUTO_INCREMENT  comment '시도번호',
  `sido_code` int NOT NULL comment '시도코드',
  `sido_name` varchar(20) DEFAULT NULL comment '시도이름',
  PRIMARY KEY (`no`),
  UNIQUE INDEX `sido_code_UNIQUE` (`sido_code` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci comment '시도정보테이블';


-- -----------------------------------------------------
-- Table `DB_PASSWORD_REDACTEDtrip`.`guguns`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tripdb`.`guguns` ;

CREATE TABLE IF NOT EXISTS `tripdb`.`guguns` (
  `no` int NOT NULL AUTO_INCREMENT comment '구군번호',
  `sido_code` int NOT NULL comment '시도코드',
  `gugun_code` int NOT NULL comment '구군코드',
  `gugun_name` varchar(20) DEFAULT NULL comment '구군이름',
  PRIMARY KEY (`no`),
  INDEX `guguns_sido_to_sidos_cdoe_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `gugun_code_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `guguns_sido_to_sidos_cdoe_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `tripdb`.`sidos` (`sido_code`))
ENGINE = InnoDB
AUTO_INCREMENT = 235
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
comment '구군정보테이블';


-- -----------------------------------------------------
-- Table `DB_PASSWORD_REDACTEDtrip`.`contenttypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tripdb`.`contenttypes` ;

CREATE TABLE IF NOT EXISTS `tripdb`.`contenttypes` (
  `content_type_id` int NOT NULL comment '콘텐츠타입번호',
  `content_type_name` varchar(45) DEFAULT NULL comment '콘텐츠타입이름',
  PRIMARY KEY (`content_type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci comment '콘텐츠타입정보테이블';


-- -----------------------------------------------------
-- Table `DB_PASSWORD_REDACTEDtrip`.`attractions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tripdb`.`attractions` ;

CREATE TABLE IF NOT EXISTS `tripdb`.`attractions` (
  `no` int NOT NULL AUTO_INCREMENT  comment '명소코드',
  `content_id` int DEFAULT NULL comment '콘텐츠번호',
  `title` varchar(500) DEFAULT NULL comment '명소이름',
  `content_type_id` int DEFAULT NULL comment '콘텐츠타입',
  `area_code` int DEFAULT NULL comment '시도코드',
  `si_gun_gu_code` int DEFAULT NULL comment '구군코드',
  `first_image1` varchar(100) DEFAULT NULL comment '이미지경로1',
  `first_image2` varchar(100) DEFAULT NULL comment '이미지경로2',
  `map_level` int DEFAULT NULL comment '줌레벨',
  `latitude` decimal(20,17) DEFAULT NULL comment '위도',
  `longitude` decimal(20,17) DEFAULT NULL comment '경도',
  `tel` varchar(20) DEFAULT NULL comment '전화번호',
  `addr1` varchar(100) DEFAULT NULL comment '주소1',
  `addr2` varchar(100) DEFAULT NULL comment '주소2',
  `homepage` varchar(1000) DEFAULT NULL comment '홈페이지',
  `overview` varchar(10000) DEFAULT NULL comment '설명',
  PRIMARY KEY (`no`),
  INDEX `attractions_typeid_to_types_typeid_fk_idx` (`content_type_id` ASC) VISIBLE,
  INDEX `attractions_sido_to_sidos_code_fk_idx` (`area_code` ASC) VISIBLE,
  INDEX `attractions_sigungu_to_guguns_gugun_fk_idx` (`si_gun_gu_code` ASC) VISIBLE,
  CONSTRAINT `attractions_area_to_sidos_code_fk`
    FOREIGN KEY (`area_code`)
    REFERENCES `tripdb`.`sidos` (`sido_code`),
  CONSTRAINT `attractions_sigungu_to_guguns_gugun_fk`
    FOREIGN KEY (`si_gun_gu_code`)
    REFERENCES `tripdb`.`guguns` (`gugun_code`),
  CONSTRAINT `attractions_typeid_to_types_typeid_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `tripdb`.`contenttypes` (`content_type_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 56644
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
comment '명소정보테이블';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

== 회원 SQL
create database tripdb;

use tripdb;

create table member (
	id varchar(50) PRIMARY KEY,
    pw varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    email VARCHAR(45) NOT NULL unique,
    roll VARCHAR(30)
);

INSERT INTO member (id, pw, name, email, roll) VALUES ("domain", "1234", "관리자", "domain.DB_PASSWORD_REDACTED.com", "domain");

CREATE TABLE travel_posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,       -- 글번호 (기본 키)
    plan_id INT NOT NULL,                         -- 여행계획 ID (외래 키 등으로 연결 가능)
    title VARCHAR(255) NOT NULL,                  -- 제목
    author VARCHAR(100) NOT NULL,                 -- 작성자
    content TEXT NOT NULL,                        -- 게시글 내용
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,-- 작성일
    views INT DEFAULT 0                           -- 조회수
);

INSERT INTO travel_posts (plan_id, title, author, content, created_at, views) VALUES
(1, '대전 누가 노잼이라 했냐', '신짱구', '진짜 노잼 맞다.', '2024-02-28 10:12:00', 10),
(1, '대전 성심당 최고!', '김철수', '튀김소보로는 무조건 와서 먹어야 합니다. (진지)', '2024-02-27 15:45:00', 25),
(2, '여행 꿀팁!', '신짱아', '여행은 미리 계획하는 게 최고예요.', '2024-02-26 09:00:00', 32),
(2, '제주도 렌트카 팁', '홍길동', '공항 근처 말고 외곽에서 빌리는 게 싸요.', '2024-02-25 14:30:00', 18),
(3, '부산 여행 후기', '박지민', '광안리 야경 최고였어요.', '2024-02-24 11:10:00', 40),
(3, '부산 밀면 맛집', '이영희', '초량 밀면 강추합니다!', '2024-02-23 13:20:00', 12),
(4, '서울에서 당일치기 가능한 곳', '최유리', '남양주 물의정원 추천!', '2024-02-22 16:55:00', 22),
(4, '전주 여행 코스 공유', '이준호', '한옥마을부터 막걸리 골목까지!', '2024-02-21 10:00:00', 27),
(5, '속초 가는 길', '정다운', '서울에서 2시간 반이면 충분해요.', '2024-02-20 17:20:00', 15),
(5, '강릉 카페 추천', '김지훈', '안목해변 카페거리 분위기 좋아요.', '2024-02-19 12:00:00', 30);