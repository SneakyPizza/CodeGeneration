Feature: search on name and return NameSearchAccountDTO

       Scenario: searching on a fullname of a user
       Given 'search-account' I provide valid user credentials
       And 'search-account' My fullname is valid
       And 'search-account' My accept header is valid
       When 'search-account' I perform a search account operation
       Then I should see a search account status code of 200

       Scenario: searching on a fullname of a user
       Given 'search-account' I provide invalid user credentials
       And 'search-account' My fullname is valid
       And 'search-account' My accept header is valid
       When 'search-account' I perform a search account operation
       Then I should see a search account status code of 403
       And I should receive an search account error message with "You need to be a User to preform this action"

       Scenario: searching on a fullname of a user
       Given 'search-account' I provide valid user credentials
       And 'search-account' My fullname is invalid
       And 'search-account' My accept header is valid
       When 'search-account' I perform a search account operation
       Then I should see a search account status code of 400
       And I should receive an search account error message with "Invalid fullname, please use '-' between the first and last name once."
