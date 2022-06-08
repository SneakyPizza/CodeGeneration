Feature: Everything AddUser related

    Scenario: Add a new user
        Given I provide all the user details
        When I call the AddUser endpoint
        Then The user is added to the database and i get status code 200