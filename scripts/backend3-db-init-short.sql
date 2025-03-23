-- Grant privileges on the backend3 database
GRANT ALL PRIVILEGES ON DATABASE backend3 TO db3_user;

-- Connect to backend3 database
\c backend3;

-- Ensure the user can create tables, sequences, and functions
GRANT USAGE, CREATE ON SCHEMA public TO db3_user;

-- Grant default privileges on future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO db3_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO db3_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO db3_user;