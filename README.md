# Yoga App

## Prerequisites

To use this app, you need:

* Java 11
* NodeJS 16
* MySQL
* Angular CLI

Clone this repository on your computer.

## Install Database

Open your MySQL or your PHPMyAdmin as an admin.
Create a new table (default: P5_OCR)
Copy the content of /ressources/sql/script.sql

Open /back/src/main/resources/application.properties and change:
* spring.datasource.url (default: jdbc:mysql://127.0.0.1:3306/P5_OCR)
* spring.datasource.username (your SQL username)
* spring.datasource.password (your SQL password)

Don't forget to GRANT ALL PRIVILEGES to this user on this table

## Install BackEnd

Run `mvn install` (or `mvn package`) to install all dependencies.
Run `mvn spring-boot:run` to run BackEnd (default: port 8080)

## Install FrontEnd

Don't forget to install your node_modules before starting (`npm install`).
Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.
Run `ng build --base-href ./` to build the project. The build artifacts will be stored in the `dist/` directory, and can be served into your server folder (Apache, Nginx, etc.)

## Run tests with coverage on BackEnd

Run `mvn clean test` to run all tests with Spring test and generate coverage with JaCoCo

## Run tests with coverage on FrontEnd

Run `jest --coverage` or `npx jest --coverage` to run all FrontEnd tests with Jest and generate coverage with istanbul.
Run `npm run e2e:ci` to launch all End-to-End tests with Cypress.
Run `npm run e2e:coverage` to generate code coverage with istanbul.