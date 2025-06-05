-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: database_a3
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `senha_hash` varchar(64) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `id_tipo` int DEFAULT NULL,
  `consentimento_lgpd` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`),
  KEY `fk_tipo_usuario` (`id_tipo`),
  CONSTRAINT `fk_tipo_usuario` FOREIGN KEY (`id_tipo`) REFERENCES `tipo_usuario` (`id_tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'gui@email.com','ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f','Gui',2,1),(2,'gui2@email.com','0f3153d24871e120bb3870e0dce2dd9d25e5aadf0e92084daaa64000fa899a1a','Guilherme C T dos Santos',2,1),(4,'guilherme@email.com','996a20d854b0b1ee65fdb04a2ef4f4405201895a232c73d385990ef09e433ceb','Guilherme Santos',1,1),(5,'nicolas@email.com','87000e071ec6da2ebe9d605b68f7ab1665268eb5f4591d3d6e7ad1add00f453d','Nicolas',2,1),(6,'guicorrea600@gmail.com','93f82e739e7418e6107da4051fb5dd8223707e3887aa27dfd32c8855e80d77f7','Guilherme Correa',2,1),(7,'eduardodonascimentocarlos27@gmail.com','7e972dc364f9e86b2a0e27e900ad09dd4fc248846a13cf0dc8e6ba3bb0298039','Carlos Eduardo do Nascimento',2,1),(9,'bruno@email.com','93f82e739e7418e6107da4051fb5dd8223707e3887aa27dfd32c8855e80d77f7','Bruninho dos santos',1,1),(11,'funcionario@email.com','93f82e739e7418e6107da4051fb5dd8223707e3887aa27dfd32c8855e80d77f7','Funcionario Novo',1,1),(12,'guicorreadev@gmail.com','0f3153d24871e120bb3870e0dce2dd9d25e5aadf0e92084daaa64000fa899a1a','Guilherme Correa Teixeira dos Santos',1,1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-05 12:28:10
