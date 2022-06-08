Feature: Get an account to the database with iban

       Scenario: Getting an account from the database
       Given 'get-account' I provide valid admin credentials
       And 'get-account' My iban is valid
       And 'get-account' My accept header is valid
       When 'get-account' I perform a get account operation
       Then I should see a get account status code of 200

       Scenario: Getting an account from the database
       Given 'get-account' I provide valid admin credentials
       And 'get-account' My iban is invalid
       And 'get-account' My accept header is valid
       When 'get-account' I perform a get account operation
       Then I should see a get account status code of 400

       Scenario: Getting an account from the database
       Given 'get-account' I provide invalid admin credentials
       And 'get-account' My iban is valid
       And 'get-account' My accept header is valid
       When 'get-account' I perform a get account operation
       Then I should see a get account status code of 403