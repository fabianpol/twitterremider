[![Build Status](https://www.travis-ci.com/fabianpol/twitterremider.svg?branch=master)](https://www.travis-ci.com/fabianpol/twitterremider)
[![codecov](https://codecov.io/gh/fabianpol/twitterremider/branch/master/graph/badge.svg?token=uk35F1Zsln)](https://codecov.io/gh/fabianpol/twitterremider)

### Goal

🤖 Twitter bot allows you to set reminders for tweets. 🤖

### How it works

Mention me ([@remind_me_this]) and tell me when should I remind you about the tweet ("in 10 days",  "next month",  "8th of September", [etc].).
To cancel the reminder, leave a comment under the confirmation tweet with '/cancel' command.
 
<img src="https://github.com/fabianpol/twitterremider/raw/improve-readme/assets/example.png" width="50%" />

<img src="https://github.com/fabianpol/twitterremider/raw/improve-readme/assets/cancel.png" width="50%" />
___

## Application details

### Technology stack
- Java 11
- [Micronaut](https://micronaut.io/)
- [H2](https://www.h2database.com/html/main.html) / [Postgresql](https://www.postgresql.org/)
- [Hibernate](https://hibernate.org/)
- [natty](http://natty.joestelmach.com/) - natural language date parser

### Configuration
#### Environment variables
##### Twitter configuration
`twitter4j.oauth.accessToken`

`twitter4j.oauth.accessTokenSecret`

`twitter4j.oauth.consumerKey`

`twitter4j.oauth.consumerSecret`

##### Database configuration
By default, the application is using build-in H2 memory based database.
By setting the following environment variables, it can also use Postgresql.

`JDBC_DRIVER` - org.postgresql.Driver

`JDBC_URL` - i.e. jdbc:postgresql://localhost:5432/postgresql

`JDBC_USER` - i.e. postgres

`JDBC_PASSWORD` - i.e. postgre

[@remind_me_this]: https://twitter.com/remind_me_this
[etc]: http://natty.joestelmach.com/doc.jsp