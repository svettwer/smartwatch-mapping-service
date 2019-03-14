Feature: Mapping smartwatch
  Maps a smartwatch to a customers account

  Scenario: New pairing
    Given a new paring is initiated
    When the pairing was successful
    Then the pairing is persisted in the database