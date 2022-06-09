Feature: Everything UpdateUser related

    Scenario: UpdateUser with valid data
        Given I give valid user information
        And I have a valid jwt token
        And I have a valid user id
        When I call the UpdateUser endpoint
        Then I should get my updated user and get a status code of 200

    Scenario: UpdateUser with fields that are null
        Given I give invalid user information
        And I have a valid jwt token
        And I have a valid user id
        When I call the UpdateUser endpoint
        Then I should get an error message and get a status code of 400

    Scenario: UpdateUser with invalid jwt token
        Given I give valid user information
        And I have an invalid jwt token
        When I call the UpdateUser endpoint
        Then I should get an error message and get a status code of 403

    Scenario: UpdateUser with user id that is null
        Given I give valid user information
        And I have a valid jwt token
        And The user id is null
        When I call the UpdateUser endpoint
        Then I should get an error message and get a status code of 400

    Scenario: UpdateUser but the user doesn't exist
        Given I give valid user information
        And I have a valid jwt token
        And The user id is not in the database
        When I call the UpdateUser endpoint
        Then I should get an error message and get a status code of 404