# ZHAW web3 course - web-client frontend - JAVA REST Backend

## DB
- set up postgres db on heroku:
- https://dev.to/danielmabadeje/how-to-create-tables-on-heroku-postgresql-1n42
- DB TABLE SCHEMA check <this root dir>/_Project/schema.sql
- to configure DB, SQL Console to add tables or use Dataclips on herokuapp.com gui instead:
- heroku pg:psql postgresql-round-48057
- Optional Rename DB (.env on heroku is automatically linked per app)
- heroku addons:rename postgresql-round-48057 issue-tracker-007-02
- https://dashboard.heroku.com/apps/<your-app-id>/resources
- Better appname:
- heroku app:rename newname

## CLI ON REMOTE DB TO CREATE TABLES
- heroku login
- heroku pg:psql

## Logging
- heroku login
- heroku logs -n 500

## DEPLOY & START APP
- rename env.txt into .env with local postgres dbname, username & password
- heroku login
- heroku create
- mvn clean install
- git add .
- git commit -m "first commit"
- git push
- git push heroku HEAD:master

- Start app locally
- heroku local web
- heroku open local app:
- heroku open
- or
- start chrome https://localhost:5000

## Actions on REST Server
- Show all Projects
- http://localhost:5000/api/projects
- POST a project <>/api/projects JSON Payload:
- {"client_id": "2222","title": "foobar","active": false}
- Post an issue: http://localhost:5000/api/project/<project_id>/issues, JSON Payload:
- {"id": 2,"client_id": "2222","project_id": "2222","done": false,"title": "Bar","due_date":"2020-01-01","priority":"1"}

# Original README from heroku java getting started (c) following

# java-getting-started

A barebones Java app, which can easily be deployed to Heroku.

This application supports the [Getting Started with Java on Heroku](https://devcenter.heroku.com/articles/getting-started-with-java) article - check it out.

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Running Locally

Make sure you have Java and Maven installed.  Also, install the [Heroku CLI](https://cli.heroku.com/).

```sh
$ git clone https://github.com/heroku/java-getting-started.git
$ cd java-getting-started
$ mvn install
$ heroku local:start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

If you're going to use a database, ensure you have a local `.env` file that reads something like this:

```
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/java_database_name
```

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku main
$ heroku open
```

## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)