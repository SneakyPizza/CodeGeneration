Feature: Everything AddUser related

    Scenario: AddUser with valid data
        Given I provide valid user details
        And I have a valid token jwt
        When I call the AddUser endpoint
        Then The user is added to the database and i get status code 200

    Scenario: AddUser with fields that are null
        Given I provide wrong user details with null values
        And I have a valid token jwt
        When I call the AddUser endpoint
        Then The user is not added to the database and i get status code 400

    Scenario: AddUser but the user already exists
        Given I provide valid user details
        And I have a valid token jwt
        When I call the AddUser endpoint twice
        Then The user is not added to the database and i get status code 409

    Scenario: AddUser with invalid jwt token
        Given I provide valid user details
        And I have an invalid token jwt
        When I call the AddUser endpoint
        Then The user is not added to the database and i get status code 403