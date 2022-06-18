Feature: Everything AddUser related

    Scenario: Adding a user with no account
        Given I provide valid user details
        When I call the AddUser endpoint
        Then I should see a user status code of 201
        And I should receive the user added to the database

    Scenario: Adding a user with no account with fields that are null
        Given I provide wrong user details with null values
        When I call the AddUser endpoint
        Then I should see a user status code of 400
        And I should receive an error message with "Bad Request"

    Scenario: AddUser but the user already exists
        Given I provide valid user details
        When I call the AddUser endpoint twice
        Then I should see a user status code of 409
        And I should receive an error message with "safx"

    Scenario: AddUser with invalid jwt token
        Given I provide valid user details
        When I call the AddUser endpoint
        Then I should see a user status code of 403
        And I should receive an error message with "asffxcz"