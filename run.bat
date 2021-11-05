REM heroku local web
REM start chrome https://localhost:5000
REM
start chrome https://ps007server.herokuapp.com/
REM new db name: issue-tracker-007-02
REM set transaction read write;
REM INSERT INTO Project VALUES (1,123,'foo',true)
REM http://localhost:5000/api/projects, POST, GET


REM https://ps007server.herokuapp.com/api/projects
REM {"client_id": "2222","title": "foobar","active": false}

REM http://localhost:5000/api/projects/3.0/issues/12
REM Post: http://localhost:5000/api/project/2222/issues
REM {"id": 2,"client_id": "2222","project_id": "2222","done": false,"title": "Bar","due_date":"2020-01-01","priority":"1"}

REM CREATE TABLE Project (
REM 	id double precision PRIMARY KEY,
REM 	client_id VARCHAR ( 255 ) UNIQUE NOT NULL,
REM 	title VARCHAR ( 255 ) NOT NULL,
REM 	active boolean NOT NULL
REM );
REM CREATE TABLE Issue (
REM 	id double precision PRIMARY KEY,
REM 	client_id VARCHAR ( 255 ) UNIQUE NOT NULL,
REM 	project_id double precision UNIQUE NOT NULL,
REM 	done boolean NOT NULL,
REM 	title VARCHAR ( 255 ) NOT NULL,
REM 	due_date date NOT NULL,
REM 	priority VARCHAR(5) NOT NULL
REM );
REM see _Project/schema.sql

REM http://keepitnative.herokuapp.com/
REM var HEROKU_API_URI = "http://zhaw-issue-tracker-api.herokuapp.com/api/", MIN_ID = 10800;
REM NEW: var HEROKU_API_URI = "https://ps007server.herokuapp.com/api/", MIN_ID = 10800;

