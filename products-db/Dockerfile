FROM mariadb
COPY 01-create_schema.sql /docker-entrypoint-initdb.d/01-create_schema.sql
COPY 02-create_microservice_products.sql /docker-entrypoint-initdb.d/02-create_microservice_products.sql
CMD ["mysqld", "--lower-case-table-names=1"]
