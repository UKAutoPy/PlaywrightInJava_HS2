@smoketest
Feature: XE currency conversion
  As a tester
  I want to validate XE currency conversions from a CSV file
  So that the displayed converted value matches the expected result derived from the shown exchange rate

  Scenario: Validate XE conversions from CSV data
    Given the XE Currency Converter site is opened
    When I load the currency conversion records from "testdata/curr_pair.csv"
    Then each record should produce the expected converted value