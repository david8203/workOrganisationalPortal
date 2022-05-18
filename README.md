# ORGANISATIONAL NEWS PORTAL
## DESCRIPTION
* Organisational News Portal is a REST API for querying and retrieving scoped news and information. There should be news/articles/posts that are available to all employees without navigating into any department, and others that are housed/classified within departments.

# AUTHOR
## David Ndungu

## TECHNOLOGIES
* java
* git
* postman
* postgresql
* gradle
## GETTING STARTED
> git clone https://github.com/david8203/workOrganisationalPortal.git open in intellij or your preferred IDE.
* and `gradle run`

## SQL
Ensure you have `posgresql` installed Run the following commands to create your database:

input the command `psql <create.sql` in your terminal to **create** the databases

## DROPPING DATABASE
input the command `psql <drop.sql` in your terminal to **DROP** the databases

## REQUIREMENTS
> Test links in App in postman. Make sure java gradle,postman extension
postgresql is installed

## RUNNING LOCALLY
> Change database creadentials to your credentials in the App.java and in the tests

## API DOCUMENTATION

| **URL**                            | **HTTP  VERB** | **DESCRIPTION**                      |
|------------------------------------|-----------|--------------------------------------|
| /add/user/:user_id/department/:department_id | POST      | Add new Users to specific department |
| /news/new/department               | POST      | Adding News to a Department          |
| /news/new/general	                 | POST      | Adding General News                  |
| /users	                            | GET       | Display all users                    |
| /departments                       | GET       | Display all Departments              |
| /news/general                      | GET       | Display all general news             |
| /user/:id/departments              | GET       | Get specific user in a department    |
| /user/:id                          | GET       | Get a specific user                  |
| /department/:id/users              | GET       | Get users in a specific department   |
|  /department/:id                   |   GET     | gets a specific department           |
|/news/department/:id	               | GET       | gets news from a specific department |

### Users
Creating **user** `{ "name":"David" "position":"Manager", "staff_role":"Editor" }`

### Departments
Creating **Departments** `{ "name":"Editing", "description":"Editing of books" }`
### News
Creating **General News { "title":"Meeting", "description":"Discussion about expanding", "user_id":1 }
creating Department News
`{ "title":"Meeting", "description":"Discussion about expanding", "department_id":1, "user_id":1 }`

## LICENSE
This project is licensed under the MIT Open Source license Copyright (c) 2022. LICENCE
