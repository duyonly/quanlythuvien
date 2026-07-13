-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: qlthuvien
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','LIBRARIAN','MEMBER') DEFAULT 'MEMBER',
  `status` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_login` datetime DEFAULT NULL,
  `failed_attempts` int DEFAULT '0',
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'admin_thanh','********','ADMIN',1,'2026-06-17 20:50:07',NULL,0,1),(2,'librarian_hoa','password123','LIBRARIAN',1,'2026-06-17 20:50:07',NULL,0,2),(3,'librarian_dung','password123','LIBRARIAN',1,'2026-06-17 20:50:07',NULL,0,3),(4,'member_nam','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,4),(5,'member_linh','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,5),(6,'member_quan','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,6),(7,'member_huong','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,7),(8,'member_tuan','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,8),(9,'member_vy','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,9),(10,'member_phong','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,10),(11,'member_mai','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,11),(12,'member_long','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,12),(13,'member_yen','password123','MEMBER',0,'2026-06-17 20:50:07',NULL,0,13),(14,'member_duc','password123','MEMBER',1,'2026-06-17 20:50:07',NULL,0,14),(15,'member_trang','********','MEMBER',1,'2026-06-17 20:50:07',NULL,0,15),(16,'duytran','duy171','MEMBER',0,'2026-06-17 20:50:07',NULL,0,18),(17,'binh','12345','MEMBER',1,'2026-06-17 20:50:07',NULL,0,19),(18,'tran trung','123','MEMBER',1,'2026-06-23 16:02:38',NULL,0,20),(19,'tran trung phu','126','MEMBER',1,'2026-06-23 21:42:20',NULL,0,21),(20,'nguyenvana','123456','MEMBER',1,'2026-06-24 11:31:32',NULL,0,22),(21,'minh','123','MEMBER',1,'2026-06-24 11:35:14',NULL,0,23),(22,'gold','123','MEMBER',1,'2026-06-25 11:53:24',NULL,0,24);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `author` varchar(150) NOT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `total_quantity` int DEFAULT '0',
  `available_quantity` int DEFAULT '0',
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `isbn` (`isbn`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'Clean Code','Robert C. Martin','9780132350884',14,16,1),(2,'Design Patterns','Erich Gamma','9780201633610',5,5,1),(3,'Nhà Giả Kim','Paulo Coelho','9780062315007',20,29,3),(4,'Đắc Nhân Tâm (Bản Đặc Biệt)','Dale Carnegie','9781439167342',18,18,4),(5,'Cha Giàu Cha Nghèo','Robert Kiyosaki','9781612680194',12,12,2),(6,'Lược Sử Thời Gian','Stephen Hawking','9780553380163',8,8,5),(7,'Sapiens: Lược Sử Loài Người','Yuval Noah Harari','9780062316097',14,21,6),(8,'Học Tiếng Anh Thần Tốc','Raymond Murphy','9781107614994',25,25,9),(9,'Tư Duy Nhanh Và Chậm','Daniel Kahneman','9780374275631',7,7,4),(10,'Bắt Trẻ Đồng Xanh','J.D. Salinger','9780316769174',6,6,3),(11,'Kinh Tế Học Vi Mô','N. Gregory Mankiw','9781305505537',10,10,2),(12,'Java Core & Advanced','Herbert Schildt','9781260440232',15,4,1),(13,'Nghệ Thuật Đuổi Hình Bắt Chữ','Don Norman','9780465050659',5,1,7),(14,'Luật Dân Sự Việt Nam','Nhiều tác giả','9786049561234',4,4,11),(15,'Món Ăn Ngon Ngày Tết','Nguyễn Thị Phụng','9786049225432',3,2,13),(40,'truyện kiều','nguyễn du','9786042254960',24,24,3);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_details`
--

DROP TABLE IF EXISTS `borrow_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ticket_id` int DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `fine_amount` double DEFAULT '0',
  `note` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `ticket_id` (`ticket_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `borrow_details_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `borrow_tickets` (`id`),
  CONSTRAINT `borrow_details_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_details`
--

LOCK TABLES `borrow_details` WRITE;
/*!40000 ALTER TABLE `borrow_details` DISABLE KEYS */;
INSERT INTO `borrow_details` VALUES (1,1,1,'2026-05-14',0,'Sách sạch sẽ',1),(2,1,3,'2026-05-14',0,'Bình thường',1),(3,2,4,'2026-05-16',0,'Tốt',1),(4,3,2,NULL,0,NULL,1),(5,4,5,NULL,0,NULL,1),(6,5,7,NULL,0,NULL,1),(7,6,12,NULL,0,NULL,1),(8,7,6,'2026-04-28',0,'Bình thường',1),(9,8,8,'2026-05-25',30000,'Trả muộn 6 ngày',1),(10,9,10,NULL,0,NULL,1),(11,10,11,NULL,0,NULL,1),(12,11,1,NULL,0,NULL,1),(13,12,12,NULL,0,NULL,1),(14,13,4,'2026-04-12',0,'Bình thường',1),(15,14,9,'2026-04-24',0,'Bình thường',1),(16,17,12,NULL,0,NULL,1),(17,18,5,NULL,0,NULL,1),(18,19,12,'2026-06-09',0,'bình thương',1),(19,20,13,'2026-06-09',0,'bình thương',1),(20,21,7,NULL,0,NULL,1),(21,22,12,NULL,0,NULL,1),(22,23,5,'2026-06-12',0,'bình thương',1),(23,24,7,'2026-06-10',0,'bình thương',7),(24,25,1,'2026-06-11',0,'bình thương',5),(25,26,1,'2026-06-11',0,'bình thương',3),(26,27,1,NULL,0,NULL,3),(27,28,1,'2026-06-12',0,NULL,5),(28,29,1,NULL,0,NULL,1),(29,30,15,NULL,0,NULL,1),(30,31,2,'2026-06-23',0,'bình thương',13);
/*!40000 ALTER TABLE `borrow_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_tickets`
--

DROP TABLE IF EXISTS `borrow_tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_tickets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `borrow_date` date NOT NULL,
  `due_date` date NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `borrow_tickets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_tickets`
--

LOCK TABLES `borrow_tickets` WRITE;
/*!40000 ALTER TABLE `borrow_tickets` DISABLE KEYS */;
INSERT INTO `borrow_tickets` VALUES (1,4,'2026-05-01','2026-05-15','RETURNED'),(2,5,'2026-05-02','2026-05-16','RETURNED'),(3,6,'2026-05-10','2026-05-24','OVERDUE'),(4,7,'2026-05-15','2026-05-29','BORROWING'),(5,8,'2026-05-20','2026-06-03','BORROWING'),(6,9,'2026-05-22','2026-06-05','BORROWING'),(7,10,'2026-04-15','2026-04-29','RETURNED'),(8,11,'2026-05-05','2026-05-19','RETURNED'),(9,12,'2026-05-08','2026-05-22','OVERDUE'),(10,14,'2026-05-25','2026-06-08','BORROWING'),(11,15,'2026-05-26','2026-06-09','BORROWING'),(12,4,'2026-05-27','2026-06-10','BORROWING'),(13,5,'2026-05-27','2026-06-10','BORROWING'),(14,6,'2026-04-01','2026-04-15','RETURNED'),(15,7,'2026-04-10','2026-04-24','RETURNED'),(17,18,'2026-06-09','2026-06-10','BORROWING'),(18,18,'2026-06-09','2026-06-27','BORROWING'),(19,19,'2026-06-09','2026-06-23','RETURNED'),(20,18,'2026-06-09','2026-06-23','RETURNED'),(21,10,'2026-06-09','2026-06-23','RETURNED'),(22,10,'2026-06-10','2026-06-23','BORROWING'),(23,18,'2026-06-10','2026-06-23','RETURNED'),(24,18,'2026-06-10','2026-06-23','BORROWING'),(25,18,'2026-06-11','2026-06-25','RETURNED'),(26,18,'2026-06-11','2026-06-25','BORROWING'),(27,18,'2026-06-11','2026-06-25','BORROWING'),(28,18,'2026-06-12','2026-06-26','RETURNED'),(29,18,'2026-06-19','2026-07-03','BORROWING'),(30,18,'2026-06-19','2026-07-03','BORROWING'),(31,19,'2026-06-23','2026-07-07','RETURNED');
/*!40000 ALTER TABLE `borrow_tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Công nghệ thông tin','Sách về lập trình, mạng máy tính, AI, cơ sở dữ liệu'),(2,'Kinh tế & Kinh doanh','Sách về quản trị, khởi nghiệp, tài chính, marketing'),(3,'Văn học & Tiểu thuyết','Truyện ngắn, tiểu thuyết trong và ngoài nước'),(4,'Tâm lý học','Sách nghiên cứu hành vi, tâm lý con người, phát triển bản thân'),(5,'Khoa học & Vũ trụ','Khám phá tự nhiên, vật lý, thiên văn học'),(6,'Lịch sử','Lịch sử Việt Nam và thế giới qua các thời kỳ'),(7,'Nghệ thuật & Thiết kế','Sách về hội họa, kiến trúc, đồ họa, âm nhạc'),(8,'Y học & Sức khỏe','Chăm sóc sức khỏe, y học thường thức, dinh dưỡng'),(9,'Ngoại ngữ','Giáo trình, tài liệu học tiếng Anh, tiếng Nhật, tiếng Trung'),(10,'Kỹ năng sống','Sách hướng dẫn kỹ năng giao tiếp, quản lý thời gian'),(11,'Chính trị & Pháp luật','Văn bản luật, chính trị học thường thức'),(12,'Triết học','Triết học phương Đông, phương Tây và các tư tưởng lớn'),(13,'Ẩm thực & Nấu ăn','Hướng dẫn làm bánh, nấu các món ăn vùng miền'),(14,'Du lịch & Khám phá','Sách hướng dẫn du lịch, ký sự đường xa'),(15,'Nuôi dạy con','Phương pháp giáo dục trẻ nhỏ, tâm lý trẻ em');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Nguyễn Văn Thanh','thanh.admin@gmail.com'),(2,'Lê Thị Hoa','hoa.lib@gmail.com'),(3,'Trần Tiến Dũng','dung.lib@gmail.com'),(4,'Hoàng Giang Nam','nam.hg@gmail.com'),(5,'Khánh Linh','linh.k@gmail.com'),(6,'Phạm Minh Quân','quan.pm@gmail.com'),(7,'Đỗ Mai Hương','huong.dm@gmail.com'),(8,'Bùi Anh Tuấn','tuan.ba@gmail.com'),(9,'Nguyễn Thảo Vy','vy.nt@gmail.com'),(10,'Vũ Hải Phong','phong.vh@gmail.com'),(11,'Ngô Thu Mai','mai.nt@gmail.com'),(12,'Đặng Hoàng Long','long.dh@gmail.com'),(13,'Hà Hải Yến','yen.hh@gmail.com'),(14,'Phan Minh Đức','duc.pm@gmail.com'),(15,'Lê Thu Trang','trang.lt@gmail.com'),(18,'trần hữu duy','duy123@gmail.com'),(19,'lê văn bình','binh2002@gmail.com'),(20,'Trần Minh Trung','trung12@gmail.com'),(21,'Trần Minh phu','phu12@gmail.com'),(22,'Nguyen Van A','a@gmail.com'),(23,'duy minh','minh22@gmail.com'),(24,'golden','gold123@gmail.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-14  4:58:48
