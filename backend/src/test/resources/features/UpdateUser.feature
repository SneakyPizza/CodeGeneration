Feature: Everything UpdateUser related

    Scenario: UpdateUser with valid data
        Given I give valid user information
        And I have a valid jwt token
        When I call the UpdateUser endpoint
        Then I should get my updated user and get a status code of 200

    Scenario: UpdateUser with invalid data
        Given I give invalid user information
        And I have a valid jwt token
        When I call the UpdateUser endpoint
        Then I should get an error message and get a status code of 400