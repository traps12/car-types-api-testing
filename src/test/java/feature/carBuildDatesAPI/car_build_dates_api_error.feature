Feature: Verify errors for car build dates api

  Scenario:user should not get car build date without key
    Given build dates API endpoint
    When  access build dates api without token
    Then  status code is 401 for build dates api
    And   build dates api response body contains error message "Unauthorized"

  Scenario: user should not get car build date with invalid key
    Given build dates API endpoint
    When  access build dates api with invalid token
    Then  status code is 403 for build dates api
    And   build dates api response body contains error message "Forbidden"

  Scenario: user should not get car build date without manufacturer
    Given  build dates API endpoint
    When  access build dates api without manufacturer code
    Then  status code is 400 for build dates api
    And   build dates api response body contains error message "Bad Request"

  Scenario: user should not get car build date without main types
    Given  build dates API endpoint
    When  access build dates api without main type
    Then status code is 400 for build dates api
    And   build dates api response body contains error message "Bad Request"