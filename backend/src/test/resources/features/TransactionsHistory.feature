Feature: Everything transaction history


  Scenario: Getting transaction history user
    Given I have an valid user token
    And I provide the correct user IBAN
    When I call the transaction history endpoint
    Then History the result is a status of 204

  Scenario: Getting transaction history admin
    Given I have an valid admin token
    And I provide the correct bank IBAN
    When I call the transaction history endpoint
    Then The result is a list of transactions of size 1

    Scenario: Getting transaction history with false IBAN and token
    Given I have an invalid token
    And I provide the incorrect IBAN
    When I call the transaction history endpoint
    Then  History the result is a status of 403

    Scenario: Getting transaction history with false IBAN but valid token
    Given I have an valid admin token
    And I provide the incorrect IBAN
    When I call the transaction history endpoint
    Then  History the result is a status of 404

    Scenario: Getting transaction history with correct IBAN and invalid token
    Given I have an invalid token
    And I provide the correct bank IBAN
    When I call the transaction history endpoint
    Then  History the result is a status of 403