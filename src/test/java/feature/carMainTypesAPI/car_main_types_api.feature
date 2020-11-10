Feature: Get list of main types for given manufacture

  Scenario Outline: Get list of car main-types with valid token and locale as GB
    Given main types API endpoint and manufacturer code
    When  I perform GET operation with valid key and "<locale>" and manufacturer code
    Then  status code is 200 for main types api
    And   I get list of main types of car in response body
    Examples:
      | locale |
      |GB|
      |DE|

  Scenario: Get list of car main types with valid token manufacturer code and without locale
    Given main types API endpoint and manufacturer code
    When  user access main type api with valid token manufacturer code and without locale
    Then  status code is 200 for main types api
    And   main type api should work with default locale


  Scenario: Json schema validation for main types api
    Given main types API endpoint and manufacturer code
    When I perform GET operation with valid key and "en" and manufacturer code
    Then response structure should match with json schema of main type

