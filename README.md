My pet project:
================
**SpringBoot** chat bot for Twitch. Connection is based on Twitch4J API. Database - **PostgreSQL**. Views - **ThymeLeaf** and **BootStrap**. **Quartz** is for cron jobs. 

Packages descriptions:
-----------------
- **Connections** :
    - Connection utils and credential helpers to connect with Bot and Main accounts. Connections can be switched off for testing web only in main `@SpringbootApplication` class. `@Values` of credentials are injected by Spring from `twitchCredentials.properties`
- **Controllers** :
    - `PredictionsController` is used for /predictions endpoint. View is in `classpath:templates/predictions` The purpose of this controller is to make/cancel and resolve win/lose for predictions with buttons only. Logic is in `/utilities/UtilityCommandsMainChannel` making API requests to Twitch. The ThymeLeaf view is changed based on the prediction current state (active, closed, resolved), only right buttons are clickable at each state.
    ![image](https://t.ly/7_AAn)
    - `PredictionsRESTController` is REST controller for my project <a href="https://github.com/MaxonRash/prediction_client_3">CLIENT</a> for Hearthstone game. The logic of making and resolving predictions is the same as in `PredictionsController`, using `/utilities/UtilityCommandsMainChannel`. Client is based on parsing game logs and sending requests to this controller at certain time. Exceptions from Twitch API are caught and proper responses are sent to client.
    - `UsersController` is CRUD controller for twitch users. Related views are in `classpath:templates`. Main `/users` page looks like this: ![image](https://t.ly/0zKAY) 
- **DTOs** :
  - are data transfer objects used in `/utilities` UtilityCommands for sending requests to Twitch API.
- **event_handlers** :
  - `EventHandlerBot` is core class for **TEST channel** where `@EventSubscribers` are used to handle events sent from Twitch (messages in chat, subscribing to channel, etc.) and from twitch chat users (various commands for fun or accessing to data, like `DayOfBirth` of a user). Bot account is used here for interactions. Testing of commands are done here before being added to `EventHandlerMain`
  - `EventHandlerMain` is core class for **MAIN channel**. All tested handlers are copied here from `EventHandlerBot` and are used with Bot and Main channel accounts, depending on which permissions are needed.
- **models** :
  - `AppUser` is an `@Entity` used for Authentication in **Spring Security**. Passwords are stored encrypted by Bcrypt.
  - `User` is an `@Entity` for Twitch user accounts.
- **quartz** :
  - Classes used for checking if there are users whose birthdate is today. Checks are done at 00:05 every day and users with DOBs are added to `/utilities/UtilityDOB` list. Congratulations are printed at 14:30 in twitch chat. Also, there is a job (every hour starting from 14:00 to 23:00, which is during stream time) for a repeating message to remind users to add their DOBs to the database via chat command.
- **repositories** :
  - `AppUsersRepository` - JPA repository for users of the app.
  - `UsersRepository` - JPA repository for twitch users.
- **security**
  - `SecurityConfig` - config for Spring Security. No custom authentication page and no authorization roles in this app.
- **services**
  - `AppUserService` - app users service extending Spring's `UserDetailsService` for authentication in Spring Security.
  - `AppUserServiceImpl` - implementation of `AppUserService` to use `AppUsersRepository` for authentication in Spring Security.
  - `UsersService` - service for `UsersRepository`.
- **timers** :
  - All of these classes are timers helping in preventing users from spamming and cooldowns for commands in `EventHadlerBot` and `EventHandlerMain`.
- **utilities** :
  - `UserDOBComparator` is a comparator for the page of sorted users by month first. Default comparison is made by year first, that is not comfortable for this type of data.
  - `UtilityCommandsGlobal` - utility commands to Twitch API that do not depend on a channel, like getting global twitch_id.
  - `UtilityCommandsMainChannel` - all utility commands for twitch API used on main channel. 
  - `UtilityCommandsTestChannel` - all utility commands for twitch API used on test channel.
  - `UtilityDOB` - utilities for Date Of Birth related data, commands and crons.
-----
Database credentials are stored in `databaseCredentials.properties`. Necessary fields for this file are listed in `databaseCredentials.properties.original`.
*****
Credentials of twitch accounts are stored in `twitchCredentials.properties`. Tokens can be requested from twitch by registering your app. More info on twitch <a href="https://dev.twitch.tv/docs/authentication/">PAGE</a> Also, token can be retrieved by using third party apps like <a href="https://twitchtokengenerator.com/">THIS</a>.
*****
- **Useful links** :
  - <a href="https://github.com/twitch4j/twitch4j">Twitch4J API</a>
  - <a href="https://dev.twitch.tv/docs/api/reference/">Twitch developers API</a>