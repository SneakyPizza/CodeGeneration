Feature: update a account of the user

       Scenario: updating a account of a user
       Given 'update-account' I provide valid admin credentials
       And 'update-account' My iban is valid
       And 'update-account' My account is valid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 200
       And I should receive the updated account added to the database

       Scenario: updating a account of a user
       Given 'update-account' I provide invalid admin credentials
       And 'update-account' My iban is valid
       And 'update-account' My account is valid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 401
       And I should receive an update account error message with "You need to be a admin to preform this action or own the account"

       Scenario: updating a account of a user
       Given 'update-account' I provide valid admin credentials
       And 'update-account' My iban is invalid
       And 'update-account' My account is valid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 500
       And I should receive an update account error message with "Invalid iban"


