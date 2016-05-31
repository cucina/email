Feature: Create inline, send and retrieve mail

Scenario: Simple send feature test
  Given template 
  """Subject: A message from Cucina
abcde"""
  When I send mail to=abc@localhost.com
  Then I should read the same email
  