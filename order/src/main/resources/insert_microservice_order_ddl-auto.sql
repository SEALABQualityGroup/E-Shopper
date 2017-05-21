---------------------------------------------------------------------------------
---------------  INSERT STATUS ORDERS  ------------------------------------------
---------------------------------------------------------------------------------

LOCK TABLES `status_order` WRITE;
INSERT INTO `status_order` VALUES (1,'OPEN'),(2,'CLOSED'),(3,'CANCELLED'),(4,'ARCHIVED'),(5,'SEND');
UNLOCK TABLES;