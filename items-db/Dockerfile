FROM mariadb
COPY 01-create_schema.sql /docker-entrypoint-initdb.d/01-create_schema.sql
COPY 02-create_microservice_items.sql /docker-entrypoint-initdb.d/02-create_microservice_items.sql
CMD ["mysqld", "--lower-case-table-names=1"]
