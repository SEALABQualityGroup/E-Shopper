---------------------------------------------------------------------------------
---------------  Tables  MICROSERVICE WISHLIST  ---------------------------------
---------------------------------------------------------------------------------
USE microservices;

CREATE TABLE `wish_list` (
  `ID_WISHLIST` int(11) NOT NULL AUTO_INCREMENT,
  `ID_ITEM` int(11) DEFAULT NULL,
  `ID_ACCOUNT` varchar(45) DEFAULT NULL,
  `INSERT_DATE` date DEFAULT NULL,
  `MODIFY_DATE` date DEFAULT NULL,
  PRIMARY KEY (`ID_WISHLIST`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
