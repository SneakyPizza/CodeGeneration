Feature: Everything AddUser related

    Scenario: Adding a new user
        Given I provide valid user details
        When I call the signup endpoint
        Then I should see a user status code of 201
        And I should receive the user added to the database as user

    Scenario: Adding a new user with fields that are null
        Given I provide wrong user details with null values
        When I call the signup endpoint
        Then I should see a user status code of 400
        And I should receive an error message with "All fields must be filled in"

    Scenario: Adding a user with no account as admin
        Given I provide valid user details as admin
        And I am logged in as an admin
        When I call the AddUser endpoint
        Then I should see a user status code of 201
        And I should receive the user added to the database as admin

    Scenario: Adding a user with no account as user to admin endpoint
        Given I provide valid user details as admin
        And I am logged in as user
        When I call the AddUser endpoint
        Then I should see a user status code of 403
        And I should receive an error message with "You are not authorized to perform this action"