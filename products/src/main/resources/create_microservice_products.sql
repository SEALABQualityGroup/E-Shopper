---------------------------------------------------------------------------------
---------------  Tables  MICROSERVICE PRODUCTS  ---------------------------------
---------------------------------------------------------------------------------

CREATE TABLE `product` (
  `PRODUCT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CATEGORY_ID` int(11) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`PRODUCT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

LOCK TABLES `product` WRITE;
INSERT INTO `product` VALUES (1,3,'Wallets','wallets'),(2,3,'Belts','belts'),(3,3,'Scarves','scarves'),(4,3,'Hats','hats'),(5,3,'Gloves','gloves'),(6,1,'Skinny','skinny'),(7,1,'jeggins','jeggins'),(8,1,'shorts','shorts'),(9,2,'T-shirt','t-shirt'),(10,2,'Top','top'),(11,2,'Cardigans','cardigans'),(12,2,'Sweatshirts','sweatshirts'),(13,2,'Dresses','dresses'),(14,2,'Trousers','trousers'),(15,5,'Minibags','minibags'),(16,5,'Shoppers','shoppers'),(17,5,'Travel bags','travel bags'),(18,6,'Flats','flats'),(19,6,'Sneakes','sneakes');
UNLOCK TABLES;