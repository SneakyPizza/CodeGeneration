Feature: Everything UpdateUser related

    Scenario: UpdateUser with valid data
        Given I give valid user information
        And I have a valid admin jwt token
        And I have a valid user id
        When I call the UpdateUser endpoint
        Then I should receive a status code of 200 from the endpoint
        And I should receive the updated user information

    Scenario: UpdateUser with fields that are null
        Given I give invalid user information
        And I have a valid admin jwt token
        And I have a valid user id
        When I call the UpdateUser endpoint
        Then I should receive a status code of 400 from the endpoint
        And I should receive a message stating "All fields must be filled in"

    Scenario: UpdateUser with invalid jwt token
        Given I give valid user information
        And I have an user jwt token
        When I call the UpdateUser endpoint
        Then I should receive a status code of 403 from the endpoint
        And I should receive a message stating "You are not authorized to perform this action"

    Scenario: UpdateUser with user id that is in the wrong format
        Given I give valid user information
        And I have a valid admin jwt token
        And The user id is in the wrong format
        When I call the UpdateUser endpoint
        Then I should receive a status code of 400 from the endpoint
        And I should receive a message stating "Invalid UUID"

    Scenario: UpdateUser but the user doesn't exist
        Given I give valid user information
        And I have a valid admin jwt token
        And The user id is not in the database
        When I call the UpdateUser endpoint
        Then I should receive a status code of 404 from the endpoint
        And I should receive a message stating "User not found"
