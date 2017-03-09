Feature: Ordering Cookies

  This feature support the way a Customer can order cookies through the TCF system

  Background:
    Given a customer named Seb with credit card 1234896983

  Scenario: The cart is empty by default
    When Seb asks for his cart contents
    Then there is 0 item inside the cart


  Scenario: adding cookies to a cart
    When Seb orders 1 x CHOCOLALALA
      And Seb asks for his cart contents
    Then there is 1 item inside the cart
      And the cart contains the following item: 1 x CHOCOLALALA

  Scenario: Ordering multiple cookies
    When Seb orders 1 x CHOCOLALALA
      And Seb orders 1 x SOO_CHOCOLATE
      And Seb asks for his cart contents
    Then there are 2 items inside the cart
      And the cart contains the following item: 1 x CHOCOLALALA
      And the cart contains the following item: 1 x SOO_CHOCOLATE

  Scenario: Modifying the number of cookies inside an order
    When Seb orders 2 x CHOCOLALALA
      And Seb orders 3 x DARK_TEMPTATION
      And Seb orders 3 x CHOCOLALALA
      And Seb asks for his cart contents
    Then there are 2 items inside the cart
      And the cart contains the following item: 5 x CHOCOLALALA
      And the cart contains the following item: 3 x DARK_TEMPTATION

  Scenario: Changing mind while ordering cookies
    When Seb orders 7 x CHOCOLALALA
      And Seb decides not to buy 2 x CHOCOLALALA
      And Seb asks for his cart contents
    Then there is 1 item inside the cart
      And the cart contains the following item: 5 x CHOCOLALALA


  Scenario: Getting the right price for a given cart
    When Seb orders 5 x CHOCOLALALA
      And Seb orders 3 x DARK_TEMPTATION
    Then the price of Seb's cart is equals to 12.20