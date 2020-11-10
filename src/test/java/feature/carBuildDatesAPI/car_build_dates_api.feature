Feature: Get list of car build dates for given manufacture and main types

  Scenario Outline: Get list of all car build dates
    Given build dates API endpoint and main type and manufacturer code
    When  I perform GET operation with valid key "<locale>" manufacturer code and main types
    Then  status code is 200 for build dates api
    And   I get list of build dates of car in response body
    Examples:
      | locale |
      |GB|
      |DE|

  Scenario: Get list of car build dates with valid token manufacturer code and without locale
    Given build dates API endpoint and main type and manufacturer code
    When  I perform GET operation with valid key "<locale>" manufacturer code and main types
    Then  status code is 200 for build dates api
    And   build date api should work with default locale


  Scenario: Json schema validation for build date api
    Given build dates API endpoint and main type and manufacturer code
    When I perform GET operation with valid key "<locale>" manufacturer code and main types
    Then response structure should match with json schema of build dates

