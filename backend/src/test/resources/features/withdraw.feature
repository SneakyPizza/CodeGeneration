Feature: Everything withdraw related

       Scenario: Preformatted withdraw as user
       Given 'withdraw' I provide valid user credentials
       And 'withdraw' My user withdraw object is valid
       When I perform a withdraw
       Then I should see a withdraw status code of 200

       Scenario: Preformatted transaction as user
       Given 'withdraw' I provide valid user credentials
       And 'withdraw' My Pin is incorrect!
       When I perform a withdraw
         Then I should see a withdraw status code of 400

        Scenario: Preformatted withdraw as user
        Given 'withdraw' I provide valid user credentials
        And 'withdraw' I have exceeded the withdraw limit
        When I perform a withdraw
        Then I should see a withdraw status code of 400

        Scenario: Preformatted withdraw as user
        Given 'withdraw' I provide valid user credentials
        And 'withdraw' IBAN does not exist
        When I perform a withdraw
        Then I should see a withdraw status code of 401