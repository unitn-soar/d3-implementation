# Soar

Soar is a command-line flight booking app built in Java.

It is meant to show the main pieces of a simple airline ticket system: users can register, log in, search for flights, buy a ticket, view their purchase history, check in, and manage their account. The project also includes the PostgreSQL tables and functions used by the app, plus diagrams and a LaTeX report.

## What the app covers

- Account registration and login
- Flight search by destination and date
- Ticket purchase for a selected flight
- Purchase history
- Self check-in
- Email, password, and account management
- Sample airlines, airports, flights, seats, and users for testing

## Requirements

- Java 26 or a compatible JDK
- Maven
- PostgreSQL

The database connection is currently set in `src/main/java/d3/soar/db/Database.java`:

```text
jdbc:postgresql://localhost:5432/soar
user: postgres
password: empty
```

This setup is only for local development. If your PostgreSQL user has a password, update the connection values before running the app.

## Database setup

Create a local database named `soar`, then run the SQL files in this order:

```bash
createdb soar
psql -U postgres -d soar -f sql/tables.sql
psql -U postgres -d soar -f sql/functions.sql
```

Use a fresh database when possible. The app inserts test data when it starts, so running it many times against the same database may create repeated data or hit existing records.

## Running the project

From the project folder, install the Maven dependencies and compile the code:

```bash
mvn compile
```

Then run `d3.soar.main.Main` from your Java IDE or from your usual Java runner.

The project targets Java 26. Older JDKs may not accept the current `main` method style.

When the app starts, it prints:

```text
Welcome to Soar! Your flight app
Type 'help' to get the command list
```

Type `HELP` inside the program to see the available commands.

## Commands

The app understands these commands:

```text
REGISTER
LOGIN
LOGOUT
SEARCH_FLIGHT
PURCHASE_HISTORY
SELF_CHECK_IN
EDIT_ACCOUNT
CHANGE_PASSWORD
DELETE_ACCOUNT
HELP
EXIT
```

Commands are not case-sensitive, so `help`, `HELP`, and `Help` are treated the same way.

## Project structure

```text
src/main/java/d3/soar
  FlightData      flight, airport, airline, route, and plane models
  PurchaseData    ticket, passenger, purchase, and purchase log models
  UserData        user types such as person, agency, admin, and developer
  MainLists       collection-style classes for the main data groups
  db              database connection and database calls
  main            command loop and command list

sql
  tables.sql      database tables, indexes, and triggers
  functions.sql   stored functions used by the Java app

diagrams
  user flows, database diagrams, and class diagram

main.tex
  project report source
```

## Notes

Soar is a learning project, not a production booking system. The code keeps the flow simple so the main ideas are easy to follow: the Java side handles user input, while PostgreSQL stores the data and runs most of the business operations.
