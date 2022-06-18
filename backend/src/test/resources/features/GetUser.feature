Feature: Everything GetUser and GetAllUsers related

    # GetAllUsers
    Scenario: Getting all users
        Given I have a valid token for an admin
        When I call the GetAllUsers endpoint
        Then I should see a status code of 200
        And I should see a list of users

    Scenario: Getting all users with a user jwt token
        Given I have a valid token for a user
        When I call the GetAllUsers endpoint
        Then I should see a status code of 403

    Scenario: Getting all users but limit is too high
       Given The limit is higher than 50
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a status code of 400
        And I get an error object with message "Limit must be between 1 and 50"

    Scenario: Getting all users but limit is too low
        Given The limit is lower than 1
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a status code of 400
        And I get an error object with message "Limit must be between 1 and 50"

    Scenario: Getting all users but offset is too low
        Given The offset is lower than 0
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a status code of 400
        And I get an error object with message "Offset should be between 0 and the total number of users"

    Scenario: Getting all users but offset is higher than the total number of users
        Given
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a status code of 400
        And I get an error object with message "Offset should be between 0 and the total number of users"

    # GetUser
    Scenario: Getting user with valid id
        Given I provide a correct user id
        And I have a valid user token
        When I call the GetAllUser endpoint i get all users and use the id of the first user to get a user from GetUser
        Then I should see a status code of 200

    Scenario: Getting user but id is null
        Given I have a user id that is null
        And I have a valid user token
        When I call the GetUser endpoint
        Then I should see a status code of 400

    Scenario: Getting user but user id doesn't exist in database
        Given I have an invalid user id
        And I have a valid user token
        When I call the GetUser endpoint
        Then I should see a status code of 404
