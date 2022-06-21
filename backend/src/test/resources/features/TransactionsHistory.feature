Feature: Everything transaction history


  Scenario: Getting transaction history user
    Given I have an valid user token
    And I provide the correct user IBAN
    When I call the transaction history endpoint
    Then History the result is a status of 404
    And 'History 'I have a error object with message "Account has no transactions"

  Scenario: Getting transaction history admin
    Given I have an valid admin token
    And I provide the correct bank IBAN
    When I call the transaction history endpoint
    Then The result is a list of transactions of size 1
    And It contains a transaction object

    Scenario: Getting transaction history with false IBAN and token
    Given I have an invalid token
    And I provide the incorrect IBAN
    When I call the transaction history endpoint
    Then  History the result is a status of 403
      And Body is Null

    Scenario: Getting transaction history with false IBAN but valid token
    Given I have an valid admin token
    And I provide the incorrect IBAN
    When I call the transaction history endpoint
    Then  History the result is a status of 400
      And 'History 'I have a error object with message "Account does not exist"

  Scenario: Getting transaction history with correct IBAN and invalid token
    Given I have an invalid token
    And I provide the correct bank IBAN
    When I call the transaction history endpoint
    Then  History the result is a status of 403
    And Body is Null