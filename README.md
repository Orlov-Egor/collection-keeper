# Collection Keeper
> It's a project that I was developing during my first year at university.  
> There are so many unrelated technologies in it, but that's the point :)

[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

![Server][server-screenshot-url]

## About The Project

Project contains _server_ and _client_ parts. Server one connects to a database and handles the collection in it. Client part can connect to server, log in to the account or register it and send the collection handling commands to the server. Collection objects can be added to database, removed from it, updated, sorted, filtered and so on.

Stack of technologies:
- Java (Core, Net, Collections, Sql, Concurrency...)
- PostgreSQL
- JavaFX
- Gradle
- Log4J

## Usage
Clone this repo to your local machine.
Use `gradle build` to build it.
```
java -jar server_all.jar <port> <db_host> <db_password>
java -jar client_all.jar <host> <port>
```

## Contact
Dmitry Sviridov  
drslampro@gmail.com  
[vk.com/slamach](https://vk.com/slamach)

[stars-shield]: https://img.shields.io/github/stars/slamach/collection-keeper
[stars-url]: https://github.com/slamach/collection-keeper/stargazers
[issues-shield]: https://img.shields.io/github/issues/slamach/collection-keeper
[issues-url]: https://github.com/slamach/collection-keeper/issues
[license-shield]: https://img.shields.io/github/license/slamach/collection-keeper
[license-url]: https://github.com/slamach/collection-keeper/blob/master/LICENSE
[server-screenshot-url]: https://github.com/slamach/collection-keeper/blob/master/doc/server_demo.png
