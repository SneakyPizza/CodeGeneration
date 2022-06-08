Feature: Everything GetUser and GetAllUsers related

    # GetAllUsers
    Scenario: Getting all users to get the user id
        Given I have a valid token for an admin
        When I call the GetAllUsers endpoint
        Then I should see all the users in a list and return a 200 status code

    #Scenario: Getting all users but limit is too high
    #    Given The limit is higher than 50
    #    And I have a valid admin token
    #    When I call the GetAllUsers endpoint
    #    Then I should get a status code of 400

    #Scenario: Getting all users but limit is too low
    #    Given The limit is lower than 1
    #    And I have a valid admin token
    #    When I call the GetAllUsers endpoint
    #    Then I should get the first 20 users and return a 200 status code

    # GetUser
    Scenario: Getting user with valid id
        Given I have a valid user id
        And I have a valid token
        When I call the GetUser endpoint
        Then I should see a user status code of 200

    Scenario: Getting user with invalid id
        Given I have an invalid user id
        And I have a valid token
        When I call the GetUser endpoint
        Then I should see a user status code of 400

    Scenario: Getting user but id is null
        Given I have a user id that is null
        And I have a valid token
        When I call the GetUser endpoint
        Then I should see a user status code of 400
