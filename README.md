Cruxer - Social RouteSetting
============================
**Note** : Not yet in a functional state.

**Requires Node.js and npm.**

Run
===
Run server with `gradle run`.

Deploy
======
~~Create deployable with `gradle deployZip`. Extract `build/distributions/Cruxer-Deployable.zip` onto server and run `java -jar cruxer-0.0.1.jar`.~~
**Moving to a completely self-contained jar.**

Configuration
=============
For the Email Service work correctly, configure `spring.mail.username` and `spring.mail.password` located in `src/main/resources/application.yml`