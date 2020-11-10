Feature: Get list of car manufacture

  Scenario Outline: Get list of car manufacturers with valid token and locale as GB
    Given manufacturer API endpoint
    When  I perform GET operation with valid key and "<locale>"
    Then  status code is 200
    And   I get list of manufacturer car types in response body
    Examples:
      | locale |
      |GB|
      |DE|

  Scenario: Get list of car manufacturers with valid token and without locale
    Given manufacturer API endpoint
    When  user access resource with valid token and without locale
    Then  status code is 200
    And   api should work with default locale


  Scenario: Json schema validation for manufacturer api
    Given manufacturer API endpoint
    When I perform GET operation with valid key and "GB"
    Then response structure should match with json schema