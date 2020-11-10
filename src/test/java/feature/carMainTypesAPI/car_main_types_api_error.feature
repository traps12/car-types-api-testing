Feature: Verify errors for car main types api

  Scenario:user should not get car main types list without key
    Given main types API endpoint and manufacturer code
    When  access main types api without token
    Then  status code is 401 for main types api
    And   main types api response body contains message and error details

  Scenario: user should not get car main types list with invalid key
    Given main types API endpoint and manufacturer code
    When  access main types api with invalid token
    Then  status code is 403 for main types api
    And   main types api response body contains message and error details

  Scenario: user should not get car main types list with invalid locale
    Given  main types API endpoint and manufacturer code
    When  access main types api with invalid locale
    Then  status code is 200 for main types api