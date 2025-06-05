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
-- Table structure for table `tarefas`
--

DROP TABLE IF EXISTS `tarefas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarefas` (
  `id_tarefa` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descricao` text,
  `data_limite` date DEFAULT NULL,
  `concluida` tinyint(1) DEFAULT '0',
  `id_equipe` int NOT NULL,
  `data_conclusao` datetime DEFAULT NULL,
  PRIMARY KEY (`id_tarefa`),
  KEY `fk_equipe` (`id_equipe`),
  CONSTRAINT `fk_equipe` FOREIGN KEY (`id_equipe`) REFERENCES `equipes` (`id_equipe`) ON DELETE CASCADE,
  CONSTRAINT `tarefas_ibfk_1` FOREIGN KEY (`id_equipe`) REFERENCES `equipes` (`id_equipe`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarefas`
--

LOCK TABLES `tarefas` WRITE;
/*!40000 ALTER TABLE `tarefas` DISABLE KEYS */;
INSERT INTO `tarefas` VALUES (1,'fazer uma landiong page','crie uma landing page usando as cores azul e preto contendo um cabecalho','2025-06-20',0,1,NULL),(2,'Fazer o botao do site','o botao tem que ser vermelho com 10 kilometros de altura','2025-06-05',1,1,'2025-06-02 13:34:37'),(7,'Vender 10 cursos','cada um que estiver responsavel por esta tarefa tera que vender no minimo 10 curss p dia','2025-06-30',1,9,'2025-06-03 10:38:47'),(8,'Front-End (login)','Criar a interface da tela de login da aplicação usando a paleta de cores tal e um estilo minimalista.','2025-08-13',0,10,NULL),(9,'Back-end (api)','Criar a api que vai se comunicar com o frontend','2025-08-28',1,10,'2025-06-04 12:01:45');
/*!40000 ALTER TABLE `tarefas` ENABLE KEYS */;
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
