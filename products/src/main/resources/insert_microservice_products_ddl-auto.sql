---------------------------------------------------------------------------------
---------------  INSERT PRODUCTS  -----------------------------------------------
---------------------------------------------------------------------------------

LOCK TABLES `product` WRITE;
INSERT INTO `product` VALUES (1,3,'wallets','Wallets'),(2,3,'belts','Belts'),(3,3,'scarves','Scarves'),(4,3,'hats','Hats'),(5,3,'gloves','Gloves'),(6,1,'skinny','Skinny'),(7,1,'jeggins','jeggins'),(8,1,'shorts','shorts'),(9,2,'t-shirt','T-shirt'),(10,2,'top','Top'),(11,2,'cardigans','Cardigans'),(12,2,'sweatshirts','Sweatshirts'),(13,2,'dresses','Dresses'),(14,2,'trousers','Trousers'),(15,5,'minibags','Minibags'),(16,5,'shoppers','Shoppers'),(17,5,'travel bags','Travel bags'),(18,6,'flats','Flats'),(19,6,'sneakes','Sneakes');
UNLOCK TABLES;