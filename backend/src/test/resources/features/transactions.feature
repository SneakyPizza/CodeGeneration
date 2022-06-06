Feature: Everything transaction

  Scenario: Getting transaction history
    Given I have a valid token for role "user" and I own the account "
    When I call the transaction endpoint
    Then the result is a list of transactions of size 2

  Scenario: Getting transaction with invalid token
    Given I have an invalid token
    When I call the transaction endpoint
    Then the result is a status of 403

  Scenario: Getting guitars with an expired token
    Given I have an expired token
    When I call the transaction endpoint
    Then the result is a status of 403

  Scenario: Posting transaction with user role
    Given I have a valid token for role "user"
    And I have access to the account
    And I have a valid transaction object
    When I make a post request to the transaction endpoint
    Then the result is a status of 200

  Scenario: Posting transaction with user role
    Given I have a valid token for role "user"
    And I have do not access to the account
    And I have a valid transaction object
    When I make a post request to the transaction endpoint
    Then the result is a status of 403

  Scenario: Posting transaction with admin role
    Given I have a valid token for role "admin"
    And I have access to the account
    And I have a valid transaction object
    When I make a post request to the transaction endpoint
    Then the result is a status of 200