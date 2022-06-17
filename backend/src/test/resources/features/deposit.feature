Feature: Everything deposit related

       Scenario: Preformatted deposit as user
       Given 'deposit' I provide valid user credentials
       And My user deposit object is valid
       When I perform a deposit
       Then I should see a deposit status code of 200
         And 'Deposit' I should have a transaction object with type "DEPOSIT"

       Scenario: Preformatted deposit as admin
       Given 'deposit' I provide valid admin credentials
       And 'deposit' My Pin is not correct!
       When I perform a deposit
       Then I should see a deposit status code of 401
         And 'Deposit' I have a error object with message "Invalid Pin code"

        Scenario: Preformatted deposit as admin
        Given 'deposit' I provide valid user credentials
        And 'deposit' IBAN does not exist!
        When I perform a deposit
        Then I should see a deposit status code of 401
            And 'Deposit' I have a error object with message "You are not the owner of this account"