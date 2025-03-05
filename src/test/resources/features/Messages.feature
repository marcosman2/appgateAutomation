Feature: Customer availability response
  As a user
  I want to check the availability of a Customer for a Phishing service

  @regression
  Scenario: Check availability when no Customer is passed
    When User sends the request, using Customer "customer.blank"
    Then User validates that Customer is not available

  @regression
  Scenario: Check availability when Customer does not exist in DB
    When User sends the request, using Customer "customer.nonExisting"
    Then User validates that Customer is not available

  @regression
  Scenario: Check availability when Customer is active and the Phishing service is active too
    When User sends the request, using Customer "customer.activeActive"
    Then User validates that Customer is available

  @regression
  Scenario: Check availability when Customer is active, but the Phishing service is inactive
    When User sends the request, using Customer "customer.activeInactive"
    Then User validates that Customer is not available

  @regression
  Scenario: Check availability when Customer is inactive, but the Phishing service is active
    When User sends the request, using Customer "customer.inactiveActive"
    Then User validates that Customer is not available

  @regression
  Scenario: Check availability when Customer is inactive and the Phishing service is inactive too
    When User sends the request, using Customer "customer.inactiveInactive"
    Then User validates that Customer is not available

  @regression
  Scenario: Check that response schema is the expected one
    When User sends the request, using Customer "customer.schema"
    Then User validates that the response schema is the expected one

  @regression
  Scenario: Check that Customer fetched on the respond corresponds to the one on the request
    When User sends the request, using Customer "customer.value"
    Then User validates that Customer on the response is the expected one "customer.value"

  @regression
  Scenario: Check that service handles the maximum length of Customer
    When User sends the request, using Customer "customer.maxLength"
    Then User validates that the response schema is the expected one

  @regression
  Scenario: Check that service handles special characters on Customer
    When User sends the request, using Customer "customer.specialCharacters"
    Then User validates that the response schema is the expected one