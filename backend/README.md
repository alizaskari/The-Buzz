Backend README

Team 21's (Team git gud) backend

How to connect to the backend
Switch into the backend branch and go into the backend folder. 
Then type DATABASE_URI="jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres?user=postgres.fjjpcdheojefynplupsc&password=a86NZKEldOhXlLFt" mvn exec:java. 

After entering in this command the server should be able to start. Connect to the http://localhost8080/messages to be able to see the messages with the id and likes.

Doku Application
To connect to the dokku switch to deploy dokku branch.
Then type in the command ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:start team-git-gud' to turn on the app. 

To look at the logs of the dokku app type ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'logs team-git-gud --tail'.

Lastly to turn off the app (which should be done after every use to prevent problems) type in the command ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:stop team-git-gud'.

Link to html: cse216_fa24_team_21/backend/target/reports/apidocs/edu/lehigh/cse216/smd226/backend/package-summary.html




List of backlog:
-Limited tests
-A lot of extra code in App.java (main hello world method which is not being used including routes that where commented out)
-Date is imported in mockdata row but it is not necessary



