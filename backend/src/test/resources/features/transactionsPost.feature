Feature: Everything transaction related

        Scenario: Preformatted transaction as admin
        Given I provide valid admin credentials
        And My admin transaction object is valid
        When I perform a transaction
        Then I should see a transaction status code of 200

       Scenario: Preformatted transaction as user
       Given I provide valid user credentials
       And My user transaction object is valid
       When I perform a transaction
       Then I should see a transaction status code of 200

       Scenario: Preformatted transaction as admin
       Given I provide invalid credentials
       And My admin transaction object is valid
       When I perform a transaction
       Then I should see a transaction status code of 401

       Scenario: Preformatted transaction as admin
       Given I provide valid admin credentials
       And My Pin is incorrect
       When I perform a transaction
       Then I should see a transaction status code of 400

       Scenario: Preformatted transaction as admin
       Given I provide valid admin credentials
       And Transaction origin is the same as target
       When I perform a transaction
       Then I should see a transaction status code of 400

        Scenario: Preformatted transaction as user
        Given I provide valid user credentials
        And I am not the owner of the account
        When I perform a transaction
        Then I should see a transaction status code of 401

        Scenario: Preformatted transaction as user
        Given I provide valid user credentials
        And I have exceeded the transaction limit
        When I perform a transaction
        Then I should see a transaction status code of 400

        Scenario: Preformatted transaction as user
        Given I provide valid user credentials
        And IBAN does not exist
        When I perform a transaction
        Then I should see a transaction status code of 400