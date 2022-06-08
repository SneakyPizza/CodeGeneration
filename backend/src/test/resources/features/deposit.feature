Feature: Everything deposit related

       Scenario: Preformatted deposit as user
       Given 'deposit' I provide valid user credentials
       And My user deposit object is valid
       When I perform a deposit
       Then I should see a deposit status code of 200

       Scenario: Preformatted deposit as admin
       Given 'deposit' I provide valid admin credentials
       And 'deposit' My Pin is not correct!
       When I perform a deposit
       Then I should see a deposit status code of 401

        Scenario: Preformatted deposit as admin
        Given 'deposit' I provide valid user credentials
        And 'deposit' IBAN does not exist!
        When I perform a deposit
        Then I should see a deposit status code of 401