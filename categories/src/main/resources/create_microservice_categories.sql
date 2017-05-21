---------------------------------------------------------------------------------
---------------  Tables  MICROSERVICE CATEGORIES  -------------------------------
---------------------------------------------------------------------------------

CREATE TABLE `category` (
  `CATEGORY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`CATEGORY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

LOCK TABLES `category` WRITE;
INSERT INTO `category` VALUES (1,'Denim','denim'),(2,'Apparel','apparel'),(3,'Accessories','accessories'),(4,'Sunglasses','sunglasses'),(5,'Bags','bags'),(6,'Shoes','shoes'),(7,'Sportswear','sportswear');
UNLOCK TABLES;