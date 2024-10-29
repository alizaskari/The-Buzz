## Html output:
- file:///c%3A/Users/bdbad/216/cse216_fa24_team_21/admin-cli/target/reports/apidocs/edu/lehigh/cse216/team21/admin/package-summary.html

## How to run
- when mvn package and mvn exec:java put the enviorment varibles for the database before them on the command line
- POSTGRES_IP="aws-0-us-east-1.pooler.supabase.com" POSTGRES_PORT=6543 POSTGRES_USER="postgres.fjjpcdheojefynplupsc" POSTGRES_PASS="a86NZKEldOhXlLFt" POSTGRES_DBNAME="postgres" mvn package
- POSTGRES_IP="aws-0-us-east-1.pooler.supabase.com" POSTGRES_PORT=6543 POSTGRES_USER="postgres.fjjpcdheojefynplupsc" POSTGRES_PASS="a86NZKEldOhXlLFt" POSTGRES_DBNAME="postgres" mvn exec:java

- When ran you will see letters for commands of Admin app
    - T/t for Create Table
    - D/d for Drop Tables
    - F/f for Fill table
    - 1 for select row in table
    - '-' for delete row in table
    - Q/q for quit

## Tests
- For my tests all i did was drop create table then add a row and check if contents of row are correct from what was inputted and then deleted row making sure it was deleted

## Database
- It can create table, drop table, select row from table, and delete row from table. Also add row that was used for testing. 
- It does this with prepared statements of sql.

## App 
- Uses database to give user a place to see a menu of things they can do and do them without any need to type many things in