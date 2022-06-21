Feature: Everything GetUser and GetAllUsers related

    # GetAllUsers
    Scenario: Getting all users
        Given I have a valid token for an admin
        And I have a valid limit and offset
        When I call the GetAllUsers endpoint
        And I get a list of users
        Then I should see a 200 status code
        And I should see a list of users

    Scenario: Getting all users with a user jwt token
        Given I have a valid token for a user
        And I have a valid limit and offset
        When I call the GetAllUsers endpoint
        Then I should see a 403 status code
        And I get an error object with message "You are not authorized to perform this action"

    Scenario: Getting all users but limit is too high
        Given I have a valid token for an admin
        And The limit is higher than 50
        When I call the GetAllUsers endpoint
        Then I should see a 500 status code
        And I get an error object with message "getAllUsers.limit: must be less than or equal to 50"

    Scenario: Getting all users but limit is too low
        Given I have a valid token for an admin
        And The limit is lower than 1
        When I call the GetAllUsers endpoint
        Then I should see a 500 status code
        And I get an error object with message "getAllUsers.limit: must be greater than or equal to 1"

    Scenario: Getting all users but offset is too low
        Given I have a valid token for an admin
        And The offset is lower than 0
        When I call the GetAllUsers endpoint
        Then I should see a 500 status code
        And I get an error object with message "getAllUsers.offset: must be greater than or equal to 0"

    Scenario: Getting all users but offset is higher than the total number of users
        Given I have a valid token for an admin
        And The offset is higher than the total number of users
        When I call the GetAllUsers endpoint
        Then I should see a 400 status code
        And I get an error object with message "Offset should be between 0 and the total number of users"

    # GetUser
    Scenario: Getting user with valid id
        Given I have a valid limit and offset
        And I provide a correct user id from the GetAllUsers endpoint
        And I have a valid admin token
        When I call the GetUser endpoint
        Then I should see a 200 status code
        And I should see a user object

    Scenario: Getting user but id is in wrong format
        Given I have a user id that is in wrong format
        And I have a valid admin token
        When I call the GetUser endpoint
        Then I should see a 400 status code
        And I get an user error with message "Invalid UUID"


    Scenario: Getting user but user id doesn't exist in database
        Given I have a user id that is not in the database
        And I have a valid admin token
        When I call the GetUser endpoint
        Then I should see a 404 status code
        And I get an user error with message "User not found"

    Scenario: Getting user but user doesn't own user
        Given I have a valid token for a user
        And I have a valid limit and offset
        And I provide a correct user id from the GetAllUsers endpoint
        When I call the GetUser endpoint
        Then I should see a 401 status code
        And I get an user error with message "You are not authorized to perform this action"
