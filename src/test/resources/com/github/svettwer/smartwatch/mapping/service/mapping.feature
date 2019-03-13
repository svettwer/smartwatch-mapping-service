Feature: Mapping smartwatch
  Maps a smartwatch to a customers account

  Scenario: Create new mapping
    Given a new paring is initiated
    When the pairing was successful
    Then the pairing is persisted in the database