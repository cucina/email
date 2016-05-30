Feature: Create and retrieve template

Scenario: Template feature test
  Given Template is created with name=test and fileName=templates/Test.ftl and locale=null
  When I request to getTemplate with name=test and locale=null
  Then I should get the created template