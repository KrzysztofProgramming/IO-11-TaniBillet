#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE keycloak_db;
    CREATE USER keycloak_user WITH ENCRYPTED PASSWORD 'keycloakpassword';
    GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak_user;

    CREATE DATABASE app_db;
    CREATE USER app_user WITH ENCRYPTED PASSWORD 'apppassword';
    GRANT ALL PRIVILEGES ON DATABASE app_db TO app_user;

    \c keycloak_db
    ALTER SCHEMA public OWNER TO keycloak_user;
    GRANT ALL ON SCHEMA public TO keycloak_user;

    \c app_db
    ALTER SCHEMA public OWNER TO app_user;
    GRANT ALL ON SCHEMA public TO app_user;
EOSQL