Feature: Add a account to the database

       Scenario: Adding a new account to the database
       Given 'add-account' I provide valid admin credentials
       And 'add-account' My account object is valid
       And 'add-account' My accept header is valid
       When 'add-account' I perform a add account operation
       Then I should see a add account status code of 201    

       Scenario: Adding a new account to the database
       Given 'add-account' I provide invalid admin credentials
       And 'add-account' My account object is valid
       And 'add-account' My accept header is valid
       When 'add-account' I perform a add account operation
       Then I should see a add account status code of 401
       And I should receive an add account error message with "You need to be a admin to preform this action"

       Scenario: Adding a new account to the database
       Given 'add-account' I provide valid admin credentials
       And 'add-account' My account object is invalid
       And 'add-account' My accept header is valid
       When 'add-account' I perform a add account operation
       Then I should see a add account status code of 404
       And I should receive an add account error message with "User was not found"

       Scenario: Adding a new account to the database
       Given 'add-account' I provide valid admin credentials
       And 'add-account' My accept header is invalid
       And 'add-account' My account object is valid
       When 'add-account' I perform a add account operation
       Then I should see a add account status code of 500
       And I should receive an add account error message with "Content type 'text/plain;charset=ISO-8859-1' not supported"