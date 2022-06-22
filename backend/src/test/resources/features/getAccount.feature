Feature: Get an account to the database with iban

       Scenario: Getting an account from the database
       Given 'get-account' I provide valid admin credentials
       And 'get-account' My iban is valid
       And 'get-account' My accept header is valid
       When 'get-account' I perform a get account operation
       Then I should see a get account status code of 200
       And 'get-account' I should receive the account

       Scenario: Getting an account from the database
       Given 'get-account' I provide valid admin credentials
       And 'get-account' My iban is invalid
       And 'get-account' My accept header is valid
       When 'get-account' I perform a get account operation
       Then I should see a get account status code of 500
       And I should receive an get account error message with "Invalid iban"

       Scenario: Getting an account from the database
       Given 'get-account' I provide invalid admin credentials
       And 'get-account' My iban is valid
       And 'get-account' My accept header is valid
       When 'get-account' I perform a get account operation
       Then I should see a get account status code of 401
       And I should receive an get account error message with "You need to be a admin to preform this action or own the account"