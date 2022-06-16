Feature: Get all accounts from the database

       Scenario: Getting all accounts from the database
       Given 'getall-account' I provide valid admin credentials
       And 'getall-account' My accept header is valid
       When 'getall-account' I perform a get all accounts operation
       Then I should see a get all accounts status code of 200

       Scenario: Getting all accounts from the database
       Given 'getall-account' I provide invalid admin credentials
       And 'getall-account' My accept header is valid
       When 'getall-account' I perform a get all accounts operation
       Then I should see a get all accounts status code of 403