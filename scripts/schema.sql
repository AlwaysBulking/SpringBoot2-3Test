CREATE SCHEMA IF NOT EXISTS analysis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS analysis.user_data (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
    user_email VARCHAR (150) UNIQUE NOT NULL,
    password VARCHAR (250) NOT NULL,
    user_name VARCHAR (250) NOT NULL,
    user_surname VARCHAR (250) NOT NULL
    );



