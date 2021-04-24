[![Build Status](https://travis-ci.org/fabianpol/twitterremider.svg?branch=master)](https://travis-ci.org/fabianpol/twitterremider)
[![codecov](https://codecov.io/gh/fabianpol/twitterremider/branch/main/graph/badge.svg?token=uk35F1Zsln)](https://codecov.io/gh/fabianpol/twitterremider)

## Stack
- Java 11
- [Micronaut](https://micronaut.io/)
- [H2](https://www.h2database.com/html/main.html) / [Postgresql](https://www.postgresql.org/)
- [Hibernate](https://hibernate.org/)
- [natty](http://natty.joestelmach.com/) - natural language date parser

## Configuration
### Environment variables
#### Twitter configuration
`twitter4j.oauth.accessToken`

`twitter4j.oauth.accessTokenSecret`

`twitter4j.oauth.consumerKey`

`twitter4j.oauth.consumerSecret`

#### Database configuration
By default, the application is using build-in H2 memory based database.
By setting the following environment variables, it can also use Postgresql.

`JDBC_DRIVER` - org.postgresql.Driver

`JDBC_URL` - i.e. jdbc:postgresql://localhost:5432/postgresql

`JDBC_USER` - i.e. postgres

`JDBC_PASSWORD` - i.e. postgre
