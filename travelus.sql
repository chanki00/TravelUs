create database if not exists travelus;

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
CREATE SCHEMA IF NOT EXISTS `travelus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `travelus` ;

-- -----------------------------------------------------
-- Table `travelus`.`sidos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `travelus`.`sidos` ;

CREATE TABLE IF NOT EXISTS `travelus`.`sidos` (
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
DROP TABLE IF EXISTS `travelus`.`guguns` ;

CREATE TABLE IF NOT EXISTS `travelus`.`guguns` (
  `no` int NOT NULL AUTO_INCREMENT comment '구군번호',
  `sido_code` int NOT NULL comment '시도코드',
  `gugun_code` int NOT NULL comment '구군코드',
  `gugun_name` varchar(20) DEFAULT NULL comment '구군이름',
  PRIMARY KEY (`no`),
  INDEX `guguns_sido_to_sidos_cdoe_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `gugun_code_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `guguns_sido_to_sidos_cdoe_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `travelus`.`sidos` (`sido_code`))
ENGINE = InnoDB
AUTO_INCREMENT = 235
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
comment '구군정보테이블';


-- -----------------------------------------------------
-- Table `travelus`.`contenttypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `travelus`.`contenttypes` ;

CREATE TABLE IF NOT EXISTS `travelus`.`contenttypes` (
  `content_type_id` int NOT NULL comment '콘텐츠타입번호',
  `content_type_name` varchar(45) DEFAULT NULL comment '콘텐츠타입이름',
  PRIMARY KEY (`content_type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci comment '콘텐츠타입정보테이블';


-- -----------------------------------------------------
-- Table `travelus`.`attractions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `travelus`.`attractions` ;

CREATE TABLE IF NOT EXISTS `travelus`.`attractions` (
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
    REFERENCES `travelus`.`sidos` (`sido_code`),
  CONSTRAINT `attractions_sigungu_to_guguns_gugun_fk`
    FOREIGN KEY (`si_gun_gu_code`)
    REFERENCES `travelus`.`guguns` (`gugun_code`),
  CONSTRAINT `attractions_typeid_to_types_typeid_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `travelus`.`contenttypes` (`content_type_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 56644
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
comment '명소정보테이블';

CREATE SCHEMA IF NOT EXISTS `travelus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `travelus` ;

-- 사용자 정보
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    user_pw VARCHAR(255) NOT NULL,
    user_email VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(50),
    age VARCHAR(10) DEFAULT "other",
    gender ENUM('M', 'F', 'O') DEFAULT 'O',
    address VARCHAR(100) DEFAULT "other",
    intro TEXT,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    allow_invite BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE refresh_token (
    username VARCHAR(255) PRIMARY KEY,
    token TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE chatroom (
  room_id INT PRIMARY KEY AUTO_INCREMENT,
  is_temporary BOOLEAN DEFAULT TRUE,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE chatroom_users (
  chatroom_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (chatroom_id, user_id),
  FOREIGN KEY (chatroom_id) REFERENCES chatroom(room_id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE chat (
  chatroom_id INT NOT NULL,
  chat_id INT NOT NULL,
  user_id INT NOT NULL,
  content VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (chatroom_id, chat_id),
  FOREIGN KEY (chatroom_id, user_id) REFERENCES chatroom_users(chatroom_id, user_id) ON DELETE CASCADE
);

-- 태그 (성격 or 여행 스타일)
CREATE TABLE Tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type ENUM('성격', '여행') NOT NULL
);

-- 사용자 태그 연결
CREATE TABLE UserTags (
    user_id INT,
    tag_id INT,
    PRIMARY KEY (user_id, tag_id),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags(id) ON DELETE CASCADE
);


-- 여행 계획
CREATE TABLE tripplans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  chatroom_id INT UNIQUE, -- 💡 채팅방 참조 (1:1)
  title VARCHAR(100),
  description TEXT,
  destination VARCHAR(100),
  duration INT,
  members INT,
  transport VARCHAR(50),
  likes INT DEFAULT 0,
  shares INT DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_shared BOOLEAN DEFAULT FALSE,
  image VARCHAR(100),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
  FOREIGN KEY (chatroom_id) REFERENCES chatroom(room_id) ON DELETE SET NULL
);

-- 여행 계획 태그
CREATE TABLE TripPlanTags (
    plan_id INT,
    tag_id INT,
    PRIMARY KEY (plan_id, tag_id),
    FOREIGN KEY (plan_id) REFERENCES TripPlans(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags(id) ON DELETE CASCADE
);

-- 여행 계획의 일차
CREATE TABLE TripPlanDays (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT,
    day_number INT, -- 1일차, 2일차 등
    date DATE,
    FOREIGN KEY (plan_id) REFERENCES TripPlans(id) ON DELETE CASCADE
);

-- 여행 계획의 각 장소
CREATE TABLE TripPlanPlaces (
    id INT AUTO_INCREMENT PRIMARY KEY,
    day_id INT,
    attraction_id INT,
    place_order INT,
    memo TEXT,
    FOREIGN KEY (day_id) REFERENCES TripPlanDays(id) ON DELETE CASCADE,
    FOREIGN KEY (attraction_id ) REFERENCES Attractions(no) ON DELETE CASCADE
);
ALTER TABLE tripplans 
ADD COLUMN version INT DEFAULT 1,
ADD COLUMN last_editor_id INT,
ADD COLUMN editing_started_at DATETIME,
ADD COLUMN is_being_edited BOOLEAN DEFAULT FALSE,
ADD FOREIGN KEY (last_editor_id) REFERENCES users(id) ON DELETE SET NULL;

-- TripPlanDays 테이블에 버전 관리 추가
ALTER TABLE TripPlanDays 
ADD COLUMN version INT DEFAULT 1,
ADD COLUMN last_modified_by INT,
ADD COLUMN last_modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD FOREIGN KEY (last_modified_by) REFERENCES users(id) ON DELETE SET NULL;

-- TripPlanPlaces 테이블에 버전 관리 추가
ALTER TABLE TripPlanPlaces 
ADD COLUMN version INT DEFAULT 1,
ADD COLUMN last_modified_by INT,
ADD COLUMN last_modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD FOREIGN KEY (last_modified_by) REFERENCES users(id) ON DELETE SET NULL;
-- 게시판 (여행 계획 공유)
CREATE TABLE Posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT,
    chatroom_id INT NOT NULL,
    user_id INT,
    start_date DATE,
    end_date DATE,
    title VARCHAR(100),
    content TEXT,
    current_members INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    personalityTags TEXT,
    FOREIGN KEY (plan_id) REFERENCES TripPlans(id) ON DELETE CASCADE,
    FOREIGN KEY (chatroom_id) REFERENCES ChatRoom(room_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- 댓글
CREATE TABLE Comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT,  -- post_id를 plan_id로 변경
    user_id INT,
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES tripplans(id) ON DELETE CASCADE,  -- tripplans를 참조
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 동행자 목록
CREATE TABLE TripCompanions (
    plan_id INT,
    user_id INT,
    status ENUM('대기', '수락', '거절') DEFAULT '대기',
    PRIMARY KEY (plan_id, user_id),
    FOREIGN KEY (plan_id) REFERENCES TripPlans(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE TripPlanLikes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_like (plan_id, user_id),
    FOREIGN KEY (plan_id) REFERENCES TripPlans(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE chat_invite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chatroom_id INT NOT NULL,
    inviter_id INT NOT NULL,
    invitee_id INT NOT NULL,
    status VARCHAR(10) NOT NULL, -- 예: PENDING, ACCEPTED, REJECTED
    type ENUM('INVITE', 'REQUEST') NOT NULL, -- 🔹 초대인지 요청인지 구분
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chatroom_id) REFERENCES chatroom(room_id) ON DELETE CASCADE,
    FOREIGN KEY (inviter_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (invitee_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 변경 이력 추적 테이블
CREATE TABLE PlanChangeHistory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT NOT NULL,
    user_id INT NOT NULL,
    change_type ENUM('TITLE', 'DESCRIPTION', 'ITINERARY_ADD', 'ITINERARY_REMOVE', 'ITINERARY_UPDATE', 'ITINERARY_REORDER') NOT NULL,
    old_value TEXT,
    new_value TEXT,
    day_number INT,
    item_index INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES tripplans(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 편집 세션 관리 테이블 생성
CREATE TABLE PlanEditingSessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT NOT NULL,
    user_id INT NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_activity DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (plan_id) REFERENCES tripplans(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_session (plan_id, user_id)
);


INSERT INTO Tags (name, type) VALUES
('휴양', '여행'),
('모험', '여행'),
('문화체험', '여행'),
('맛집투어', '여행'),
('쇼핑', '여행'),
('힐링', '여행'),
('럭셔리', '여행'),
('알뜰여행', '여행'),
('자연', '여행'),
('도시', '여행'),
('해변', '여행'),
('산', '여행'),
('역사', '여행'),
('예술', '여행'),
('축제', '여행'),
('스포츠', '여행'),
('봄', '여행'),
('여름', '여행'),
('가을', '여행'),
('겨울', '여행'),
('사계절', '여행');

INSERT INTO Tags (name, type) VALUES
('외향적', '성격'),
('내향적', '성격'),
('계획적인', '성격'),
('즉흥적인', '성격'),
('느긋한', '성격'),
('활동적인', '성격'),
('꼼꼼한', '성격'),
('유쾌한', '성격'),
('조용한', '성격'),
('적응력 좋은', '성격'),
('사진 좋아함', '성격'),
('리더형', '성격'),
('서포터형', '성격'),
('아침형 인간', '성격'),
('저녁형 인간', '성격'),
('감성적인', '성격'),
('실용적인', '성격'),
('호기심 많은', '성격'),
('대화 좋아함', '성격'),
('긍정적인', '성격'),
('차분한', '성격');