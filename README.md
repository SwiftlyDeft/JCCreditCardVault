### Requirements
1. Network connectivity using OKHTTP 4, requires android INTERNET permissions
2. Jetpack Composer 

### Decisions
1. Lay the foundations of a Service to manage API interactions, use coroutines to make the API request from the main screen as to not block the UI
2. Use credit card images for the type, provides a nicer look and feel
3. Used Jetpack Compose to utilise a more modern approach to Android development, reduced the amount of code normally used with the xml approach

### Assumptions 
1. Omission of correct formatting of the credit card type, it would of been nice to see the credit card types in their correct punctuations 
2. Credit card numbers are displayed in batches of 4 normally, made a decision to remove the hyphens to display them in a clearer way
3. Specification did not state about navigation, omitted detail view navigation for this exercise 

### Nice to have's
1. A better loading screen and a blank (loading) state
2. Pull to refresh

![Screenshot_20240408_070257_JCCreditCardViewer](https://github.com/SwiftlyDeft/JCCreditCardVault/assets/4774399/22cb2e89-b426-4818-94db-20c9e8ec21d4)
