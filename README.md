# ZHAW web3 - non riotjs & nodejs solution - JAVA REST Backend

## Swagger OpenApi Docu

 when run locally:

 http://localhost:5000/swagger-ui.html#/main

 http://localhost:5000/swagger-ui.html#/super-trumpf

 or on stage server installation:

 https://ps007server.herokuapp.com/swagger-ui.html
 
## Init your local app with heroku

 install heroku binary first, as described step-by-step in the heroku "Getting started" manuals!

 Having free heroku account do:

`heroku login`

 `heroku create` creates the app with a new app with new name (see the rename app section).

## DB
 set up postgres db on heroku follow the steps in this tutorial:
 
 https://dev.to/danielmabadeje/how-to-create-tables-on-heroku-postgresql-1n42
 
 You will need the DB TABLE SCHEMA - check `./_Project/schema.sql`
 
 To configure DB, SQL Console to add tables or use Dataclips on herokuapp.com gui instead:

 `heroku pg:psql {db_name}` (sql command prompt on heroku instance, activate postgres first)

 Optional Rename DB (.env on heroku is automatically linked per app)

 `heroku addons:rename {old_name} {new_name}`

## Rename the app instance

  Change to better appname/{your-app-id}:

 `heroku app:rename newname`

## Postgres GUI on heroku

 To get the GUI on heroku with the postgres instance (Use Dataclips etc)
 
 https://dashboard.heroku.com/apps/{your-app-id}/resources

## CLI ON REMOTE DB TO CREATE TABLES
 `heroku login`

 `heroku pg:psql` (sql command prompt, needed to create the db tables)

## Logging
 `heroku login`

 `heroku logs -n 500` (display last 500 lines)

## DEPLOY & START APP
 edit .env only uses to run postgres locally (needs local postgres installation):

 .env needs these three content items (1.-3.)
 
 1. connection-string:

 `JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/java_database_name`

 local credentials: 

 2. `username=...{local postgres username e.g. postgres}`

 3. `password=...{local postgres password}`

## Build app (need built .jar package)

`heroku login`

`heroku create`

`mvn clean install`

## Deploy app to github.com and to heroku


 `git add .`

 `git commit -m "first commit"`

 `git push`

 `git push heroku HEAD:master`

# Start app locally (needs postgres linked in .env file)

`heroku local web`

 heroku open local app:

`heroku open`

 or

`start chrome https://localhost:5000`


## Actions on REST Server (test with e.g. postman rest client)

 `{server_uri}` local = `http://localhost:5000`

 Show all Projects

 Get Request on:

 `{server_uri}/api/projects`

 POST a project `{server_uri}/api/projects` 
 
 JSON Payload:

 `{"client_id": "2222","title": "foobar","active": false}`

 Post an issue: `{server_uri}/api/project/<project_id>/issues`
 
 JSON Payload:
 
`{"id": 2,"client_id": "2222","project_id": "2222","done": false,"title": "Bar","due_date":"2020-01-01","priority":"1"}`

# Original README from heroku java getting started (copyright by heroku) following

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