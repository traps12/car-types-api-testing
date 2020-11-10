Feature: Verify errors for car manufacturer api

  Scenario:user should not get manufacturer list without key
    Given manufacturer API endpoint
    When  access manufacturer api without token
    Then  status code is 401
    And   manufacturer api response body contains message and error details

  Scenario: user should not get manufacturer list with invalid key
    Given manufacturer API endpoint
    When  access manufacturer api with invalid token
    Then  status code is 403
    And   manufacturer api response body contains message and error details

  Scenario: user should not get manufacturer list with invalid locale
    Given manufacturer API endpoint
    When  access manufacturer api with invalid locale
    Then status code is 200