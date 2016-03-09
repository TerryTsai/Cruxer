Cruxer - Social RouteSetting
============================
**Note** : Still a work in progress.

Usage
=====
```
// To quickly run
gradle run

- or -

// To create jar
gradle build
java -jar build/libs/cruxer-0.0.1.jar
```

Profiles
========
- `development` : serves web files from `file:src/main/resources/web`, so they can be modified without
having to redeploy. **This is currently the default.**

- `production` : serves web files from `classpath:/web/` to keep everything self contained. To use, either
set `spring.profiles.active=production` in `src/main/resources/application.yml` or run jar with
`-Dspring.profiles.active=production`.

Configuration
=============
For the Email Service work correctly, configure `spring.mail.username` and `spring.mail.password` located in `src/main/resources/application.yml`
