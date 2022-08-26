drop database if exists Event;
create database Event default character set utf8;
-- SET GLOBAL time_zone = "Asia/Thailand";
SET GLOBAL time_zone = system;
SET time_zone = "+07:00";
SET @@session.time_zone = "+07:00";
use Event;

DROP TABLE IF EXISTS event.eventcategory ;
create table eventcategory(
eventCategoryId int not null auto_increment primary key, 
eventCategoryName varchar(100) not null, 
eventCategoryDescription varchar(500),
duration int not null);

DROP TABLE IF EXISTS event.users ;
create table users(
userId int not null auto_increment primary key, 
username varchar(100) not null unique, 
email varchar(50) not null unique, 
-- userpassword varchar(90) not null, 
roles ENUM('admin', 'lecturer', 'student') not null default 'student',
createdOn datetime not null default CURRENT_TIMESTAMP(),  
updatedOn datetime not null default CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP);

DROP TABLE IF EXISTS event.event ;
create table event(
eventId int not null auto_increment primary key, 
bookingName varchar(100) not null, 
bookingEmail varchar(200) not null, 
category varchar(100) not null,
startTime datetime not null,
duration int not null,
eventNotes varchar(500),
eventCategoryId int,
-- userId int,
FOREIGN KEY (eventCategoryId) REFERENCES eventcategory(eventCategoryId));
-- FOREIGN KEY (userId) REFERENCES users(userId));

insert into eventcategory
values(1,'Project Magement Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 intergrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา',30);
insert into eventcategory
values(2,'DevOps/Infra Clinic','Use this event category for DevOps/Infra clinic',20);
insert into eventcategory
values(3,'Database Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I',15);
insert into eventcategory
values(4,'Client-side Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I',30);
insert into eventcategory
values(5,'Server-side Clinic',null,30);

-- insert into users
-- values(1,'Somchai Jaidee (OR-7)','somchai.jai@mail.kmutt.ac.th','student',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
-- insert into users
-- values(2,'Somsri Rakdee (SJ-3)','somsri.rak@mail.kmutt.ac.th','student',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
-- insert into users
-- values(3,'สมเกียรติ ขยันเรียน กลุ่มTT-4','somkiat.kay@kmutt.ac.th','student',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
-- insert into users
-- values(4,'นาวิน บินทะลุบ้าน กลุ่มTT-2','nawin binturuban.nong@kmutt.ac.th','student',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

insert into event
values(1,'Somchai Jaidee (OR-7)','somchai.jai@mail.kmutt.ac.th','DevOps/Infra Clinic','2022-04-23 13:30:00',30,null,2);
insert into event
values(2,'Somsri Rakdee (SJ-3)','somsri.rak@mail.kmutt.ac.th','Project Magement Clinic','2022-05-27 09:30:00',30,"ขอปรึกษาปัญหาเพื่อนไม่ช่วยงาน",1);
insert into event
values(3,'สมเกียรติ ขยันเรียน กลุ่มTT-4','somkiat.kay@kmutt.ac.th','Database Clinic','2022-04-23 16:30:00',15,null,3);
insert into event
values(4,'นาวิน บินทะลุบ้าน กลุ่มTT-2','nawin binturuban.nong@kmutt.ac.th','Client-side Clinic','2022-04-23 16:30:00',15,'นาวินเองจร่าา',4);
commit;