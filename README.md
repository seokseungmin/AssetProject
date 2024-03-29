<div align="center">


### 사내 자산관리 프로젝트

<h2 align="center"> 소개 </h2>
사내 자산을 효율적으로 관리 가능하도록 설계한 웹 서비스 입니다.

사내 자산의 이동, 반납, 사용 등 관리자가 자산 정보를 빠르게 얻을 수 있으며,
변경된 자산 정보에 대한 추적도 굉장히 쉽게 가능합니다.

<h2 align="center"> 기능 </h2>

국제화 (한글/영어) 지원<br>
자산 등록 시 검증기능(필수입력 속성값 *)<br>

통합 자산 조회 페이지(자산정렬O, 검색기능 구현O, 페이징처리O)<br>
하드웨어 자산 조회 페이지(자산정렬O, 검색기능 구현O, 페이징처리O)<br>
소프트웨어 자산 조회 페이지(자산정렬O, 검색기능 구현O, 페이징처리O)<br>

하드웨어 자산 등록 페이지(검증기능 완료)<br>
소프트웨어 자산 등록 페이지(검증기능 완료)<br>
하드웨어 자산 수정 페이지(검증기능 완료)<br>
소프트웨어 자산 수정 페이지(검증기능 완료)<br>
하드웨어 자산 삭제 기능<br>
소프트웨어 자산 삭제 기능<br>
페이징처리<br>
자산검색<br>
자산정렬<br>

관리자 로그인(스프링 시큐리티 Session)<br>
관리자 로그아웃<br>
관리자 추가 페이지<br>
관리자 조회 페이지<br>
관리자 아이디 찾기<br>
History 로그성 데이터에 (등록, 수정, 삭제) 작업 기록<br>
작업이력 페이지(정렬O, 검색O, 페이징처리O)<br>
관리자 비밀번호 찾기(SMTP) BCryptPasswordEncoder사용<br>
임시비밀번호 이메일로 발송, 관리자 비밀번호 DB에 저장시에는 암호화된 상태로 저장. 
로그인할때는 암호화전 임시 비밀번호 입력하면 임시 비밀번호를 암호화해서 DB의 암호화된 값과 비교해서 로그인 처리.

<h2 align="center"> Tools / Languages </h2>

![Front-end](https://skillicons.dev/icons?i=idea,spring,gradle,java,mysql,html,css,javascript)<br>
<img src="https://img.shields.io/badge/Spring Web-59666C?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/SpringBoot-59666C?style=for-the-badge&logo=SpringBoot&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-59666C?style=for-the-badge&logo=Spring Security&logoColor=white"/><br>
<img src="https://img.shields.io/badge/Thymeleaf-59666C?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Mybatis-59666C?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Validation-59666C?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Devtools-59666C?style=for-the-badge&logo=Spring&logoColor=white"/><br>
<img src="https://img.shields.io/badge/Java-59666C?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/gradle-59666C?style=for-the-badge&logo=gradle&logoColor=white"/>
<img src="https://img.shields.io/badge/Lombok-59666C?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Mysql-59666C?style=for-the-badge&logo=MySql&logoColor=white"/>
<img alt="Html" src ="https://img.shields.io/badge/HTML5-59666C.svg?&style=for-the-badge&logo=HTML5&logoColor=white"/>
<img alt="Css" src ="https://img.shields.io/badge/CSS3-59666C.svg?&style=for-the-badge&logo=CSS3&logoColor=white"/> 
<img alt="JavaScript" src ="https://img.shields.io/badge/JavaScript-59666C.svg?&style=for-the-badge&logo=JavaScript&logoColor=white"/>
 
</div>

<h2 align="center"> Tables </h2>

```sql

USE AssetManage;

CREATE TABLE `asset` (
  `assetIdx` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `assetCode` VARCHAR(255) NULL,
  `assetName` VARCHAR(255) NULL,
  `assetType` ENUM('MONITOR', 'NOTEBOOK', 'PHONE', 'SOFTWARE') NULL,
  `assetStatus` ENUM('ASSIGNED', 'RETURNED', 'INACTIVE', 'ACTIVE') NULL,
  `sn` VARCHAR(255) NULL,
  `location` VARCHAR(255) NULL,
  `dept` VARCHAR(255) NULL,
  `purchaseDate` DATE NULL,
  `assignedDate` DATE NULL,
  `returnDate` DATE NULL,
  `currentUser` VARCHAR(255) NULL,
  `previousUser` VARCHAR(255) NULL,
  `manufacturer` VARCHAR(255) NULL
);

CREATE TABLE `hardware` (
  `hardwareIdx` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `assetIdx` BIGINT NULL,
  `cpu` VARCHAR(255) NULL,
  `ssd` VARCHAR(255) NULL,
  `hdd` VARCHAR(255) NULL,
  `memory` VARCHAR(255) NULL,
  `usageDuration` DATE NULL,
  `note` TEXT NULL,
  CONSTRAINT `hardware_ibfk_1` FOREIGN KEY (`assetIdx`) REFERENCES `asset` (`assetIdx`)
);

CREATE INDEX `idx_hardware_assetIdx` ON `hardware` (`assetIdx`);

CREATE TABLE `software` (
  `softwareIdx` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `assetIdx` BIGINT NULL,
  `expiryDate` DATE NULL,
  `note` TEXT NULL,
  CONSTRAINT `software_ibfk_1` FOREIGN KEY (`assetIdx`) REFERENCES `asset` (`assetIdx`)
);

CREATE INDEX `idx_software_assetIdx` ON `software` (`assetIdx`);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE software;
TRUNCATE TABLE hardware;
TRUNCATE TABLE asset;
SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE Admin (
   adminIdx INT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   role VARCHAR(50) NOT NULL
);

CREATE TABLE history
(
    historyIdx BIGINT AUTO_INCREMENT PRIMARY KEY,
    assetCode VARCHAR(255) NOT NULL,
    assetType ENUM('MONITOR', 'NOTEBOOK', 'PHONE', 'SOFTWARE') NOT NULL,
    action ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    changedBy VARCHAR(255) NOT NULL,
    changedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assetJSON JSON
);

```
