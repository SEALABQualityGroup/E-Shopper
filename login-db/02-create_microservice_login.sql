---------------------------------------------------------------------------------
---------------  Tables  MICROSERVICE LOGIN  ------------------------------------
---------------------------------------------------------------------------------
USE microservices;

CREATE TABLE `login` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_ACCOUNT` varchar(50) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `INSERT_DATE` date DEFAULT NULL,
  `MODIFY_DATE` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
