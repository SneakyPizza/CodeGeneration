Feature: Everything GetUser and GetAllUsers related

    # GetAllUsers
    Scenario: Getting all users
        Given I have a valid token for an admin
        And I have a valid limit and offset
        When I call the GetAllUsers endpoint
        Then I should see a 200 status code
        And I should see a list of users

    Scenario: Getting all users with a user jwt token
        Given I have a valid token for an user
        And I have a valid limit and offset
        When I call the GetAllUsers endpoint
        Then I should see a 403 status code
        And I get an error object with message "You are not authorized to perform this action"

    Scenario: Getting all users but limit is too high
       Given The limit is higher than 50
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a 400 status code
        And I get an error object with message "Limit must be between 1 and 50"

    Scenario: Getting all users but limit is too low
        Given The limit is lower than 1
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a 400 status code
        And I get an error object with message "Limit must be between 1 and 50"

    Scenario: Getting all users but offset is too low
        Given The offset is lower than 0
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a 400 status code
        And I get an error object with message "Offset should be between 0 and the total number of users"

    Scenario: Getting all users but offset is higher than the total number of users
        Given
        And I have a valid admin token
        When I call the GetAllUsers endpoint
        Then I should see a 400 status code
        And I get an error object with message "Offset should be between 0 and the total number of users"

    # GetUser
    Scenario: Getting user with valid id
        Given I provide a correct user id
        And I have a valid limit and offset
        And I have a valid user token
        When I call the GetAllUser endpoint i get all users and use the id of the first user to get a user from GetUser
        Then I should see a 200 status code
        And I should see a user object

    Scenario: Getting user but id is in wrong format
        Given I have a user id that is in wrong format
        And I have a valid limit and offset
        And I have a valid user token
        When I call the GetUser endpoint
        Then I should see a 400 status code
        And I get an error object with message "Invalid UUID"


    Scenario: Getting user but user id doesn't exist in database
        Given I have a user id that is not in the database
        And I have a valid limit and offset
        And I have a valid user token
        When I call the GetUser endpoint
        Then I should see a 404 status code
        And I get an error object with message "User not found"

    Scenario: Getting user but user doesn't own user
        Given I have a valid user token
        And I have a valid limit and offset
        And I have a valid user token
        When I call the GetUser endpoint
        Then I should see a 401 status code
        And I get an error object with message "You are not authorized to perform this action"
