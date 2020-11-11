Feature: Verify errors for car main types api

  Scenario:user should not get car main types list without key
    Given main types API endpoint
    When  access main types api without token
    Then  status code is 401 for main types api
    And   main types api response body contains error message "Unauthorized"

  Scenario: user should not get car main types list with invalid key
    Given main types API endpoint
    When  access main types api with invalid token
    Then  status code is 403 for main types api
    And   main types api response body contains error message "Forbidden"

  Scenario: user should not get car main types list without manufacturer code
    Given main types API endpoint
    When  access main types api without manufacturer code
    Then  status code is 400 for main types api
    And   main types api response body contains error message "Bad Request"

