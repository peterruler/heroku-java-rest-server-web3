
REM heroku cmds
heroku login
heroku create
mvn clean install
git add .
git commit -m "first commit"
git push
git push heroku HEAD:master

heroku local web
heroku open