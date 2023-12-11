Feature: the user can create, retrieve and reserve the books
  Scenario: user creates two books and retrieves both of them
    When the user creates the book "Avatar" written by "James" with a reservation "false"
    And the user creates the book "A" written by "B" with a reservation "false"
    And the user reserve the book "Avatar"
    And the user gets all books
    Then the list should contain the following books in the same order
      | title   | author | reserved |
      | A       | B      | false    |
      | Avatar  | James  | true    |