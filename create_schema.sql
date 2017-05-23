CREATE SCHEMA `microservices` DEFAULT CHARACTER SET utf8 ;
CREATE USER 'microservices'@'localhost' IDENTIFIED BY 'microservices';
GRANT ALL PRIVILEGES ON *.microservices TO 'microservices'@'localhost';