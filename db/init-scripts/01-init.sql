-- Create the database if it doesn't exist
CREATE DATABASE flighttracker;

-- Connect to the database
\c flighttracker;

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS flighttracker;

-- Set the search path
SET search_path TO flighttracker;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE flighttracker TO flighttracker;
GRANT ALL PRIVILEGES ON SCHEMA flighttracker TO flighttracker;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA flighttracker TO flighttracker;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA flighttracker TO flighttracker;

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA flighttracker GRANT ALL ON TABLES TO flighttracker;
ALTER DEFAULT PRIVILEGES IN SCHEMA flighttracker GRANT ALL ON SEQUENCES TO flighttracker; 