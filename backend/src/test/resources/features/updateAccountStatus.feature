Feature: update a account of the user

       Scenario: updating a account of a user
       Given 'update-account' I provide valid admin credentials
       And 'update-account' My iban is valid
       And 'update-account' My account is valid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 200

       Scenario: updating a account of a user
       Given 'update-account' I provide invalid admin credentials
       And 'update-account' My iban is valid
       And 'update-account' My account is valid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 403

       Scenario: updating a account of a user
       Given 'update-account' I provide valid admin credentials
       And 'update-account' My iban is invalid
       And 'update-account' My account is valid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 400

       Scenario: updating a account of a user
       Given 'update-account' I provide valid admin credentials
       And 'update-account' My iban is valid
       And 'update-account' My account is invalid
       And 'update-account' My accept header is valid
       When 'update-account' I perform a update account operation
       Then I should see a update account status code of 200


