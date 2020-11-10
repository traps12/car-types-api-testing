Feature: Verify errors for car build dates api

  Scenario:user should not get car build date details without key
    Given build dates API endpoint and main type and manufacturer code
    When  access build dates api without token
    Then  status code is 401 for build dates api
    And   build dates api response body contains message and error details

  Scenario: user should not get car build date details with invalid key
    Given manufacturer API endpoint
    When  access build dates api with invalid token
    Then  status code is 403 for build dates api
    And   build dates api response body contains message and error details

  Scenario: user should not get car build date details with invalid locale
    Given  manufacturer API endpoint
    When  access build dates api with invalid locale
    Then status code is 200 for build dates api