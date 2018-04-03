---------------------------------------------------------------------------------
---------------  Tables  MICROSERVICE CART  -------------------------------------
---------------------------------------------------------------------------------
USE microservices;

CREATE TABLE `cart` (
  `ID_CART` int(11) NOT NULL AUTO_INCREMENT,
  `ID_ITEM` int(11) DEFAULT NULL,
  `ID_ACCOUNT` varchar(45) DEFAULT NULL,
  `PRICE` decimal(10,2) DEFAULT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `INSERT_DATE` date DEFAULT NULL,
  `MODIFY_DATE` date DEFAULT NULL,
  `FLAG_ORDER` varchar(1) DEFAULT 'N',
  PRIMARY KEY (`ID_CART`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
