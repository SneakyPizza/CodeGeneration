Feature: Everything UpdateUser related

    Scenario: UpdateUser
        Given I give valid user information
        And I have a valid jwt token
        When I call the UpdateUser endpoint
        Then I should get my updated user and get a status code of 200