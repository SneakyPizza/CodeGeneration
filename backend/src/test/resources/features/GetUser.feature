Feature: Everything GetUser related

    Scenario: Getting user with valid id
        Given I have a valid user id
        When I call the GetUser endpoint
        Then I should see a user status code of 200

    Scenario: Getting user with invalid id
        Given I have an invalid user id
        When I call the GetUser endpoint
        Then I should see a user status code of 404

    Scenario: Getting user but id is null
        Given I have a null user id
        When I call the GetUser endpoint
        Then I should see a user status code of 400
