CREATE TABLE Project (
	id double precision PRIMARY KEY,
	client_id VARCHAR ( 255 ) UNIQUE NOT NULL,
	title VARCHAR ( 255 ) NOT NULL,
	active boolean NOT NULL
);
CREATE TABLE Issue (
	id double precision PRIMARY KEY,
	client_id VARCHAR ( 255 ) UNIQUE NOT NULL,
	project_id double precision UNIQUE NOT NULL,
	done boolean NOT NULL,
	title VARCHAR ( 255 ) NOT NULL,
	due_date date NOT NULL,
	priority VARCHAR(5) NOT NULL
);