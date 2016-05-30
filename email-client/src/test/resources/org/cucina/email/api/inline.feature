Feature: Create inline and retrieve template

Scenario: Inline Template feature test
  Given template='abcde fgh'
  When I create template with name=inline and locale=null
  Then I should get the same template with name=inline and locale=null
  