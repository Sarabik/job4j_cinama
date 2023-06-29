# job4j_cinema
job4j_cinema is a web application with an emphasis on the server side of the site for purchasing cinema tickets.

**The main logic of the App:**
* registration, sign in, sign out
* displaying all film sessions
* displaying the list of sessions for one chosen film
* displaying all films
* displaying full information about one chosen film
* purchasing tickets

Any user can get acquainted with film sessions, as well as with detailed information about all the films that are shown. Purchasing tickets is available only to registered users.

**The database is implemented with the following tables:**
* files - file storage information for film posters
* genres - film genres
* halls - cinema halls
* films - films for distribution
* film_sessions - available sessions
* users - users registered on the site
* tickets - tickets already purchased for certain film sessions

Please note that all database tables, with the exception of "tickets" and "users", are scripted and filled with liquibase.

Spring Framework is used as the **main framework**. The pages are developed with Thymeleaf and Bootstrap and **dependencies** are used: PostgreSQL 42.5.4, Liquibase 4.15.0 and Sql2o 1.6.0.

**Environment requirements:** Java 17.0.2, PostgreSQL 14.0, Apache Maven 3.8.4

**Used technologies:**
* Java 17
* Maven 3.8
* PostgreSQL 14
* Spring Boot
* Junit Jupiter
* AssertJ
* Mockito
* Liquibase 4
* Sql2o
* H2database
* Thymeleaf
* Bootstrap
* Checkstyle
* Log4J

**How to run the project:**
1. To run the project, you need to clone the project from this repository;
2. Then you need to create a local database "cinema";
3. Specify the login and password for the database you created in the db/liquibase.properties file;
4. Run liquibase to pre-create and autofill tables;
5. Launch the application using one of the following methods:
   5.1 Through the Main class, located in the folder src\main\java\ru\job4j\cinema;
   5.2 Compiling and running the project via maven with mvn spring-boot:run;
   5.3 After building the project via maven and running the built file with the command java -jar job4j_cinema-1.0-SNAPSHOT.jar;
6. Open the page http://localhost:8080/index in the browser;


**App screenshots**

Main page

![main_page.JPG](screenshots/main_page.JPG)

Page with all film sessions

![all_sessions_page.JPG](screenshots/all_sessions_page.JPG)

Page with all session for the chosen film

![all_chosen_film_sessions_page.JPG](screenshots/all_chosen_film_sessions_page.JPG)

Page with all shown films

![all_films_page.JPG](screenshots/all_films_page.JPG)

Page with chosen film information

![film_info_page.JPG](screenshots/film_info_page.JPG)

Registration page

![register_page.JPG](screenshots/register_page.JPG)

Sign in page

![login_page.JPG](screenshots/login_page.JPG)

Purchasing ticket page

![buy_ticket_page.JPG](screenshots/buy_ticket_page.JPG)

Get a ticket page

![success_page.JPG](screenshots/success_page.JPG)

Failure page

![failure_page.JPG](screenshots/failure_page.JPG)

