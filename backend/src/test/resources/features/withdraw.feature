Feature: Everything withdraw related

       Scenario: Preformatted withdraw as user
       Given 'withdraw' I provide valid user credentials
       And 'withdraw' My user withdraw object is valid
       When I perform a withdraw
       Then I should see a withdraw status code of 200
       And I should have a withdraw object with type "WITHDRAWAL"

       Scenario: Preformatted transaction as user
       Given 'withdraw' I provide valid user credentials
       And 'withdraw' My Pin is incorrect!
       When I perform a withdraw
         Then I should see a withdraw status code of 401
         And I should have a error object with message "Invalid Pin code"

        Scenario: Preformatted withdraw as user
        Given 'withdraw' I provide valid user credentials
        And 'withdraw' I have exceeded the withdraw limit
        When I perform a withdraw
        Then I should see a withdraw status code of 400
        And I should have a error object with message "Transaction limit exceeded"

        Scenario: Preformatted withdraw as user
        Given 'withdraw' I provide valid user credentials
        And 'withdraw' IBAN does not exist
        When I perform a withdraw
        Then I should see a withdraw status code of 401
        And I should have a error object with message "You are not the owner of this account"